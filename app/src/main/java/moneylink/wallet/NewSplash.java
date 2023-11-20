package moneylink.wallet;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.SplashLoginBinding;
import moneylink.wallet.di.StaticHelper;
import moneylink.wallet.di.JSONdi.restRequest.VersionRequest;
import moneylink.wallet.di.JSONdi.restResponse.CountryIpResponse;
import moneylink.wallet.di.JSONdi.restResponse.DataResponse;
import moneylink.wallet.di.retrofit.RestApi;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.di.retrofit.TrackRestClient;
import moneylink.wallet.login_signup_module.MainActivityLoginSignUp;

public class NewSplash extends TootiBaseActivity<SplashLoginBinding> {

    private Handler mHandler;
    String version = "";


    static {
        System.loadLibrary("native-lib");
    }

    public static native String papi();

    @Override
    public int getLayoutId() {
        return R.layout.splash_login;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        getData();
        //getCountry();
    }


    public void getData() {
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

        Log.e("getData: ", "Request");
        RestApi restApi = RestClient.getClient().create(RestApi.class);
        Call<DataResponse> call = restApi.getData(RestClient.makeGSONRequestBody(request));

        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                         StaticHelper.KEY = response.body().Key;
                        Log.e("onResponse: ",StaticHelper.KEY );
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
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
            }
        });
    }


    public void getCountry() {
        Call<CountryIpResponse> call = TrackRestClient.get()
                .getIP(papi());

        call.enqueue(new Callback<CountryIpResponse>() {
            @Override
            public void onResponse(Call<CountryIpResponse> call, retrofit2.Response<CountryIpResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    sessionManager.setIpAddress(response.body().ip);
                    sessionManager.IpCountryName(response.body().country_name);
                } else {
                    sessionManager.setIpAddress("");
                    sessionManager.IpCountryName("");
                    // onMessage(getString(R.string.contact_to_admin));
                }
                goToNext();
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
        }, 200);
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
