package moneylink.wallet.MoneyTransferModuleV;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentCheckIdRequirementBinding;
import moneylink.wallet.di.ResponseHelper.GetIdTypeResponse;
import moneylink.wallet.dialogs.DialogGetIdType;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnSelectIdType;
import moneylink.wallet.utils.DateAndTime;
import moneylink.wallet.utils.IsNetworkConnection;

public class CheckIdRequirementFragment extends BaseFragment<FragmentCheckIdRequirementBinding>
        implements OnSelectIdType, DatePickerDialog.OnDateSetListener {

    boolean isIssueDateSelect = false;
    boolean isCash = false;
    String totalPayable;

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.idType.getText())) {
            onMessage(getString(R.string.plz_select_id_type));
            return false;
        } else if (TextUtils.isEmpty(binding.idNumber.getText().toString())) {
            onMessage(getString(R.string.plz_enter_id_number));
            return false;
        } else if (TextUtils.isEmpty(binding.idIssueDate.getText().toString())) {
            onMessage(getString(R.string.plz_select_issue_date));
            return false;
        } else if (TextUtils.isEmpty(binding.idExpireDate.getText().toString())) {
            onMessage(getString(R.string.plz_select_expire_date));
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        assert getArguments() != null;
        totalPayable = getArguments().getString("total_payable");
        isCash = getArguments().getBoolean("is_from_cash", true);

        binding.idType.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogGetIdType dialogGetIdType = new DialogGetIdType("GBR", this);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                dialogGetIdType.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.idIssueDate.setOnClickListener(v -> {
            isIssueDateSelect = true;
            showPickerDialog(getString(R.string.select_date_txt));
        });

        binding.idExpireDate.setOnClickListener(v -> {
            isIssueDateSelect = false;
            showPickerDialog(getString(R.string.select_date_txt));
        });


        binding.loginBtn.setOnClickListener(v -> {
            if (isValidate()) {
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.request.remIdNumber = binding.idNumber.getText().toString();
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.request.remIDExpDate = binding.idExpireDate.getText().toString();
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.request.remIDIssueDate = binding.idIssueDate.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("total_payable", totalPayable);
                bundle.putBoolean("is_from_cash", isCash);

                if(isCash) {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.cashTransferSummaryFragment
                                    , bundle);
                } else {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.cashTransferSummaryFragment2
                                    , bundle);
                }


            }
        });
    }


    /**
     * dialog code for show date picker
     */
    private void showPickerDialog(String title) {
        Calendar calendar = Calendar.getInstance();


        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog
                .newInstance(this, Year, Month, Day);
//        if(isDateOfBirth) {
//            Year = calendar.get(Calendar.YEAR - 18);
//        }
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setAccentColor(Color.parseColor("#115d89"));
        datePickerDialog.setLocale(new Locale("en"));
        if (isIssueDateSelect) {
            datePickerDialog.setMaxDate(calendar);
        } else {
            datePickerDialog.setMinDate(calendar);
        }


        datePickerDialog.setTitle(title);
        datePickerDialog.show(getParentFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_check_id_requirement;
    }


    @Override
    public void onSelectIdType(GetIdTypeResponse response) {
        showType(response);
    }

    @Override
    public void onGetIdTypes(List<GetIdTypeResponse> responses) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (isIssueDateSelect) {
            // request.idIssueDate = DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth);
            binding.idIssueDate.setText(DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth));
        } else {
            //   request.idExpireDate = DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth);
            binding.idExpireDate.setText(DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth));
        }
    }


    void showType(GetIdTypeResponse response) {
        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.request.remIdType =
                String.valueOf(response.id);
        binding.idType.setText(response.idTypeName);
    }
}