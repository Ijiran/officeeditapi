package com.pxyz.officeeditapi.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 文件锁标志对象
 */
@JsonInclude(Include.NON_NULL)
public class FileLockToken {

    /**
     * 锁定关联ID
     */
    @JsonProperty("S")
    private String xWopiCorrelationId;
    
    /**
     * 位置-横轴
     */
    @JsonProperty("F")
    private String postX;
    /**
     * 位置-纵轴
     */
    @JsonProperty("E")
    private String postY;
    /**
     * 修改标识码
     */
    @JsonProperty("M")
    private String modifyCode;
    /**
     * 段落标识码
     */
    @JsonProperty("P")
    private String modifyPhrase;
    
    public String getXWopiCorrelationId() {
        return xWopiCorrelationId;
    }

    public void setXWopiCorrelationId(String xWopiCorrelationId) {
        this.xWopiCorrelationId = xWopiCorrelationId;
    }

    public String getPostX() {
        return postX;
    }

    public void setPostX(String postX) {
        this.postX = postX;
    }

    public String getPostY() {
        return postY;
    }

    public void setPostY(String postY) {
        this.postY = postY;
    }

    public String getModifyCode() {
        return modifyCode;
    }

    public void setModifyCode(String modifyCode) {
        this.modifyCode = modifyCode;
    }

    public String getModifyPhrase() {
        return modifyPhrase;
    }

    public void setModifyPhrase(String modifyPhrase) {
        this.modifyPhrase = modifyPhrase;
    }

    @Override
    public String toString() {
        return "FileLockToken [xWopiCorrelationId=" + xWopiCorrelationId + ", postX=" + postX + ", postY=" + postY
                + ", modifyCode=" + modifyCode + ", modifyPhrase=" + modifyPhrase + "]";
    }

}
