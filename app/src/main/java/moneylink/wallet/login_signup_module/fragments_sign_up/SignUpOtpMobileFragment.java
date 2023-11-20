package moneylink.wallet.login_signup_module.fragments_sign_up;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.databinding.EnterOtpBankingBinding;
import moneylink.wallet.di.RequestHelper.AuthenticationRequest;
import moneylink.wallet.di.RequestHelper.VerifyOTPRequest;
import moneylink.wallet.di.apicaller.AuthenticationRequestTask;
import moneylink.wallet.di.apicaller.VerifyOTPTask;
import moneylink.wallet.dialogs.AlertDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.interfaces.OnOTPVerified;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.login_signup_module.MainActivityLoginSignUp;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.IsNetworkConnection;

import java.util.Arrays;

public class SignUpOtpMobileFragment extends BaseFragment<EnterOtpBankingBinding>
        implements OnSuccessMessage, OnOTPVerified, OnDecisionMade {


    Integer[] codeInputIds;
    private CountDownTimer countDownTimer;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            AuthenticationRequest request = new AuthenticationRequest();

            request.email = ((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.email;
            request.mobileNumber = ((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.phoneNumber;
            AuthenticationRequestTask task = new AuthenticationRequestTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        // initialize code EditText ids in array
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


        binding.resendsignup.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                AuthenticationRequest request = new AuthenticationRequest();

                request.email = ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.email;
                request.mobileNumber = ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.phoneNumber;
                AuthenticationRequestTask task = new AuthenticationRequestTask(getContext(), this);
                task.execute(request);
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

    /**
     * start the timer
     */
    void startTimer() {
        countDownTimer = null;
        countDownTimer = new CountDownTimer(60000 * 5, 1000) {

            public void onTick(long millisUntilFinished) {
                long value = millisUntilFinished / 1000;
                String text = getString(R.string.resend_sms).concat(" ").concat(getDurationString((int)value));
                binding.resendsignup.setText(text);
                binding.resendsignup.setClickable(false);
            }

            public void onFinish() {
                binding.resendsignup.setClickable(true);
                binding.resendsignup.setEnabled(true);
                binding.resendsignup.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.resendsignup.setText(getString(R.string.resend_otp));
            }

        };
        countDownTimer.start();
    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    /**
     * method will stop the counter
     */
    private void stopCounter() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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
            request.mobileNumber = ((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.phoneNumber;
            request.emailAddress = ((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.email;
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
    public int getLayoutId() {
        return R.layout.enter_otp_banking;
    }


    @Override
    public void onResponseMessage(String message) {
        Navigation.findNavController(binding.getRoot()).navigateUp();
        onMessage(message);
    }

    @Override
    public void onOTPVerified(boolean isVerified) {
        if (isVerified) {
            stopCounter();
            String boldCode = Html.fromHtml("<b>" + getCode() + "</b>").toString();
            String message = getString(R.string.otp_message).concat(" ")
                    .concat(boldCode).concat(" ")
                    .concat(getString(R.string.remember_me_otp));
            AlertDialog alertDialog = new AlertDialog(getString(R.string.attention_txt)
                    , message, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            alertDialog.show(transaction, "");
        }
    }

    @Override
    public void onProceed() {
        stopCounter();
        Navigation.findNavController(getView())
                .navigate(R.id.action_signUpOtpMobileFragment_to_newSignupCreateProfile);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);
        startTimer();
        binding.resendsignup.setTextColor(getResources().getColor(R.color.black_50));
    }
}
