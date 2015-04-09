package com.assignment4;

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface PostSvcApi
{
    /*------------------------------------------------------------------*/
    public static final String GET_FILES = "/video/information";
    public static final String GET_VIDEO_FILE = "/video/getfile";
    public static final String UPLOAD_FILE = "/video/upload";
    public static final String DELETE_FILE = "video/delete";
    public static final String LOGIN = "/login";
    /*-----------------------------------------------------------------*/
    @GET(GET_FILES)
    public List<Files> getFiles();

    @GET(GET_VIDEO_FILE)
    public boolean getVideoFile();

//    @POST(UPLOAD_FILE)
//    public boolean UploadFile(@);

    @POST(LOGIN)
    public boolean Login(@Query("username") String username,@Query("password") String password);

    @DELETE(DELETE_FILE)
    public boolean DeleteFile(@Query("position") int position);

    @POST(UPLOAD_FILE)
    @Multipart
    public String uploadFile(@Part("name") String name,@Part("file") TypedFile tf);


}



