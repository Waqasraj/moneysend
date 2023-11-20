package moneylink.wallet.di.retrofit;

import android.content.Context;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chandresh on 18/10/2017.
 */

public abstract class RestCallback<T> implements Callback<T> {

    private final Context mContext;

    public RestCallback(Context context) {
        this.mContext = context;
    }

    //success response
    protected abstract void success(Response<T> response);

    //error
    protected abstract void failure();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() != null && response.body() instanceof ArrayList && ((ArrayList) response.body()).size() > 0) {
            success(response);
        } else {
            failure();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        failure();
    }
}
