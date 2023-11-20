package moneylink.wallet.login_signup_module.forgot_pin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.ActivityChangePinBinding;
import moneylink.wallet.di.RequestHelper.ForgotPinSetNewRequest;
import moneylink.wallet.di.apicaller.SetNewPinTask;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.StringHelper;

public class ForgotPasswordCreateNewPinFragment extends BaseDialogFragment<ActivityChangePinBinding>
        implements OnSuccessMessage {
    String numberEmail;
    public boolean isWithNumber;

    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.newPin.getText().toString())) {
            onMessage(getString(R.string.enter_new_pin));
            return false;
        } else if (TextUtils.isEmpty(binding.confirmPin.getText().toString())) {
            onMessage(getString(R.string.enter_confirm_pin));
            return false;
        } else if (CheckValidation.isValidPinLength(binding.newPin.getText().toString())) {
            onMessage(getString(R.string.pin_must_4_digit));
            return false;
        } else if (CheckValidation.isValidPinLength(binding.confirmPin.getText().toString())) {
            onMessage(getString(R.string.pin_must_4_digit));
            return false;
        } else if (!TextUtils.equals(binding.newPin.getText().toString(), binding.confirmPin.getText().toString())) {
            onMessage(getString(R.string.pin_must_be_same));
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pin;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.oldPin.setVisibility(View.GONE);
        binding.oldPinTxt.setVisibility(View.GONE);


        if (getArguments() != null) {
            numberEmail = getArguments().getString("user_number");
            isWithNumber = getArguments().getBoolean("is_with_number", false);
        }

        binding.changePin.setOnClickListener(v -> {
            if (isValidate()) {
                ForgotPinSetNewRequest newRequest = new ForgotPinSetNewRequest();
                if(isWithNumber) {
                    newRequest.mobileNumber = StringHelper.parseNumber(numberEmail);
                    newRequest.emailAddress = "";
                } else {
                    newRequest.mobileNumber = "";
                    newRequest.emailAddress = numberEmail;
                }
                newRequest.newPin = binding.newPin.getText().toString();
                newRequest.confirmPin = binding.confirmPin.getText().toString();
                newRequest.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    SetNewPinTask task = new SetNewPinTask(getContext(), this);
                    task.execute(newRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });
    }



    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);
        Navigation.findNavController(getView())
                .navigateUp();
    }
}
