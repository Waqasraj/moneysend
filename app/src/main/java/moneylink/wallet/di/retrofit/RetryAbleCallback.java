package moneylink.wallet.di.retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HS on 25/12/2017.
 */

public abstract class RetryAbleCallback<T> implements Callback<T> {
    private static final String TAG = RetryAbleCallback.class.getSimpleName();
    private final Call<T> call;
    private int totalRetries = 3;
    private int retryCount = 0;

    private RetryAbleCallback(Call<T> call, int totalRetries) {
        this.call = call;
        this.totalRetries = totalRetries;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() != 200)
            if (retryCount++ < totalRetries) {
                Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + totalRetries + ")");
                retry();
            } else
                onFinalResponse(call, response);
        else
            onFinalResponse(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        try {
            Log.e(TAG, t.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if (retryCount++ < totalRetries) {
            Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + totalRetries + ")");
            retry();
        } else
            onFinalFailure(call, t);
    }

    public abstract void onFinalResponse(Call<T> call, Response<T> response);


    public abstract void onFinalFailure(Call<T> call, Throwable t);

    private void retry() {
        call.clone().enqueue(this);
    }
}
