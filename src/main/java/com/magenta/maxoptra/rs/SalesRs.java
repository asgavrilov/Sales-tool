/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magenta.maxoptra.rs;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author gavrilov
 */
@Path("/sales")
public class SalesRs {

    private final static int MAX_IN_MEMORY_FILE_SIZE = 20 * 1024 * 1024;

    @GET  // этот метод будет гет, будет забирать поля
    @Path("/echo")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public String echo(@QueryParam("echo") String echo) {
        return echo;
    } //echo используется в гетах, для проверки функциональности

    @POST
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA) //после submit получаем форму
    @Produces(MediaType.APPLICATION_XML) //вернем результат API запроса от МХ
    public String update(@Context HttpServletRequest request) throws FileUploadException {
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(MAX_IN_MEMORY_FILE_SIZE);
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        String acc = "";
        String usr = "";
        String pass = "";
        String targetServer = "";
        byte[] dataArr = null;
        for (FileItem item : fileItems) {
            switch (item.getFieldName()) {
                case ("account"): {
                    acc = new String(item.get());
                    break;
                }
                case ("user"): {
                    usr = new String(item.get());
                    break;
                }
                case ("pwd"): {
                    pass = new String(item.get());
                    break;
                }
                case ("f"): {
                    dataArr = item.get();
                    break;
                }
                case  ("server"): {
                    targetServer = new String(item.get());
                }
                default: {
                    System.out.println("unknown " + item.getFieldName());
                }
            }
        }

        if (dataArr != null) {
            MxHttpClient.GenerateVehicles(acc, usr, pass, targetServer, dataArr);
            MxHttpClient.GeneratePerformers(acc, usr, pass, targetServer, dataArr);
            MxHttpClient.GenerateAssignments(acc, usr, pass, targetServer, dataArr);
        }
        return "<result>done</result>";
    }
}