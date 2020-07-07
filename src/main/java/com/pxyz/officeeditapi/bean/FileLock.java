package com.pxyz.officeeditapi.bean;

import javax.ws.rs.core.Response.Status;

/**
 * 文件锁对象
 */
public class FileLock {

    /**
     * 锁状态（具体操作的状态，常见状态为OK-200，Conflict-409等）
     */
    private Status status;

    /**
     * 锁定串
     */
    private String xWopiLock;
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getXWopiLock() {
        return xWopiLock;
    }

    public void setXWopiLock(String xWopiLock) {
        if(xWopiLock == null){
            xWopiLock = "";//header内容不允许为NULL
        }
        this.xWopiLock = xWopiLock;
    }

    @Override
    public String toString() {
        return "FileLockBean [status=" + status + ", xWopiLock=" + xWopiLock + "]";
    }

}
