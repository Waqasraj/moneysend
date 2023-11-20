package moneylink.wallet.menumodules;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityChangePinBinding;
import moneylink.wallet.di.RequestHelper.ChangePin;
import moneylink.wallet.di.apicaller.ChangePinTask;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.IsNetworkConnection;


public class ChangePinActivity extends TootiBaseActivity<ActivityChangePinBinding>
        implements OnSuccessMessage {


    public boolean isValidate() {

        if (TextUtils.isEmpty(binding.oldPin.getText().toString())) {
            onMessage(getString(R.string.enter_old_pin));
            return false;
        } else if (TextUtils.isEmpty(binding.newPin.getText().toString())) {
            onMessage(getString(R.string.enter_new_pin));
            return false;
        } else if (TextUtils.isEmpty(binding.confirmPin.getText().toString())) {
            onMessage(getString(R.string.enter_confirm_pin));
            return false;
        } else if (binding.oldPin.getText().toString().length() < 4) {
            onMessage(getString(R.string.pin_must_4_digit));
            return false;
        } else if (binding.oldPin.getText().toString().length() < 4) {
            onMessage(getString(R.string.pin_must_4_digit));
            return false;
        } else if (binding.oldPin.getText().toString().length() < 4) {
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
    protected void initUi(Bundle savedInstanceState) {
        binding.changePin.setOnClickListener(v -> {
            Constants.hideKeyboard(this);
            if (isValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(this)) {
                    ChangePin changePin = new ChangePin();
                    changePin.oldPin = binding.oldPin.getText().toString();
                    changePin.newPin = binding.newPin.getText().toString();
                    changePin.confirmPin = binding.confirmPin.getText().toString();
                    changePin.customerNo = sessionManager.getCustomerNo();
                    changePin.languageId = sessionManager.getlanguageselection();
                    ChangePinTask task = new ChangePinTask(this, this);
                    task.execute(changePin);
                }
            }

        });


        binding.toolBarFinal.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onSuccess(String s) {
        onResponseMessage(getString(R.string.pin_changed_successfully));
        Handler mHandler;
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                finish();
            }
        }, 400);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}