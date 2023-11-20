package moneylink.wallet.login_signup_module.fragments_sign_up;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;


import com.google.firebase.messaging.FirebaseMessaging;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import retrofit2.Call;
import retrofit2.Response;
import moneylink.wallet.NewSplash;
import moneylink.wallet.R;

import moneylink.wallet.databinding.NewSignupLoginBinding;
import moneylink.wallet.di.StaticHelper;
import moneylink.wallet.di.JSONdi.restRequest.UpdateDeviceToken;
import moneylink.wallet.di.JSONdi.restResponse.ResponseApi;
import moneylink.wallet.di.retrofit.RestApi;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.utils.ContextWrapper;


public class NewLoginCheck extends BaseFragment<NewSignupLoginBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    upDeviceToken(token);
                });

        binding.buttonCreate.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_newLoginCheck_to_signUpMobileNoFragment));

        binding.already.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.loginWithNumber);
        });


        if (getSessionManager().getDefaultLanguage().equalsIgnoreCase("en")) {
            binding.languageSpinner.setHint(getString(R.string.arabic_txt));
        } else {
            binding.languageSpinner.setHint(getString(R.string.english_txt));
        }


        binding.languageSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (position, item) -> onSignOut());
    }

    @Override
    public int getLayoutId() {
        return R.layout.new_signup_login;
    }


    private void upDeviceToken(String deviceToken) {
        UpdateDeviceToken token = new UpdateDeviceToken();
        token.Customerid = getSessionManager().getCustomerNo();
        token.DeviceToken = deviceToken;
        token.type = "ANDROID";
        RestApi restApi = RestClient.getClient().create(RestApi.class);
        Call<ResponseApi> responseCall = restApi.updateToken(token
                , StaticHelper.KEY);
        responseCall.enqueue(new retrofit2.Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi>
                    response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().status.equals(101)) {
                        getSessionManager().setDeviceToken(deviceToken);
                        StaticHelper.REQUEST_KEY = deviceToken;
                        StaticHelper.E_KEY = StaticHelper.REQUEST_KEY.concat(",").concat(StaticHelper.method());
                        Log.e("REQUEST_KEY" , StaticHelper.REQUEST_KEY  );
                        Log.e("E_KEY" , StaticHelper.E_KEY  );
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSignOut() {
        if (getSessionManager().getlanguageselection().equalsIgnoreCase("4")) {
            ContextWrapper.changeLang(getContext(), "en");
            getSessionManager().setDefaultLanuguage("en");
            getSessionManager().setlanguageselection("1");
        } else if (getSessionManager().getlanguageselection().equalsIgnoreCase("1")) {
            ContextWrapper.changeLang(getContext(), "ar");
            getSessionManager().setDefaultLanuguage("ar");
            getSessionManager().setlanguageselection("4");
        }
        Intent intent = new Intent(getBaseActivity(), NewSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

}
