package com.Aahan.wefix.Api;

import com.Aahan.wefix.model.Category1Response;
import com.Aahan.wefix.model.CategoryResponse;
import com.Aahan.wefix.model.Company1Response;
import com.Aahan.wefix.model.CompanyResponse;
import com.Aahan.wefix.model.DelearResponse;
import com.Aahan.wefix.model.LogResponse;
import com.Aahan.wefix.model.MyResponse;
import com.Aahan.wefix.model.PaidLogResponse;
import com.Aahan.wefix.model.Service1Response;
import com.Aahan.wefix.model.Service2Response;
import com.Aahan.wefix.model.ServiceResponse;
import com.Aahan.wefix.model.SumAllLogResponse;
import com.Aahan.wefix.model.WarrantyLogResponse;

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
    @POST("uploadimage")
    Call<MyResponse> uploadImage(
            @Field("title") String title,
            @Field("location") String location,
            @Field("encoded_image") String encoded_image
    );

    @FormUrlEncoded
    @PUT("updatedelearpassword")
    Call<MyResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("username") String username
    );

    @GET("getcompany")
    Call<CompanyResponse> getCompany();

    @PUT("getcategorybyid/{tbl_category_id}")
    Call<Category1Response> getCategoryByID(@Path("tbl_category_id") int tbl_category_id);

    @PUT("getcalllogfordelear/{ref_delear_id}")
    Call<LogResponse> getCallLogForDelear(@Path("ref_delear_id") int ref_delear_id);

    @PUT("getpaidcalllog/{ref_delear_id}")
    Call<PaidLogResponse> getPaidCallLog(@Path("ref_delear_id") int ref_delear_id);

    @PUT("sumallpaidlog/{ref_delear_id}")
    Call<SumAllLogResponse>sumAllPaidLog(@Path("ref_delear_id") int ref_delear_id);

    @PUT("getwarrantycalllogfordelear/{ref_delear_id}")
    Call<WarrantyLogResponse> getWarrantyCallLog(@Path("ref_delear_id") int ref_delear_id);

    @PUT("getcompanybyid/{tbl_company_id}")
    Call<Company1Response> getCompanyById(@Path("tbl_company_id") int tbl_company_id);

    @GET("getcategory")
    Call<CategoryResponse> getCategory1();

    @PUT("getservice/{id}")
    Call<Service1Response> getService(@Path("id") int id);

    @PUT("getservice1/{id}")
    Call<Service2Response> getServiceByID(@Path("id") int id);

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

    @FormUrlEncoded
    @POST("addwarrantycalllogfordelear")
    Call<MyResponse> addWarrantyCallLog(
            @Field("call_log_date") String call_log_date,  //1
            @Field("call_log_type") String call_log_type,  //2
            @Field("client_name") String client_name,    //4
            @Field("client_address") String address,//5
            @Field("client_pin") String client_pin,  //6
            @Field("client_mb") String client_mb,   //7
            @Field("client_email") String client_email, //8
            @Field("ref_company_id") int ref_company_id,  //11
            @Field("model_no") String model_no, //10
            @Field("serial_no") String serial_no, //10
            @Field("purchase_date") String purchase_date,
            @Field("image") String image,
            @Field("amount") double amount,               //12
            @Field("payment_type") String payment_type,
            @Field("problem") String problem,          //13
            @Field("entry_time") String entry_tim,     //14
            @Field("call_log_status") String call_log_status,  //15
            @Field("client_log_ip") String client_log_ip,
            @Field("ref_delear") int ref_delear,
            @Field("category") String category
    );

}
