package com.example.wefixdelear.Api;

import com.example.wefixdelear.model.Category1Response;
import com.example.wefixdelear.model.CategoryResponse;
import com.example.wefixdelear.model.DelearResponse;
import com.example.wefixdelear.model.LogResponse;
import com.example.wefixdelear.model.MyResponse;
import com.example.wefixdelear.model.Service1Response;
import com.example.wefixdelear.model.ServiceResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("delearlogin")
    Call<DelearResponse> delearLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @PUT("updatedelearpassword")
    Call<MyResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("username") String username
    );

    @FormUrlEncoded
    @PUT("getcategorybyid/{tbl_category_id}")
    Call<Category1Response> getCategoryByID(
            @Path("tbl_category_id") int tbl_category_id,
            @Field("app") String app
    );

    @FormUrlEncoded
    @PUT("getcalllogfordelear/{ref_delear_id}")
    Call<LogResponse> getCallLogForDelear(
            @Path("ref_delear_id") int ref_delear_id,
            @Field("app") String app
    );

    @GET("getcategory")
    Call<CategoryResponse> getCategory1();

    @FormUrlEncoded
    @PUT("getservice/{id}")
    Call<Service1Response> getService(
            @Path("id") int id,
            @Field("app") String app
    );

    @GET("getallservice")
    Call<ServiceResponse> getAllServices();

    @FormUrlEncoded
    @POST("addcalllogfordelear")
    Call<ResponseBody> addCallLog(
            @Field("call_log_date") String call_log_date,  //1
            @Field("call_log_type") String call_log_type,  //2
            @Field("ref_delear") int ref_delear,    //3
            @Field("client_name") String client_name,    //4
            @Field("client_address") String address,//5
            @Field("client_pin") String client_pin,  //6
            @Field("client_mb") String client_mb,   //7
            @Field("client_email") String client_email, //8
            @Field("ref_cat_id") int ref_cat_id,       //9
            @Field("ref_service_id") int ref_service_id, //10
            @Field("ref_company_id") int ref_company_id,  //11
            @Field("product_company") String product_company,
            @Field("amount") double amount,               //12
            @Field("payment_type") String payment_type,
            @Field("problem") String problem,          //13
            @Field("entry_tim") String entry_tim,     //14
            @Field("call_log_status") String call_log_status,  //15
            @Field("client_log_ip") String client_log_ip
    );

}
