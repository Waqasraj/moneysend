package tootipay.wallet.di.retrofit;


import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;


import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {
    public static final String base_url = "https://183.87.134.37/TotipayImagesApi/";
   // public static final String base_url = "https://totiwalletrest.totiwallet.com/";
    private static Retrofit retrofit = null;
    private static RestApi restClient = null;
    private static final int CONNECTION_TIMEOUT = 30; //seconds
    private static final int READ_TIMEOUT = 20; //seconds
    private static final int WRITE_TIMEOUT = 20; //seconds
    public static MediaType MEDIA_TYPE_IMAGE = MediaType.parse("placeholder/*");
    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data");
    private static Gson gson;


    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout
                    (CONNECTION_TIMEOUT,
                            TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,
                            TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(base_url)
                    .build();

        }
        return retrofit;
    }


    @NonNull
    public static RequestBody makeGSONRequestBody(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return RequestBody.create(MEDIA_TYPE_TEXT, gson.toJson(jsonObject));
    }

    @NonNull
    public static RequestBody makeJSONRequestBody(JSONObject jsonObject) {
        String params = jsonObject.toString();
        return RequestBody.create(MEDIA_TYPE_TEXT, params);
    }



}
