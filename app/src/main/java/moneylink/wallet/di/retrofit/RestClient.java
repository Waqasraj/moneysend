package moneylink.wallet.di.retrofit;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;


import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {

    static {
        System.loadLibrary("native-lib");
    }
    public static native String RitpayDomesticbaseurl();
    public static native String baseUrlFromJNI();

    private static Retrofit retrofit = null;
    private static RestApi restClient = null;
    private static final int CONNECTION_TIMEOUT = 30; //seconds
    private static final int READ_TIMEOUT = 20; //seconds
    private static final int WRITE_TIMEOUT = 20; //seconds
    public static MediaType MEDIA_TYPE_IMAGE = MediaType.parse("placeholder/*");
    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data");
    private static Gson gson;


    public static final String RitpayDomestic_base_url = RitpayDomesticbaseurl(); // URL rest

    private static RestApi REST_CLIENT_BASE;
    private static Retrofit restAdapter_base;


    public static RestApi getBase() {
        if (REST_CLIENT_BASE == null) {
            REST_CLIENT_BASE = restAdapter_base.create(RestApi.class);
        }
        return REST_CLIENT_BASE;
    }

    static {
        setupRestClientBase();
    }


    private static void setupRestClientBase() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // loggingInterceptor.redactHeader();
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000,TimeUnit.SECONDS);
//        builder.connectTimeout(300, TimeUnit.SECONDS);
//        builder.readTimeout(80, TimeUnit.SECONDS);
//        builder.writeTimeout(90, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().setLenient().create();
        restAdapter_base = new Retrofit.Builder()
                .baseUrl(RitpayDomestic_base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

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
                    .baseUrl(baseUrlFromJNI())
                    .build();

        }
        return retrofit;
    }



    @NonNull
    public static RequestBody makeJSONRequestBody(JSONObject jsonObject) {
        String params = jsonObject.toString();
        return RequestBody.create(MEDIA_TYPE_TEXT, params);
    }



    @NonNull
    public static RequestBody makeGSONRequestBody(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return RequestBody.create(MEDIA_TYPE_TEXT, gson.toJson(jsonObject));
    }


    @NonNull
    public static String makeGSONString(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(jsonObject);
    }

}
