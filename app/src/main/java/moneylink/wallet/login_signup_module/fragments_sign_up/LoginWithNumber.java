package moneylink.wallet.login_signup_module.fragments_sign_up;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


import retrofit2.Call;

import moneylink.wallet.R;
import moneylink.wallet.databinding.LoginWithNumberLayoutBinding;
import moneylink.wallet.di.RequestHelper.GetCustomerProfileRequest;
import moneylink.wallet.di.RequestHelper.LoginRequest;
import moneylink.wallet.di.ResponseHelper.CustomerProfile;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.StaticHelper;
import moneylink.wallet.di.apicaller.GetCustomerProfileTask;
import moneylink.wallet.di.apicaller.LoginRequestTask;
import moneylink.wallet.di.JSONdi.restRequest.GetCustomerProfileImageRequest;
import moneylink.wallet.di.JSONdi.restResponse.GetProfileImage;
import moneylink.wallet.di.retrofit.RestApi;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.NewDashboardActivity;
import moneylink.wallet.interfaces.OnGetCustomerProfile;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.interfaces.OnSuccessLogin;
import moneylink.wallet.login_signup_module.MainActivityLoginSignUp;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.ProgressBar;
import moneylink.wallet.utils.StringHelper;

public class LoginWithNumber extends BaseFragment<LoginWithNumberLayoutBinding> implements
        OnSuccessLogin, OnSelectCountry, OnGetCustomerProfile {

    Integer[] codeInputIds;
    ProgressBar progressBar;
    String url = "";

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_code));
            return false;
        } else if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_number_error));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.mobilesignupb.getText().toString()
                , binding.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        }

        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        // initialize code EditText ids in array
        if(getSessionManager().getUserCountryCode() != null) {
            binding.countryCodeTextView.setText(((MainActivityLoginSignUp)
                    getBaseActivity()).sessionManager.getUserCountryCode());
        }
        if(!getSessionManager().getUserNumberRemember().isEmpty()) {
            binding.mobilesignupb.setText(getSessionManager()
                    .getUserNumberRemember());
        }
        if (getSessionManager().getURLFlag() != null) {
            setSendingCurrencyImage(getSessionManager().getURLFlag());
        }


        if (!TextUtils.isEmpty(binding.mobilesignupb.getText())) {
            binding.rememberMeBox.setChecked(true);
        }
        setSendingCurrencyImage(getSessionManager().getURLFlag());

        codeInputIds = new Integer[]{
                binding.digitOne.getId(),
                binding.digitTwo.getId(),
                binding.digitThree.getId(),
                binding.digitFour.getId()
        };

        addTextWatcher();
        addOnTextChangeListeners();
        progressBar = new ProgressBar();


        binding.loginBtn.setOnClickListener(v -> {
            Constants.hideKeyboard(getActivity());
            if (isValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    verifyCode();
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }
        });


        binding.countrySpinnerSignIn.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry country = new DialogCountry(this::onSelectCountry);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                country.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });


        binding.forgotPin.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("login_with_number", true);
            Navigation.findNavController(v)
                    .navigate(R.id.action_loginWithNumber_to_forgotPinNumberFragment
                            , bundle);
        });


      binding.rememberMeBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
           if (isChecked) {
        if (!TextUtils.isEmpty(binding.countryCodeTextView
                .getText().toString()) &&
                !TextUtils.isEmpty(binding.mobilesignupb.getText().toString()) && !url.isEmpty()) {

            getSessionManager().userPhoneRemember(url,
                    binding.countryCodeTextView.getText().toString()
                    , binding.mobilesignupb.getText().toString()
            );
        }
          } else {
                getSessionManager()
                        .userPhoneRemember(getSessionManager().getURLFlag(),
                       "", "");
            }
      });
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_with_number_layout;
    }


    /**
     * method will verify digits
     */
    void verifyCode() {
        String userCode = getCode();
        if (userCode.length() < 4) {
            onMessage(getString(R.string.askfordigit));
        } else {


            if(binding.rememberMeBox.isChecked()) {

                if (!TextUtils.isEmpty(binding.countryCodeTextView
                        .getText().toString()) &&
                        !TextUtils.isEmpty(binding.mobilesignupb.getText().toString()) && !url.isEmpty()) {

                    getSessionManager().userPhoneRemember(url,
                            binding.countryCodeTextView.getText().toString()
                            , binding.mobilesignupb.getText().toString()
                    );

                }
            }


            if(TextUtils.isEmpty(binding.countryCodeTextView.getText().toString())) {
                onMessage(getString(R.string.plz_select_country_code));
                return;
            }


            if (isValidate()) {
                String userNumber = binding.countryCodeTextView.getText().toString()
                        + binding.mobilesignupb.getText().toString();

                progressBar.showProgressDialogWithTitle(getContext(), getString(R.string.getting_data_loading));
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.emailAddress = "";
                loginRequest.mobileNumber = StringHelper.parseNumber(userNumber);
                getSessionManager().setLoginPhone(StringHelper.parseNumber(userNumber));
                loginRequest.password = getCode();
                loginRequest.languageId = getSessionManager().getlanguageselection();
                LoginRequestTask task = new LoginRequestTask(getContext(), true, this);
                task.execute(loginRequest);
            }
        }
    }

    /**
     * getting the code
     */
    private String getCode() {
        return binding.digitOne.getText().toString() + binding.digitTwo.getText().toString() + binding.digitThree.getText().toString() +
                binding.digitFour.getText().toString();
    }

    /**
     * method will init the text watcher
     */
    private void addTextWatcher() {
        for (int id : codeInputIds) {
            EditText editText = getView().findViewById(id);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0 && editText.getId() != binding.digitFour.getId()) {
                        // check if input is not empty, then jump to next
                        int next = Arrays.asList(codeInputIds).indexOf(editText.getId()) + 1;
                        getView().findViewById(codeInputIds[next]).requestFocus();
                    }


                    if (editText.getId() == binding.digitFour.getId()) {
                        if (s.length() > 0) {
                            Constants.hideKeyboard(getActivity());
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /**
     * method will help to find previous box on delete button
     */
    private void addOnTextChangeListeners() {
        for (int id : codeInputIds) {
            getView().findViewById(id)
                    .setOnKeyListener((v, actionId, event) -> {

                        if (event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_DEL
                                && ((EditText) v).length() == 0
                                && v.getId() != binding.digitOne.getId()) {
                            // check if input is empty, then jump to previous
                            int prev = Arrays.asList(codeInputIds).indexOf(v.getId()) - 1;
                            EditText prevView = getView().findViewById(codeInputIds[prev]);
                            // Also clear previous value
                            prevView.setText("");
                            prevView.requestFocus();

                            return true;
                        }
                        return false;
                    });
        }
    }

    @Override
    public void onResponseMessage(String message) {
        if (progressBar.isShowing()) {
            progressBar.hideProgressDialogWithTitle();
        }
        onMessage(message);
    }

    @Override
    public void onSuccessLogin(String customerNo) {
        ((MainActivityLoginSignUp) getBaseActivity())
                .sessionManager.putCustomerPhone(StringHelper.parseNumber(
                binding.countryCodeTextView.getText() +
                        binding.mobilesignupb.getText().toString()
        ));
        ((MainActivityLoginSignUp) getBaseActivity())
                .sessionManager.setCustomerNo(customerNo);


        getCustomerProfile();
    }


    private void getCustomerProfile() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetCustomerProfileRequest request = new GetCustomerProfileRequest();
            request.mobileNo = ((MainActivityLoginSignUp) getBaseActivity())
                    .sessionManager.getCustomerPhone();
            request.customerNo = ((MainActivityLoginSignUp) getBaseActivity())
                    .sessionManager.getCustomerNo();
            request.emailAddress = ((MainActivityLoginSignUp) getBaseActivity())
                    .sessionManager.getEmail();
            request.languageId = getSessionManager().getlanguageselection();
            GetCustomerProfileTask task = new GetCustomerProfileTask(getContext(), true, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        setSendingCurrencyImage(country.imageURL);
        url = country.imageURL;
        binding.countryCodeTextView.setText(country.countryCode);
    }

    @Override
    public void onGetCustomerProfile(CustomerProfile customerProfile) {
        getSessionManager().setCustomerGet(customerProfile);

        ((MainActivityLoginSignUp) getBaseActivity())
                .sessionManager.isKYCApproved(customerProfile.isApprovedKYC);

        getSessionManager().setDocumentsUploaded(customerProfile.isDocUploaded);
        getCustomerImage();
    }

    private void getCustomerImage() {

        GetCustomerProfileImageRequest request = new GetCustomerProfileImageRequest();
        request.Customer_No = ((MainActivityLoginSignUp) getBaseActivity()).sessionManager.getCustomerNo();
        request.credentials.LanguageID = Integer.parseInt(getSessionManager().getlanguageselection());
        RestApi restApi = RestClient.getClient().create(RestApi.class);
        Call<GetProfileImage> call = restApi.getCustomerImage(RestClient.makeGSONRequestBody(request)
         , StaticHelper.KEY ,StaticHelper.E_KEY);
        call.enqueue(new retrofit2.Callback<GetProfileImage>() {

            @Override
            public void onResponse(@NotNull Call<GetProfileImage> call, @NotNull retrofit2.Response<GetProfileImage> response) {
                progressBar.hideProgressDialogWithTitle();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().status == 101) {
                            getSessionManager().customerImage(response.body().imageData);
                            //{"ResponseCode":100,"Description":"Request failed"}
                        } else {
                            getSessionManager().customerImage("");
                        }
                        startActivity(new Intent(getActivity(), NewDashboardActivity.class));
                        getBaseActivity().finish();
                    }
                } else {
                    getSessionManager().customerImage("");
                    startActivity(new Intent(getActivity(), NewDashboardActivity.class));
                    getBaseActivity().finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetProfileImage> call, @NotNull Throwable t) {
                progressBar.hideProgressDialogWithTitle();

                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
                getSessionManager().customerImage("");
                startActivity(new Intent(getActivity(), NewDashboardActivity.class));
                getBaseActivity().finish();
            }
        });

    }

    public void setSendingCurrencyImage(String urlImage) {
        Glide.with(this)
                .asBitmap()
                .load(urlImage)
                .placeholder(R.drawable.ic_united_kingdom)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.imageIcon.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
