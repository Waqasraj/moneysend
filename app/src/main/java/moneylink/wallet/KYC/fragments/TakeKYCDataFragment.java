package moneylink.wallet.KYC.fragments;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentTakeKycDataBinding;
import moneylink.wallet.di.RequestHelper.EditCustomerProfileRequest;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.ResponseHelper.GetIdTypeResponse;
import moneylink.wallet.di.apicaller.EditCustomerProfileTask;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.dialogs.DialogGetIdType;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnResponse;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.DateAndTime;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.interfaces.OnSelectIdType;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.StringHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class TakeKYCDataFragment extends BaseFragment<FragmentTakeKycDataBinding>
        implements OnSelectCountry, OnSelectIdType, DatePickerDialog.OnDateSetListener, OnResponse {

    boolean isCountrySelected;
    boolean isIssueDateSelect = false;
    EditCustomerProfileRequest request;

    String idType = "";
    boolean isDateOfBirth = false;
    boolean isNumberEmpty = false;
    boolean isNumberCode = false;

    @Override
    protected void injectView() {

    }

    @Override
    public boolean isValidate() {
        if (isNumberEmpty) {
            if (TextUtils.isEmpty(binding.numberLayout.countryCodeTextView.getText().toString())) {
                onMessage(getString(R.string.plz_select_country_code));
                return false;
            } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
                onMessage(getString(R.string.enter_mobile_no_error));
                return false;
            }
            if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                    , binding.numberLayout.countryCodeTextView.getText().toString())) {
                onMessage(getString(R.string.invalid_number));
                return false;
            }
        }
        if (TextUtils.isEmpty(binding.issuanceCountry.getText())) {
            onMessage(getString(R.string.plz_select_is_country));
            return false;
        } else if (TextUtils.isEmpty(binding.idType.getText())) {
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
        } else if (!binding.switchAccount.isChecked()) {
            if (TextUtils.isEmpty(binding.dateOfBirth.getText().toString())) {
                onMessage(getString(R.string.enter_dob_error));
                return false;
            }
        }


        return true;
    }


    public void showIndividualAccount() {
        binding.issuanceCountryTitle.setText(getString(R.string.issuance_country));
        binding.idTypeTitle.setText(getString(R.string.id_type));
        binding.idNumberTitle.setText(getString(R.string.id_number_text));
        binding.idIssueDateTitle.setText(getString(R.string.id_issue_date));
        binding.idExpireDateTitle.setText(getString(R.string.id_expirey_date));
        binding.dateOfBirthTitle.setVisibility(View.VISIBLE);
        binding.dateOfBirth.setVisibility(View.VISIBLE);
        request.customer.dob = binding.dateOfBirth.getText().toString();
        binding.switchAccount.setText(getString(R.string.individual_reg));
    }


    public void showBusinessAccount() {
        binding.switchAccount.setText(getString(R.string.bussiness_regis));
        binding.issuanceCountryTitle.setText(getString(R.string.business_issue_country));
        binding.idTypeTitle.setText(getString(R.string.business_id_type));
        binding.idNumberTitle.setText(getString(R.string.business_id_number_text));
        binding.idIssueDateTitle.setText(getString(R.string.business_id_issue_date));
        binding.idExpireDateTitle.setText(getString(R.string.business_id_expirey_date));
        binding.dateOfBirthTitle.setVisibility(View.GONE);
        binding.dateOfBirth.setVisibility(View.GONE);
        request.customer.dob = "10/12/1900";
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.switchAccount.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showBusinessAccount();
            } else {
                showIndividualAccount();
            }
        });


        if (getSessionManager().getCustomerPhone().isEmpty()) {
            isNumberEmpty = true;
            binding.numberTxt.setVisibility(View.VISIBLE);
            binding.numberLayout.mainNumber.setVisibility(View.VISIBLE);
        }

        request = new EditCustomerProfileRequest();
        binding.nextLayout.setOnClickListener(v -> {

            if (isValidate()) {
                request.customer = getSessionManager().getCustomerDetails();

                if (!binding.switchAccount.isChecked()) {
                    request.customer.dob = binding.dateOfBirth.getText().toString();
                } else {
                    request.customer.dob = "10/12/1900";
                }

                if (getSessionManager().getCustomerPhone().isEmpty()) {
                    request.customer.phoneNumber = StringHelper.parseNumber(binding.numberLayout.countryCodeTextView.getText().toString()
                            + binding.numberLayout.mobilesignupb.getText()
                            .toString());
                } else {
                    request.customer.phoneNumber = getSessionManager().getCustomerPhone();
                }
                request.residenceCountry = getSessionManager().getResidenceCountry();
                request.idExpireDate = binding.idExpireDate.getText().toString();
                request.idIssueDate = binding.idIssueDate.getText().toString();
                request.idNumber = binding.idNumber.getText().toString();
                request.languageId = getSessionManager().getlanguageselection();
                request.customerNo = getSessionManager().getCustomerNo();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    EditCustomerProfileTask task = new EditCustomerProfileTask(getContext(), this);
                    task.execute(request);
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

        binding.dateOfBirth.setOnClickListener(v -> {
            isIssueDateSelect = false;
            isDateOfBirth = true;
            showPickerDialog(getString(R.string.select_date_txt));
        });

        binding.issuanceCountry.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry dialogCountry = new DialogCountry(this, false, false);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                dialogCountry.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });

        binding.idType.setOnClickListener(v -> {
            if (isCountrySelected) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    DialogGetIdType dialogGetIdType = new DialogGetIdType(idType, this);
                    FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                    dialogGetIdType.show(fm, "");
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage(getString(R.string.plz_select_country_error));
            }
        });

        binding.idIssueDate.setOnClickListener(v -> {
            isIssueDateSelect = true;
            isDateOfBirth = false;
            showPickerDialog(getString(R.string.select_date_txt));
        });

        binding.idExpireDate.setOnClickListener(v -> {
            isIssueDateSelect = false;
            isDateOfBirth = false;
            showPickerDialog(getString(R.string.select_date_txt));
        });

        binding.numberLayout.countrySpinnerSignIn.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                isNumberCode = true;
                DialogCountry country = new DialogCountry(this::onSelectCountry);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                country.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_take_kyc_data;
    }


    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        if (isNumberCode) {
            binding.numberLayout.countryCodeTextView.setText(country.countryCode);
            setSendingCurrencyImage(country.imageURL);
            isNumberCode = false;
            return;
        }

        idType = country.countryShortName;
        binding.issuanceCountry.setText(country.countryName);
        isCountrySelected = true;
        // request.residenceCountry = country.countryShortName;
    }


    void showType(GetIdTypeResponse response) {
        request.idType = response.id;
        binding.idType.setText(response.idTypeName);
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
        }

        if (!isIssueDateSelect && !isDateOfBirth) {
            datePickerDialog.setMinDate(calendar);
        }

        if (isDateOfBirth) {
            datePickerDialog.setMaxDate(calendar);
        }


        datePickerDialog.setTitle(title);
        datePickerDialog.show(getParentFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
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
        }

        if (!isDateOfBirth && !isIssueDateSelect) {
            //   request.idExpireDate = DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth);
            binding.idExpireDate.setText(DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth));
        }


        if (isDateOfBirth) {
            Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                onMessage(getString(R.string.must_be_18_year_old));
            } else {
                binding.dateOfBirth.setText(DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth));
                request.customer.dob = binding.dateOfBirth.getText().toString();
            }
        }
    }

    @Override
    public void onSuccess() {
        Navigation.findNavController(getView())
                .navigate(R.id.action_takeKYCDataFragment_to_takeKYCIdPictureFragment);
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