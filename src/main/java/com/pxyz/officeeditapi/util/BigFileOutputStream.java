package com.pxyz.officeeditapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 实现用于直接响应一个输出流 
 * @author QLC
 */
public class BigFileOutputStream extends OutputStream implements javax.ws.rs.core.StreamingOutput {

    /** 日志记录器 */
    private Log log = LogFactory.getLog(this.getClass());

    private InputStream inputStream;

    /**
     * 默认构造器
     */
    public BigFileOutputStream(){
        
    }  
    /**
     * 定义构造器
     * @param inputStream
     */
    public BigFileOutputStream(InputStream inputStream)  
    {  
        this.inputStream = inputStream;  
    }  
      
    @Override
    /**
     * 写文件流，将文件流写入文件中
     */
    public void write(OutputStream output) throws IOException, WebApplicationException {  
        try {
            IOUtils.copy(inputStream, output);
        } catch (Exception e) {
            log.error("流写出失败:"+e.getMessage());
        }
    }  
      
    public InputStream getInputStream() {  
        return inputStream;  
    }

    public void setInputStream(InputStream inputStream) {  
        this.inputStream = inputStream;  
    }

    @Override
    public void write(int b) throws IOException {
        throw new IOException("Unspport method");
    }  
      
}  
