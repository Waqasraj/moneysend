package moneylink.wallet.di.retrofit;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import moneylink.wallet.di.JSONdi.restRequest.UpdateDeviceToken;
import moneylink.wallet.di.JSONdi.restResponse.AEResponse;
import moneylink.wallet.di.JSONdi.restResponse.CountryIpResponse;
import moneylink.wallet.di.JSONdi.restResponse.DataResponse;
import moneylink.wallet.di.JSONdi.restResponse.GetProfileImage;
import moneylink.wallet.di.JSONdi.restRequest.UploadKYCImage;
import moneylink.wallet.di.JSONdi.restRequest.UploadUserImageRequest;
import moneylink.wallet.di.JSONdi.restResponse.ResponseApi;


public interface RestApi {
//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("BillPayment/GetPrepaidOperator")
//    Call<AEResponse> getOperator(@Body RequestBody requestBody,
//                                 @Header("key") String key, @Header("secretkey") String sKey);
//
//
//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("BillPayment/GetPlanCategories")
//    Call<AEResponse> getPlanName(@Body RequestBody requestBody,
//                                 @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/MakeWABillPayment")
    Call<AEResponse> getPayBill(@Body RequestBody requestBody,
                                @Header("key") String key, @Header("secretkey") String sKey);

//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("BillPayment/GetRechargePlans")
//    Call<AEResponse> getRechargePlanName(@Body RequestBody requestBody,
//                                         @Header("key") String key, @Header("secretkey") String sKey);
//

//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("BillPayment/GetBillDetail")
//    Call<AEResponse> getBillDetails(@Body RequestBody requestBody,
//                                    @Header("key") String key, @Header("secretkey") String sKey);

    @GET("json")
    Call<CountryIpResponse> getIP(@Query("key") String key);

    @POST("TotiPayRestAPI/KYC/UploadKYCImage")
    Call<ResponseApi> uploadAttachment(@Body UploadKYCImage uploadKYCImage
            , @Header("Key") String key, @Header("RequestKey") String requestKey);



    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWASKUByBillerName")
    Call<AEResponse> getWASKUByBillerName(@Body RequestBody requestBody,
                                          @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWABillerNames")
    Call<AEResponse> GetWRBillerNames(@Body RequestBody requestBody,
                                      @Header("key") String key, @Header("secretkey") String sKey);
    //GetWRBillerFields
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWABillerFields")
    Call<AEResponse> GetWRBillerFields(@Body RequestBody requestBody,
                                       @Header("key") String key, @Header("secretkey") String sKey);


//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("WR/GetWRBillerType")
//    Call<AEResponse> GetWRBillerType(@Body RequestBody requestBody,
//                                     @Header("key") String key, @Header("secretkey") String sKey);

    //Country
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWABillerCategoryCountry")
    Call<AEResponse> GetCountryList(@Body RequestBody requestBody,
                                    @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWABillerCategory")
    Call<AEResponse> GetWRBillerCategory(@Body RequestBody requestBody,
                                         @Header("key") String key, @Header("secretkey") String sKey);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWABillerDetail")
    Call<AEResponse> getWRBillDetail(@Body RequestBody requestBody,
                                     @Header("key") String key, @Header("secretkey") String sKey);



    @POST("TotiPayRestAPI/Auth/UploadDeviceToken")
    Call<ResponseApi> updateToken(@Body UpdateDeviceToken token , @Header("Key") String key);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("TotiPayRestAPI/KYC/GetCustomerImage2")
    Call<GetProfileImage> getCustomerImage(@Body RequestBody requestBody
            , @Header("Key") String key, @Header("RequestKey") String requestKey);


//    @HTTP(method = "GET", path = "TotiPayRestAPI/Images/GetCustomerImage", hasBody = true)
//    Call<GetProfileImage> getCustomerImage(@Body GetCustomerProfileImageRequest request);

    @POST("TotiPayRestAPI/KYC/AddCustomerImage")
    Call<ResponseApi> uploadCustomerImage(@Body UploadUserImageRequest request ,
                                          @Header("Key") String key, @Header("RequestKey") String requestKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("TotiPayRestAPI/Auth/ENAB")
    Call<DataResponse> getData(@Body RequestBody request);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWA_Prepaid_BillerNames_byMobileNo")
    Call<AEResponse> getPrepaidBillerNames(@Body RequestBody requestBody,
                                           @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWA_Prepaid_BillerNames_byMobileNo_Retry")
    Call<AEResponse> getPrepaidBillerNamesRetry(@Body RequestBody requestBody,
                                                @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWAPaymentDetail")
    Call<AEResponse> getWAPaymentDetail(@Body RequestBody requestBody,
                                        @Header("key") String key, @Header("secretkey") String sKey);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWA_Prepaid_CountryWise_Billers")
    Call<AEResponse> getPrepaidOperatorsCountryWise(@Body RequestBody requestBody,
                                                    @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WA/GetWA_Prepaid_Countrylist")
    Call<AEResponse> getPrepaidCountry(@Body RequestBody requestBody,
                                       @Header("key") String key, @Header("secretkey") String sKey);

//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("WR/GetWRCountryList")
//    Call<AEResponse> getCountryList(@Body RequestBody requestBody,
//                                    @Header("key") String key, @Header("secretkey") String sKey);


//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("BillPayment/GetRechargePlans")
//    Call<AEResponse> getPrepaidPlan(@Body RequestBody requestBody,
//                                    @Header("key") String key, @Header("secretkey") String sKey);

//    @Headers("Content-Type:application/json;charset=UTF-8")
//    @POST("WR/WRPrepaidRecharge")
//    Call<AEResponse> prepaidRecharge(@Body RequestBody requestBody,
//                                     @Header("key") String key, @Header("secretkey") String sKey);


}