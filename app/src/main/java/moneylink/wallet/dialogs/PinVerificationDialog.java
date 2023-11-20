package moneylink.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.Arrays;

import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.PinVerificationDialogBinding;

import moneylink.wallet.di.RequestHelper.MatchPinRequest;
import moneylink.wallet.di.apicaller.MatchPinTask;
import moneylink.wallet.interfaces.OnUserPin;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.IsNetworkConnection;

public class PinVerificationDialog extends BaseDialogFragment<PinVerificationDialogBinding>
        implements OnUserPin {

    Integer[] codeInputIds;
    OnUserPin onUserPin;


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    public PinVerificationDialog(OnUserPin onUserPin) {
        this.onUserPin = onUserPin;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pin_verification_dialog;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
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
            verifyCode();
        });
    }


    /**
     * method will init the text watcher
     */
    private void addTextWatcher() {
        for (int id : codeInputIds) {
            EditText editText = binding.getRoot().findViewById(id);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0 && editText.getId() != binding.digitFour.getId()) {
                        // check if input is not empty, then jump to next
                        int next = Arrays.asList(codeInputIds).indexOf(editText.getId()) + 1;
                        binding.getRoot().findViewById(codeInputIds[next]).requestFocus();
                    }


                    if (editText.getId() == binding.digitFour.getId()) {
                        if (s.length() > 0) {
                            Constants.hideKeyboard(getBaseActivity());
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
            binding.getRoot().findViewById(id)
                    .setOnKeyListener((v, actionId, event) -> {

                        if (event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_DEL
                                && ((EditText) v).length() == 0
                                && v.getId() != binding.digitOne.getId()) {
                            // check if input is empty, then jump to previous
                            int prev = Arrays.asList(codeInputIds).indexOf(v.getId()) - 1;
                            EditText prevView = binding.getRoot().findViewById(codeInputIds[prev]);
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
     * getting the code
     */
    private String getCode() {
        return binding.digitOne.getText().toString() + binding.digitTwo.getText().toString() + binding.digitThree.getText().toString() +
                binding.digitFour.getText().toString();
    }

    /**
     * method will verify digits
     */
    void verifyCode() {
        String userCode = getCode();
        if (userCode.length() < 4) {
            onMessage(getString(R.string.askfordigit));
        } else {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                MatchPinRequest request = new MatchPinRequest();
                request.customerPIN = getCode();
                request.customerNO = getSessionManager().getCustomerNo();
                request.languageID = getSessionManager().getlanguageselection();

                MatchPinTask task = new MatchPinTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        }
    }

    @Override
    public void onVerifiedPin() {
        onUserPin.onVerifiedPin();
        cancelUpload();
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}


