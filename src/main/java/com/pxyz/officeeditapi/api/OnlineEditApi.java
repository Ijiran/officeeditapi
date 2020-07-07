package com.pxyz.officeeditapi.api;

import com.pxyz.officeeditapi.service.OnlineEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Ijiran
 * @Package com.pxyz.officeeditapi.api
 * @date 2020-07-04 16:21
 */
@Controller
@Path("/")
public class OnlineEditApi {

    @Autowired
    private OnlineEditService onlineEditService;

    /**
     * 获取文件信息
     * @param fileId
     * @param accessToken
     * @return
     */
    @GET
    @Path("{file_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFileInfo(@PathParam("file_id") String fileId, @QueryParam("access_token") String accessToken) {
        System.out.println("getFileInfo：" + fileId);
        return onlineEditService.getFileInfo(fileId, accessToken);
    }

    /**
     * 获取文件流
     * @param fileId
     * @param accessToken
     * @return
     */
    @GET
    @Path("{file_id}/contents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileInputStream(@PathParam("file_id") String fileId, @QueryParam("access_token") String accessToken) {
        System.out.println("getFileInputStream：" + fileId);
        return onlineEditService.getFileInputStream(fileId, accessToken);
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
    @POST
    @Path("{file_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lock(@PathParam("file_id") String fileId, @QueryParam("access_token") String accessToken,
                         @HeaderParam("x-wopi-lock") String xWopiLock, @HeaderParam("x-wopi-override") String xWopiOverride,
                         @HeaderParam("x-wopi-oldlock") String xWopiOldLock, @HeaderParam("x-wopi-correlationid") String xWopiCorrelationId) {
        System.out.println("lock：" + fileId);
        return onlineEditService.lock(fileId, accessToken, xWopiLock, xWopiOverride, xWopiOldLock, xWopiCorrelationId);
    }

    /**
     * 保存文件
     * @param request
     * @param fileId
     * @param accessToken
     * @param xWopiLock
     * @param xWopiOverride
     * @return
     */
    @POST
    @Path("{file_id}/contents")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putFile(@Context HttpServletRequest request, @PathParam("file_id") String fileId,@QueryParam("access_token") String accessToken, @HeaderParam("x-wopi-lock") String xWopiLock,@HeaderParam("x-wopi-override") String xWopiOverride){
        System.out.println("putFile：" + fileId);
        Response response = null;
        try {
            response = onlineEditService.putFile(fileId, accessToken, xWopiLock, xWopiOverride, request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
