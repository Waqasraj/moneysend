package tootipay.wallet.di.retrofit;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tootipay.wallet.di.restResponse.CountryIpResponse;
import tootipay.wallet.di.restResponse.GetProfileImage;
import tootipay.wallet.di.restRequest.UploadKYCImage;
import tootipay.wallet.di.restRequest.UploadUserImageRequest;
import tootipay.wallet.di.restResponse.ResponseApi;


public interface RestApi {

    @GET("json")
    Call<CountryIpResponse> getIP(@Query("key") String key);

    @POST("TotiPayRestAPI/Images/UploadKYCImage")
    Call<ResponseApi> uploadAttachment(@Body UploadKYCImage uploadKYCImage);

    @GET("TotiPayRestAPI/Images/GetCustomerImage2")
    Call<GetProfileImage> getCustomerImage(@Query("Customer_No") String customerNo
            , @Query("PartnerCode") Integer partnerCode, @Query("UserName") String username,
                                           @Query("UserPassword") String password,
     @Query("LanguageID") Integer languageId);


//    @HTTP(method = "GET", path = "TotiPayRestAPI/Images/GetCustomerImage", hasBody = true)
//    Call<GetProfileImage> getCustomerImage(@Body GetCustomerProfileImageRequest request);

    @POST("TotiPayRestAPI/Images/AddCustomerImage")
    Call<ResponseApi> uploadCustomerImage(@Body UploadUserImageRequest request);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("TotiPayRestAPI/Version/GetVersion")
    Call<ResponseApi> checkVersion(@Body RequestBody request);

}