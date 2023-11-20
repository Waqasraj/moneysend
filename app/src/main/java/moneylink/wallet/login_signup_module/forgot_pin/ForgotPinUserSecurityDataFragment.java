package moneylink.wallet.login_signup_module.forgot_pin;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.databinding.ForgotPinUserSecurityDataBinding;
import moneylink.wallet.di.RequestHelper.ForgotPinRequestApprovedUserRequest;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.utils.DateAndTime;
import moneylink.wallet.utils.StringHelper;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class ForgotPinUserSecurityDataFragment extends BaseFragment<ForgotPinUserSecurityDataBinding>
        implements DatePickerDialog.OnDateSetListener {

    ForgotPinRequestApprovedUserRequest request;
    public boolean isLoginWithNumber = false;
    public String userNumberEmail = "";

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.idNumber.getText().toString())) {
            onMessage(getString(R.string.plz_enter_id_number));
            return false;
        } else if (TextUtils.isEmpty(binding.idExpireDate.getText().toString())) {
            onMessage(getString(R.string.plz_enter_id_expire_date));
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.forgot_pin_user_security_data;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        request = new ForgotPinRequestApprovedUserRequest();

        if (getArguments() != null) {
            isLoginWithNumber = getArguments().getBoolean("is_with_number");
            userNumberEmail = getArguments().getString("user_number");
        }

        binding.idExpireDate.setOnClickListener(v -> {
            showPickerDialog();
        });


        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("is_with_number", isLoginWithNumber);
                ForgotPinRequestApprovedUserRequest request = new ForgotPinRequestApprovedUserRequest();
                if (isLoginWithNumber) {
                    request.mobileNumber = StringHelper.parseNumber(userNumberEmail);
                    request.emailAddress = "";

                } else {
                    request.mobileNumber = "";
                    request.emailAddress = userNumberEmail;
                }
                if (isLoginWithNumber) {
                    bundle1.putString("user_number", userNumberEmail);
                } else {
                    bundle1.putString("user_number", userNumberEmail);
                }
                request.languageId = getSessionManager().getlanguageselection();
                request.idExpireDate = binding.idExpireDate.getText().toString();
                request.idNumber = binding.idNumber.getText().toString();
                bundle1.putParcelable("object_forgot", request);
                Navigation.findNavController(v)
                        .navigate(R.id.action_forgotPinUserSecurityDataFragment_to_forgotPinOTPFragment
                                , bundle1);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        request.idExpireDate = DateAndTime.getForgotPinFormat(year, monthOfYear, dayOfMonth);
        binding.idExpireDate.setText(request.idExpireDate);
    }


    /**
     * dialog code for show date picker
     */
    private void showPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setAccentColor(Color.parseColor("#115d89"));
        datePickerDialog.setMinDate(calendar);
        //datePickerDialog.setMaxDate(calendar);
        datePickerDialog.setTitle(getActivity().getResources().getString(R.string.select_date_txt));
        datePickerDialog.show(getParentFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }

}
