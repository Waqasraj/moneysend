package moneylink.wallet.login_signup_module.fragments_sign_up;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;


import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.databinding.CompleteProfileNonbBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.login_signup_module.MainActivityLoginSignUp;

public class NewSignupCreateProfile extends BaseFragment<CompleteProfileNonbBinding> {


    String gender = "m";


    @Override
    public void onResume() {
        super.onResume();

        if (!((MainActivityLoginSignUp) getBaseActivity())
                .viewModel.registerUserRequest.email.isEmpty()) {
            binding.emailsignup.setText(((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.email);
            binding.emailsignup.setEnabled(false);
        }
    }

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.firstName.getText().toString())) {
            onMessage(getString(R.string.enter_first_name));
            return false;
        }  else if (TextUtils.isEmpty(binding.lastName.getText().toString())) {
            onMessage(getString(R.string.enter_last_name));
            return false;
        }  else if (TextUtils.isEmpty(binding.emailsignup.getText().toString())) {
            onMessage(getString(R.string.enter_email_error));
            return false;
        }
//        else if (TextUtils.isEmpty(gender)) {
//            onMessage(getString(R.string.select_gender));
//            return false;
//        }
        else if (binding.firstName.getText().length() < 3) {
            onMessage(getString(R.string.first_name_greate_than_3));
            return false;
        } else if (binding.lastName.getText().length() < 3) {
            onMessage(getString(R.string.last_name_greate_than_3));
            return false;
        }
//        else if (CheckValidation.isValidName(binding.firstName.getText().toString())) {
//            onMessage(getString(R.string.first_name_special_character_not_allowed));
//            return false;
//        } else if (CheckValidation.isValidName(binding.mnamesignup.getText().toString())) {
//            onMessage(getString(R.string.middle_name_spe_char));
//            return false;
//        } else if (CheckValidation.isValidName(binding.lastName.getText().toString())) {
//            onMessage(getString(R.string.last_name_spe_char));
//            return false;
//        }
        else if (!CheckValidation.isEmailValid(binding.emailsignup.getText().toString())) {
            onMessage(getString(R.string.invalid_email_address_txt));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextcreate1.setOnClickListener(v -> {
            if (isValidate()) {
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.firstName = binding.firstName.getText().toString();
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.lastName = binding.lastName.getText().toString();
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.middleName = "--";
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.email = binding.emailsignup.getText().toString();

                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.gender = gender;
                Navigation.findNavController(v)
                        .navigate(R.id.action_newSignupCreateProfile_to_fragmentSignupCompleteProfile);
            }
        });


        binding.radiomf.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            gender = radioButton.getText().toString().substring(0, 1);
        });

    }

    public void showCardDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Alert")
                .setMessage("Credit Card is not allowed for testing.")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.complete_profile_nonb;
    }
}
