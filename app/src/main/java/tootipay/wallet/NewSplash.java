package tootipay.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.SplashLoginBinding;
import tootipay.wallet.di.restRequest.Credentials;
import tootipay.wallet.di.restRequest.VersionRequest;
import tootipay.wallet.di.restResponse.CountryIpResponse;
import tootipay.wallet.di.restResponse.ResponseApi;
import tootipay.wallet.di.retrofit.RestApi;
import tootipay.wallet.di.retrofit.RestClient;
import tootipay.wallet.di.retrofit.TrackRestClient;
import tootipay.wallet.login_signup_module.MainActivityLoginSignUp;

public class NewSplash extends TootiBaseActivity<SplashLoginBinding> {


    //    code for language screen

    private Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.splash_login;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        getVersion();
    }


    public void getCountry() {
        Call<CountryIpResponse> call = TrackRestClient.get()
                .getIP("xqgm3PkZb4RTXmxMmikizvh4d0IDDjELyWW9VtLYETSANVG8H0");

        call.enqueue(new Callback<CountryIpResponse>() {
            @Override
            public void onResponse(Call<CountryIpResponse> call, retrofit2.Response<CountryIpResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    sessionManager.setIpAddress(response.body().ip);
                    sessionManager.IpCountryName(response.body().country_name);
                    goToNext();
                } else {
                    sessionManager.setIpAddress("");
                    sessionManager.IpCountryName("");
                    goToNext();
                    // onMessage(getString(R.string.contact_to_admin));
                }
            }

            @Override
            public void onFailure(Call<CountryIpResponse> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                sessionManager.setIpAddress("");
                sessionManager.IpCountryName("");
                goToNext();
                //  onMessage(getString(R.string.contact_to_admin));
            }
        });
    }


    String version = "";

    public void getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        VersionRequest request = new VersionRequest();
        request.DeviceType = "Android";

        RestApi restApi = RestClient.getClient().create(RestApi.class);
        Call<ResponseApi> call = restApi.checkVersion(RestClient.makeGSONRequestBody(request));
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().status.equals(101)) {
                        if (response.body().version.equalsIgnoreCase(version)) {
                            getCountry();
                        } else {
                            updateAvailable();
                        }
                    }
                } else {
                    Log.e("Fasle: ", "false");
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
            }
        });
    }


    void goToNext() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                startActivity(new Intent(getApplicationContext(), MainActivityLoginSignUp.class));
                /* startActivity(new Intent(getApplicationContext(),NewDashboardActivity.class));*/
                finish();
            }
        }, 2000);
    }


    public void updateAvailable() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.update_available))
                .setMessage(getString(R.string.update_available_msg))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + appPackageName)));
                        } catch (ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
