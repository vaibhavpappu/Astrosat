package com.ne.project.astrosat;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by vaibhav on 18-03-2018.
 */
interface RegisterAPI {
    @FormUrlEncoded
    @POST("/https://astrosat.000webhostapp.com/insert.php")
    public void insertUser(
            @Field("target_name") String target_name,
            @Field("target_name1") String target_name1,
            @Field("target_name2") String target_name2,
            @Field("target_name3") String target_name3,
            @Field("target_name4") String target_name4,
            @Field("target_name5") String target_name5,
            @Field("report") String report,
            @Field("config") String config,
            Callback<Response> callback);
}

