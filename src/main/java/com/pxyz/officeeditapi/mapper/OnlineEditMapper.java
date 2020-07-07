package com.pxyz.officeeditapi.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface OnlineEditMapper {

    /**
     * 获取科研现有附件信息
     * @param fileId
     * @return
     */
    Map<String,Object> getFileInfo(String fileId);

    /**
     * 更新文件信息
     * @param fileId
     */
    void updateFileInfo(String fileId);
	
}
