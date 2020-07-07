package com.pxyz.officeeditapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pxyz.officeeditapi.bean.FileInfo;
import com.pxyz.officeeditapi.bean.FileLock;
import com.pxyz.officeeditapi.mapper.OnlineEditMapper;
import com.pxyz.officeeditapi.util.BigFileOutputStream;
import com.pxyz.officeeditapi.util.FtpUtils;
import com.pxyz.officeeditapi.util.JacksonUtil;
import com.pxyz.officeeditapi.util.OnlineFileUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Ijiran
 * @Package com.pxyz.officeeditapi.service
 * @date 2020-07-05 21:36
 */
@Service
public class OnlineEditService {

    @Autowired
    private OnlineEditMapper onlineEditMapper;

    @Autowired
    private FileLockService fileLockService;

    //url：http://10.85.60.213/we/WordEditorFrame.aspx?WOPISrc=http://10.85.215.68:8080/wopiHost/wopi/files/738EF0BB642147B5C054330EACC9A5E4

    /**
     * 获取文件信息
     * @param fileId
     * @param accessToken
     * @return
     */
    public String getFileInfo(String fileId, String accessToken) {
        String result = null;
        try {
            Map<String,Object> map = onlineEditMapper.getFileInfo(fileId);
            if(map!=null){
                FileInfo onlineFileInfo = OnlineFileUtils.packOnlineFileInfo(map);
                result = JacksonUtil.objectToJson(onlineFileInfo);
                System.out.println(result);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取文件流
     * @param fileId
     * @param accessToken
     * @return
     */
    public Response getFileInputStream(String fileId, String accessToken) {
        Response resp = null;
        BigFileOutputStream bfStream = null;
        try {
            Map<String,Object> map = onlineEditMapper.getFileInfo(fileId);
            if(map!=null){
                String filePath = map.get("FTP_FILE_PATH").toString();
                String fileName = map.get("FTP_FILE_NAME").toString();
                InputStream inputStream = OnlineFileUtils.getFileInputStream(filePath);
                Integer fileSize = inputStream.available();// 文件大小
                bfStream = new BigFileOutputStream(inputStream);
                resp = Response.ok(bfStream, MediaType.APPLICATION_OCTET_STREAM).build(); // 直接返回输出流
                resp.getHeaders().add(HTTP.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM);
                resp.getHeaders().add("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
                resp.getHeaders().add(HTTP.CONTENT_LEN, fileSize);// 文件大小
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            resp = Response.status(Response.Status.NOT_FOUND).build();
        }
        return resp;
    }

    /**
     * 文件加锁
     * @param fileId
     * @param accessToken
     * @param xWopiLock
     * @param xWopiOverride
     * @param xWopiOldLock
     * @param xWopiCorrelationId
     * @return
     */
    public Response lock(String fileId, String accessToken, String xWopiLock,String xWopiOverride, String xWopiOldLock,String xWopiCorrelationId) {
        Map<String,Object> map = onlineEditMapper.getFileInfo(fileId);
        String fileExt = map.get("FILE_EXT_NAME").toString();
        Response resp = null;
        try {
            FileLock fileLock = null;
            String operationName = xWopiOverride;
            if(".docx".equalsIgnoreCase(fileExt)  || ".doc".equalsIgnoreCase(fileExt)){
                if("GET_LOCK".equals(xWopiOverride)){
                    fileLock = fileLockService.getLockFile(fileId);
                }else if("LOCK".equals(xWopiOverride)){
                    if(xWopiOldLock == null || xWopiOldLock.trim().length() == 0){
                        //LOCK
                        fileLock = fileLockService.lockFile(fileId, xWopiLock);
                    }else{
                        //UnlockAndRelock
                        fileLock = fileLockService.unlockAndRelockFile(fileId, xWopiLock, xWopiOldLock);
                        operationName = "UnlockAndRelock";
                    }
                }else if("UNLOCK".equals(xWopiOverride)){
                    /**
                     * 注：unlock时，xWopiLock不是JSON,而是“GetCurrentLockString-986f5f5ea86741c5b0521f94c0733ba6”
                     * 所有不能使用，只能使用xWopiCorrelationId
                     */
                    fileLock = fileLockService.unlockFile(fileId, xWopiCorrelationId);
                }else if("REFRESH_LOCK".equals(xWopiOverride)){
                    fileLock = fileLockService.refreshLockFile(fileId, xWopiLock);
                }
                if(fileLock == null){
                    resp = Response.status(Response.Status.BAD_REQUEST).build();
                }else{
                    System.out.println(fileLock);
                    resp = Response.status(fileLock.getStatus()).build();
                    resp.getHeaders().add("X-WOPI-Lock", fileLock.getXWopiLock());
                    resp.getHeaders().add("X-WOPI-LockFailureReason", operationName + " fail:"+fileLock.getStatus().getReasonPhrase());
                    resp.getHeaders().add("X-WOPI-ItemVersion",  OnlineFileUtils.getFileVersion(map.get("CREATE_TIME"),map.get("UPDATE_TIME")));
                }
            }else{
                resp = Response.ok().build();
            }
        } catch (Exception e) {
            resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            resp.getHeaders().add("X-WOPI-LockFailureReason", e.getMessage());
        }
        return resp;
    }

    /**
     * 保存文件
     * @param fileId
     * @param accessToken
     * @param xWopiLock
     * @param xWopiOverride
     * @param inputStream
     * @return
     */
    public  Response putFile(String fileId, String accessToken, String xWopiLock, String xWopiOverride, InputStream inputStream) {
        Map<String,Object> fileMap = onlineEditMapper.getFileInfo(fileId);
        String filePath = fileMap.get("FTP_FILE_PATH").toString();
        Response resp = null;
        try {
            if(filePath.startsWith("/")) filePath = filePath.substring(1);
            //删除FTP源文件
            FtpUtils.deleteFile(FtpUtils.InternationUploadPath + filePath);
            //上传FTP
            FtpUtils.uploadFile(inputStream, FtpUtils.InternationUploadPath + filePath);
            //更新文件信息
            onlineEditMapper.updateFileInfo(fileId);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("X-WOPI-Lock", xWopiLock);
            map.put("X-WOPI-ItemVersion", OnlineFileUtils.getFileVersion(fileMap.get("CREATE_TIME"), fileMap.get("UPDATE_TIME")));
            map.put("X-WOPI-LockFailureReason", "");
            resp = Response.ok().build();
            Iterator<String> it = map.keySet().iterator();
            String key;
            while(it.hasNext()){
                key = it.next();
                resp.getHeaders().add(key, map.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

}
