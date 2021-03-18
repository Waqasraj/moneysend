package tootipay.wallet.login_signup_module.forgot_pin;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.databinding.EnterOtpBankingBinding;
import tootipay.wallet.di.RequestHelper.ForgotPinRequestApprovedUserRequest;
import tootipay.wallet.di.RequestHelper.VerifyOTPRequest;
import tootipay.wallet.di.apicaller.ForgotPinRequestApprovedTask;
import tootipay.wallet.di.apicaller.VerifyOTPTask;
import tootipay.wallet.fragments.BaseFragment;

import tootipay.wallet.interfaces.OnOTPVerified;
import tootipay.wallet.interfaces.OnSuccessMessage;
import tootipay.wallet.utils.IsNetworkConnection;
import tootipay.wallet.utils.StringHelper;

import java.util.Arrays;

import tootipay.wallet.utils.Constants;

public class ForgotPinOTPFragment extends BaseFragment<EnterOtpBankingBinding>
        implements OnSuccessMessage, OnOTPVerified {

    Integer[] codeInputIds;
    String numberEmail;
    private CountDownTimer countDownTimer;
    ForgotPinRequestApprovedUserRequest requestApprovedUserRequest
            = new ForgotPinRequestApprovedUserRequest();

    public boolean isWithNumber;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        // initialize code EditText ids in array

        if (getArguments() != null) {
            numberEmail = getArguments().getString("user_number");
            isWithNumber = getArguments().getBoolean("is_with_number", false);
            requestApprovedUserRequest = getArguments().getParcelable("object_forgot");
        }


        codeInputIds = new Integer[]{
                binding.digitOne.getId(),
                binding.digitTwo.getId(),
                binding.digitThree.getId(),
                binding.digitFour.getId()
        };

        addTextWatcher();
        addOnTextChangeListeners();

        binding.confirmsignupotp.setOnClickListener(v -> {
            Constants.hideKeyboard(getBaseActivity());
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                verifyCode();
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });


        Constants.hideKeyboard(getBaseActivity());
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            requestApprovedUserRequest.languageId = getSessionManager().getlanguageselection();
            ForgotPinRequestApprovedTask task = new ForgotPinRequestApprovedTask(getContext(),
                    this);
            task.execute(requestApprovedUserRequest);
            startTimer();
        } else {
            onMessage(getString(R.string.no_internet));
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }


        binding.resendsignup.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                requestApprovedUserRequest.languageId = getSessionManager().getlanguageselection();
                ForgotPinRequestApprovedTask task = new ForgotPinRequestApprovedTask(getContext(),
                        this);
                task.execute(requestApprovedUserRequest);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopCounter();
    }

    /**
     * start the timer
     */
    void startTimer() {
        Log.e("startTimer: ", "start");
        countDownTimer = null;
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                long value = millisUntilFinished / 1000;
                if (getContext() != null) {
                    binding.resendsignup.setText(getString(R.string.resend_sms).concat("").concat(String.valueOf(value)));
                    binding.resendsignup.setClickable(false);
                }


            }

            public void onFinish() {
                binding.resendsignup.setClickable(true);
                if (getContext() != null) {
                    binding.resendsignup.setTextColor(getResources().getColor(R.color.colorPrimary));
                    binding.resendsignup.setText(getString(R.string.resend_otp));
                }
            }

        };
        countDownTimer.start();
    }


    /**
     * method will stop the counter
     */
    private void stopCounter() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.enter_otp_banking;
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


    /**
     * method will verify digits
     */
    void verifyCode() {
        String userCode = getCode();
        if (userCode.length() < 4) {
            onMessage(getString(R.string.askfordigit));
        } else {
            VerifyOTPRequest request = new VerifyOTPRequest();
            if (isWithNumber) {
                request.emailAddress = "";
                request.mobileNumber = StringHelper.parseNumber(numberEmail);
            } else {
                request.emailAddress = numberEmail;
                request.mobileNumber = "";
            }
            request.languageId = getSessionManager().getlanguageselection();
            request.OTP = getCode();
            VerifyOTPTask task = new VerifyOTPTask(getContext(), this);
            task.execute(request);
        }
    }

    /**
     * getting the code
     */
    private String getCode() {
        return binding.digitOne.getText().toString() + binding.digitTwo.getText().toString() + binding.digitThree.getText().toString() +
                binding.digitFour.getText().toString();
    }


    @Override
    public void onOTPVerified(boolean isVerified) {
        if (isVerified) {
            stopCounter();
            Bundle bundle = new Bundle();
            if (isWithNumber) {
                bundle.putString("user_number", numberEmail);
            } else {
                bundle.putString("user_number", numberEmail);
            }
            bundle.putBoolean("is_with_number", isWithNumber);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_forgotPinOTPFragment_to_forgotPasswordCreateNewPinFragment
                            , bundle);
        }
    }


    @Override
    public void onResponseMessage(String message) {
        Navigation.findNavController(binding.getRoot()).navigateUp();
        onMessage(message);
    }


    @Override
    public void onSuccess(String s) {
        onMessage(s);
        startTimer();
        binding.resendsignup
                .setTextColor(getResources().getColor(R.color.black_50));
    }

}
