package tootipay.wallet.login_signup_module.forgot_pin;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import tootipay.wallet.R;
import tootipay.wallet.databinding.ForgotPinNumberEmailLayoutBinding;
import tootipay.wallet.di.RequestHelper.ForgotPinRequestApprovedUserRequest;
import tootipay.wallet.di.ResponseHelper.CustomerProfile;
import tootipay.wallet.di.ResponseHelper.GetCountryListResponse;
import tootipay.wallet.dialogs.DialogCountry;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnGetCustomerProfile;
import tootipay.wallet.interfaces.OnSelectCountry;
import tootipay.wallet.utils.CheckValidation;
import tootipay.wallet.utils.IsNetworkConnection;
import tootipay.wallet.utils.StringHelper;

public class ForgotPinNumberFragment extends BaseFragment<ForgotPinNumberEmailLayoutBinding>
        implements OnSelectCountry, OnGetCustomerProfile {


    public boolean isLoginWithNumber = false;

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (isLoginWithNumber) {
            if (TextUtils.isEmpty(binding.numberLayout.countryCodeTextView.getText().toString())) {
                onMessage(getString(R.string.select_country));
                return false;
            } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
                onMessage(getString(R.string.enter_mobile_no_error));
                return false;
            } else if (!CheckValidation
                    .isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                            , binding.numberLayout.countryCodeTextView.getText().toString())) {
                onMessage(getString(R.string.invalid_number));
                return false;
            }
        } else {
            if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
                onMessage(getString(R.string.enter_email_error));
                return false;
            } else if (!CheckValidation.isValidEmail(binding.mobilesignupb.getText().toString())) {
                onMessage(getString(R.string.enter_email_error));
                return false;
            }
        }

        return true;
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {

        isLoginWithNumber = getArguments().getBoolean("login_with_number", false);

        if (isLoginWithNumber) {
            binding.mobilesignupb.setVisibility(View.GONE);
            binding.emailView.setVisibility(View.GONE);
            binding.numberLayout.mainNumber.setVisibility(View.VISIBLE);
            binding.title.setText(getString(R.string.Please_enteR_nmber));
        } else {
            binding.title.setText(getString(R.string.Please_Enter_email_txt));
            binding.mobilesignupb.setHint(getString(R.string.enteremaail_adress));
        }

        binding.continuesighupb.setOnClickListener(v -> {
            if (isValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {


                    Bundle bundle1 = new Bundle();
                    bundle1.putBoolean("is_with_number", isLoginWithNumber);
                    ForgotPinRequestApprovedUserRequest request = new ForgotPinRequestApprovedUserRequest();
                    if (isLoginWithNumber) {
                        request.mobileNumber = StringHelper.parseNumber(
                                binding.numberLayout.countryCodeTextView.getText() +
                                        binding.numberLayout.mobilesignupb.getText().toString()
                        );
                        request.emailAddress = "";

                    } else {
                        request.mobileNumber = "";
                        request.emailAddress = binding.mobilesignupb.getText().toString();
                    }
                    if (isLoginWithNumber) {
                        bundle1.putString("user_number", binding.numberLayout.countryCodeTextView.getText().toString()
                                + binding.numberLayout.mobilesignupb.getText().toString());
                    } else {
                        bundle1.putString("user_number", binding.mobilesignupb.getText().toString());
                    }
                    request.idExpireDate = "";
                    request.idNumber = "";
                    request.languageId = getSessionManager().getlanguageselection();
                    bundle1.putParcelable("object_forgot", request);
                    Navigation.findNavController(getView())
                            .navigate(R.id.action_forgotPinNumberFragment_to_forgotPinOTPFragment
                                    , bundle1);
                                    } else {
                    onMessage(getString(R.string.no_internet));
                }



//
//
//                    GetCustomerProfileRequest request = new GetCustomerProfileRequest();
//                    if (isLoginWithNumber) {
//                        request.emailAddress = "";
//                        request.mobileNo = StringHelper.parseNumber(binding.numberLayout.countryCodeTextView.getText().toString()
//                                + binding.numberLayout.mobilesignupb.getText().toString());
//                    } else {
//                        request.emailAddress = binding.mobilesignupb.getText().toString();
//                        request.mobileNo = "";
//                    }
//                    request.languageId = getSessionManager().getlanguageselection();
//                    GetCustomerProfileTask task = new GetCustomerProfileTask(getContext(), this);
//                    task.execute(request);


            }
        });


        binding.numberLayout.countrySpinnerSignIn.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry dialogCountry = new DialogCountry(this);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                dialogCountry.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.forgot_pin_number_email_layout;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.numberLayout.countryCodeTextView.setText(country.countryCode);
        setReceivingCurrencyImage(country.imageURL);
    }

    @Override
    public void onGetCustomerProfile(CustomerProfile customerProfile) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_with_number", isLoginWithNumber);
        if (isLoginWithNumber) {
            bundle.putString("user_number", binding.numberLayout.countryCodeTextView.getText().toString()
                    + binding.numberLayout.mobilesignupb.getText().toString());
        } else {
            bundle.putString("user_number", binding.mobilesignupb.getText().toString());
        }
//        if (customerProfile.isApprovedKYC) {
//            Navigation.findNavController(getView())
//                    .navigate(R.id.action_forgotPinNumberFragment_to_forgotPinUserSecurityDataFragment
//                            , bundle);
//        } else {

            Bundle bundle1 = new Bundle();
            bundle1.putBoolean("is_with_number", isLoginWithNumber);
            ForgotPinRequestApprovedUserRequest request = new ForgotPinRequestApprovedUserRequest();
            if (isLoginWithNumber) {
                request.mobileNumber = StringHelper.parseNumber(
                        binding.numberLayout.countryCodeTextView.getText() +
                                binding.numberLayout.mobilesignupb.getText().toString()
                );
                request.emailAddress = "";

            } else {
                request.mobileNumber = "";
                request.emailAddress = binding.mobilesignupb.getText().toString();
            }
            if (isLoginWithNumber) {
                bundle1.putString("user_number", binding.numberLayout.countryCodeTextView.getText().toString()
                        + binding.numberLayout.mobilesignupb.getText().toString());
            } else {
                bundle1.putString("user_number", binding.mobilesignupb.getText().toString());
            }
            request.idExpireDate = "";
            request.idNumber = "";
            request.languageId = getSessionManager().getlanguageselection();
            bundle1.putParcelable("object_forgot", request);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_forgotPinNumberFragment_to_forgotPinOTPFragment
                            , bundle1);
       // }
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    public void setReceivingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.numberLayout.imageIcon.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
