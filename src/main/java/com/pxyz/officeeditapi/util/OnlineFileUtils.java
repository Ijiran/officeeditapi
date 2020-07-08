package com.pxyz.officeeditapi.util;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;

import com.pxyz.officeeditapi.bean.FileInfo;
import org.apache.commons.codec.binary.Base64;

/**
 * 在线编辑文件工具类
 */
public class OnlineFileUtils {

    /**
     * 在线编辑文件基本信息
     * @param map
     * @return
     */
    public static FileInfo packOnlineFileInfo(Map<String,Object> map){
        Date time;
        if(map.get("UPDATE_TIME") != null){
            time = (Date)map.get("UPDATE_TIME");
        }else{
            time = (Date)map.get("CREATE_TIME");
        }
        String filePath = map.get("FTP_FILE_PATH").toString();
        String ownerId = "admin";
        FileInfo onlineFileInfo = new FileInfo();
        try {
            long version = time.getTime(); // 文件版本号
            InputStream inputStream = getFileInputStream(filePath);
            onlineFileInfo.setBaseFileName(map.get("FTP_FILE_NAME").toString());
            onlineFileInfo.setOwnerId(ownerId);
            onlineFileInfo.setVersion(version);
            onlineFileInfo.setSize(inputStream.available());
            onlineFileInfo.setUserId("qlc");
            onlineFileInfo.setSha256(getHash256(inputStream));//文件的256位bit的SHA-2编码散列内容
            onlineFileInfo.setAllowExternalMarketplace(true);
            onlineFileInfo.setSupportsUpdate(true);
            onlineFileInfo.setSupportsLocks(true);
            onlineFileInfo.setUserCanWrite(true);
            onlineFileInfo.setUserCanRename(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onlineFileInfo;
    }

    /**
     * 获取文件版本
     * @param createTime
     * @param updateTime
     * @return
     */
    public static Long getFileVersion(Object createTime, Object updateTime){
        long version = 0L;
        try {
            Date time;
            if(createTime != null){
                time = (Date)updateTime;
            }else{
                time = (Date)createTime;
            }
            version = time.getTime(); // 文件版本号
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取文件流
     * @param filePath
     * @return
     */
    public static InputStream getFileInputStream(String filePath){
        if(filePath.startsWith("/")) filePath = filePath.substring(1);
        //读取FTP文件流
        InputStream inputStream = null;
        return inputStream;
    }

    /**
     * 获取文件流的SHA-256值
     * @param inputStream
     * @return
     */
    public static String getHash256(InputStream inputStream) {
        String value = "";
        // 获取hash值
        try {
            byte[] buffer = new byte[1024];
            int numRead;
            //如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest complete = MessageDigest.getInstance("SHA-256");
            do {
                //从文件读到buffer
                numRead = inputStream.read(buffer);
                if (numRead > 0) {
                    //用读到的字节进行MD5的计算，第二个参数是偏移量
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            inputStream.close();
            value = new String(Base64.encodeBase64(complete.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
