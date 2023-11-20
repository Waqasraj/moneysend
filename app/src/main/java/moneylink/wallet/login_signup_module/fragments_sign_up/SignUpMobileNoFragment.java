package moneylink.wallet.login_signup_module.fragments_sign_up;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentSignUpMobileNoBinding;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.login_signup_module.MainActivityLoginSignUp;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.StringHelper;

public class SignUpMobileNoFragment extends BaseFragment<FragmentSignUpMobileNoBinding> implements OnSelectCountry {


    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_code));
            return false;
        } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        }
        else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                , binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.numberLayout.countrySpinnerSignIn.setOnClickListener(v -> {
            Constants.hideKeyboard(getBaseActivity());
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry dialogCountry = new DialogCountry(this);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                dialogCountry.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.sentOtpEmail.setOnClickListener(v -> {
            Constants.hideKeyboard(getBaseActivity());
            Navigation.findNavController(v)
                    .navigate(R.id.action_signUpMobileNoFragment_to_fragmentSignUpForEmail);
        });


        binding.continuesighupb.setOnClickListener(v -> {
            Constants.hideKeyboard(getBaseActivity());
            if (isValidate()) {
                String phoneNumber = binding.numberLayout.mobilesignupb.getText().toString();
                String userNumber = binding.numberLayout.countryCodeTextView.getText().toString().concat(phoneNumber);
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.phoneNumber = StringHelper.parseNumber(userNumber);

                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    Navigation.findNavController(v)
                            .navigate(R.id.action_signUpMobileNoFragment_to_signUpOtpMobileFragment);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }
        });

        binding.numberLayout.mobilesignupb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_up_mobile_no;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.numberLayout.countryCodeTextView.setText(country.countryCode);
        setSendingCurrencyImage(country.imageURL);
    }

    public void setSendingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.ic_united_kingdom)
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