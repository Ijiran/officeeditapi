package com.pxyz.officeeditapi.service;

import javax.ws.rs.core.Response.Status;

import com.pxyz.officeeditapi.bean.FileLock;
import com.pxyz.officeeditapi.bean.FileLockToken;
import com.pxyz.officeeditapi.util.JacksonUtil;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 文件编辑锁 
 * 目前没有实现lock timer的刷新操作，待以后再改进
 */
@Service
@Scope(value="singleton")
public class FileLockService {

    /**
     * 文件锁池
     */
    private ConcurrentMap<String, FileLockToken> fileLockMap = new ConcurrentHashMap<String, FileLockToken>();

    /**
     * 获得文件锁
     * @param key 文件ID
     * @return
     * @author QLC
     */
    private FileLockToken getLock(String key){
        return fileLockMap.get(key);
    }
    
    /**
     * 锁定文件
     * @param key 文件ID
     * @param lockTokenJson 锁定串对象
     * @return
     * @author QLC
     */
    private void lock(String key,FileLockToken lockTokenJson){
        fileLockMap.put(key, lockTokenJson);
    }

    /**
     * 解锁定文件
     * @param key 文件ID
     * @return
     * @author QLC
     */
    private void unlock(String key){
        fileLockMap.remove(key);
    }

    /**
     * 将JSON串转换成FileLockToken对象
     * @param lockTokenJson
     * @return
     * @author QLC
     */
    private FileLockToken getTokenBean(String lockTokenJson){
        FileLockToken bean = null;
        try {
            bean =  JacksonUtil.jsonToObject(lockTokenJson, FileLockToken.class);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return bean;
    }

    /**
     * lockTokenBean对象转JSON
     * @param lockTokenBean
     * @return
     * @author QLC
     */
    private String getTokenJson(FileLockToken lockTokenBean){
        String json = null;
        try {
            json =  JacksonUtil.objectToJson(lockTokenBean);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return json;
    }

    /**
     * 获取文件锁（未锁定不创建）
     * If the file is currently not locked, the host must return a 200 OK and include an X-WOPI-Lock response header 
     * set to the empty string
     * @param fileId 文档ID
     * @param fileId 锁定串
     * @return
     * @throws JsonProcessingException
     */
    public FileLock getLockFile(String fileId) throws JsonProcessingException{
        FileLock lock = new FileLock();
        FileLockToken oTokenBean = this.getLock(fileId);
        if(oTokenBean != null){//已锁
            lock.setStatus(Status.OK);
            lock.setXWopiLock(JacksonUtil.objectToJson(oTokenBean));
        }else{//未锁
            lock.setStatus(Status.OK);
            lock.setXWopiLock(null);
        }
        return lock;
    }

    /**
     * 锁定文件
     * 1.冲突(如果文件已经被锁定，且非当前用户),则返回409,成功，返回200
     * @param fileId 文档ID
     * @param lockTokenJson 锁定串
     * @return
     * @author QLC
     */
    public FileLock lockFile(String fileId,String lockTokenJson){
        FileLock lock = new FileLock();
        FileLockToken oTokenBean = this.getLock(fileId);
        FileLockToken nTokenBean = this.getTokenBean(lockTokenJson);
        if(oTokenBean != null){//已锁
            if(!oTokenBean.getXWopiCorrelationId().equals(nTokenBean.getXWopiCorrelationId())){//已锁，且不匹配
                lock.setStatus(Status.CONFLICT);
                lock.setXWopiLock(this.getTokenJson(oTokenBean));
            }else{////已锁，匹配
                lock.setStatus(Status.OK);
                lock.setXWopiLock(lockTokenJson);
                this.lock(fileId, nTokenBean);//锁定
                //TODO the host should refresh the lock timer and return 200 OK
            }
        }else{//未锁
            lock.setStatus(Status.OK);
            lock.setXWopiLock(lockTokenJson);
            this.lock(fileId, nTokenBean);//锁定
        }
        return lock;
    }
    
    /**
     * 刷新锁
     * 1.The RefreshLock operation refreshes the lock on a file by resetting its automatic expiration timer 
     * to 30 minutes
     * @param fileId 文档ID
     * @param lockTokenJson 锁定串
     * @return
     * @author QLC
     */
    public FileLock refreshLockFile(String fileId,String lockTokenJson){
        FileLock lock = new FileLock();
        FileLockToken oTokenBean = this.getLock(fileId);
        FileLockToken nTokenBean = this.getTokenBean(lockTokenJson);
        if(oTokenBean != null){//已锁
            if(!oTokenBean.getXWopiCorrelationId().equals(nTokenBean.getXWopiCorrelationId())){//已锁，且不匹配
                lock.setStatus(Status.CONFLICT);
                lock.setXWopiLock(this.getTokenJson(oTokenBean));
            }else{////已锁，匹配
                lock.setStatus(Status.OK);
                lock.setXWopiLock(lockTokenJson);
                this.lock(fileId, nTokenBean);
                //TODO the host should refresh the lock timer and return 200 OK
            }
        }else{//未锁
            lock.setStatus(Status.CONFLICT);
            lock.setXWopiLock(null);
        }
        return lock;
    }

    /**
     * 解锁
     * @param fileId 文档ID
     * @param xWopiCorrelationId
     * @return
     * @author QLC
     */
    public FileLock unlockFile(String fileId,String xWopiCorrelationId){
        FileLock lock = new FileLock();
        FileLockToken oTokenBean = this.getLock(fileId);
        if(oTokenBean != null){//已锁
            if(!oTokenBean.getXWopiCorrelationId().equals(xWopiCorrelationId)){//已锁，且不匹配
                lock.setStatus(Status.CONFLICT);
                lock.setXWopiLock(this.getTokenJson(oTokenBean));
            }else{////已锁，匹配
                lock.setStatus(Status.OK);
                lock.setXWopiLock(this.getTokenJson(oTokenBean));
                this.unlock(fileId);//解锁
            }
        }else{//未锁
            lock.setStatus(Status.CONFLICT);
            lock.setXWopiLock(null);
        }
        return lock;
    }

    /**
     * 解锁并重新锁定
     * @param fileId 文档ID
     * @param lockTokenJson 锁定串
     * @param oldLockToken 原锁定串
     * @return
     * @author QLC
     */
    public FileLock unlockAndRelockFile(String fileId,String lockTokenJson,String oldLockToken){
        FileLock lock = new FileLock();
        FileLockToken oTokenBean = this.getLock(fileId);
        FileLockToken oldLockTokenBean = this.getTokenBean(oldLockToken);
        if(oTokenBean != null){//已锁
            if(!oTokenBean.getXWopiCorrelationId().equals(oldLockTokenBean.getXWopiCorrelationId())){//已锁，且不匹配
                lock.setStatus(Status.CONFLICT);
                lock.setXWopiLock(getTokenJson(oTokenBean));
            }else{//已锁，匹配
                lock.setStatus(Status.OK);
                lock.setXWopiLock(lockTokenJson);
                this.unlock(fileId);//解锁
                this.lock(fileId, this.getTokenBean(lockTokenJson));//重新锁定
            }
        }else{//未锁
            lock.setStatus(Status.CONFLICT);
            lock.setXWopiLock(null);
        }
        return lock;
    }

}
