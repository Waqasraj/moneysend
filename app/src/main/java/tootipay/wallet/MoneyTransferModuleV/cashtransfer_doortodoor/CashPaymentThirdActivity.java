package tootipay.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import tootipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import tootipay.wallet.databinding.ActivityCashPaymentThirdBinding;
import tootipay.wallet.di.RequestHelper.CalTransferRequest;
import tootipay.wallet.di.RequestHelper.CountryPayOutCurrencyRequest;
import tootipay.wallet.di.RequestHelper.GetSendRecCurrencyRequest;
import tootipay.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import tootipay.wallet.di.RequestHelper.TootiPayRequest;
import tootipay.wallet.di.ResponseHelper.CalTransferResponse;
import tootipay.wallet.di.ResponseHelper.GetCountryListResponse;
import tootipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import tootipay.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;
import tootipay.wallet.di.ResponseHelper.PurposeOfTransferListResponse;
import tootipay.wallet.di.apicaller.CheckRatesTask;
import tootipay.wallet.di.apicaller.CountryPayoutCurrencyTask;
import tootipay.wallet.di.apicaller.GetSendRecCurrencyTask;
import tootipay.wallet.di.apicaller.GetWalletCurrencyListTask;
import tootipay.wallet.dialogs.DialogCurrency;
import tootipay.wallet.dialogs.DialogSourceOfIncome;
import tootipay.wallet.dialogs.DialogTransferPurpose;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnGetTransferRates;
import tootipay.wallet.interfaces.OnSelectCountry;
import tootipay.wallet.interfaces.OnSelectCurrency;
import tootipay.wallet.interfaces.OnSelectSourceOfIncome;
import tootipay.wallet.interfaces.OnSelectTransferPurpose;
import tootipay.wallet.utils.IsNetworkConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import tootipay.wallet.R;

import java.util.List;

import tootipay.wallet.utils.MoneyValueFilter;
import tootipay.wallet.utils.NumberFormatter;

import static tootipay.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class CashPaymentThirdActivity extends BaseFragment<ActivityCashPaymentThirdBinding>
        implements OnSelectTransferPurpose, OnSelectCountry, OnSelectCurrency, OnGetTransferRates
        , OnSelectSourceOfIncome {


    CalTransferRequest calTransferRequest; // variable to store the calTransfer
    Integer soucreOfIncome;
    String paymentMode = "cash"; // default bank
    boolean isTootiPay = false;
    TootiPayRequest tootiPayRequest;
    boolean showingBreakPoint = false;
    boolean isSendingCurrencySelected = false;
    boolean isRatesShowing = false;

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_sending_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_receiving_currency));
            return false;
        }
        return true;
    }


    public boolean isSendValidate() {
        if (TextUtils.isEmpty(binding.sourceOfIncome.getText())) {
            onMessage(getString(R.string.plz_select_source_of_income_error));
            return false;
        } else if (TextUtils.isEmpty(binding.purposeOfTransfer.getText())) {
            onMessage(getString(R.string.plz_select_send_purpose_error));
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.cash_transfer));
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        tootiPayRequest = new TootiPayRequest();
        calTransferRequest = new CalTransferRequest();
        binding.receivingCurrency.setText(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.payOutCurrency);

        setReceivingCurrencyImage(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.imageURL);
        calTransferRequest.PayoutCurrency = binding.receivingCurrency.getText().toString(); // set receiving currency
        calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();

        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        calTransferRequest.payInCountry = getSessionManager().getResidenceCountry();
        calTransferRequest.payOutCountry = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.payOutCountryCode;


        binding.sendingAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.afterConvertRatesLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
                    binding.commissionLayout.setVisibility(View.GONE);
                    binding.sendingAmountLayout.setVisibility(View.GONE);
                    binding.viewPriceBreakDown.setText(getString(R.string.view_price_break_down));
                    if (!TextUtils.isEmpty(binding.payOutAmount.getText().toString()) &&
                            !isRatesShowing) {
                        binding.payOutAmount.setText("");
                    }

                }
            }
        });

        binding.payOutAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.afterConvertRatesLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
                    binding.commissionLayout.setVisibility(View.GONE);
                    binding.sendingAmountLayout.setVisibility(View.GONE);
                    binding.viewPriceBreakDown.setText(getString(R.string.view_price_break_down));
                    if (!TextUtils.isEmpty(binding.sendingAmountField.getText().toString())
                            && !isRatesShowing) {
                        binding.sendingAmountField.setText("");
                    }
                }
            }
        });

        binding.viewPriceBreakDown.setOnClickListener(v -> {
            if (!showingBreakPoint) {
                showingBreakPoint = true;
                binding.commissionLayout.setVisibility(View.VISIBLE);
                binding.sendingAmountLayout.setVisibility(View.VISIBLE);
                binding.viewPriceBreakDown.setText(getString(R.string.hide_break_down));
            } else {
                showingBreakPoint = false;
                binding.commissionLayout.setVisibility(View.GONE);
                binding.sendingAmountLayout.setVisibility(View.GONE);
                binding.viewPriceBreakDown.setText(getString(R.string.view_price_break_down));
            }

        });

        binding.sendNowLayout.setOnClickListener(view -> {
            if (isSendValidate()) {
                if (((MoneyTransferMainLayout) getBaseActivity()).sessionManager.getISKYCApproved()) {
                    sendAmount();
                } else {
                    onMessage(getString(R.string.plz_complete_kyc));
                }
            }
        });

        binding.convertNow.setOnClickListener(v -> {
            if (isValidate()) {
                if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())
                        && TextUtils.isEmpty(binding.payOutAmount.getText().toString())) {
                    onMessage(getString(R.string.plz_enter_amount));
                    return;
                } else if (!TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
                    calTransferRequest.TransferCurrency = calTransferRequest.PayInCurrency;
                    calTransferRequest.TransferAmount = Double.parseDouble(NumberFormatter.removeCommas(
                            binding.sendingAmountField.getText().toString()));
                } else if (!TextUtils.isEmpty(binding.payOutAmount.getText().toString())) {
                    calTransferRequest.TransferCurrency = calTransferRequest.PayoutCurrency;
                    calTransferRequest.TransferAmount = Double.parseDouble(NumberFormatter.removeCommas(
                            binding.payOutAmount.getText().toString()));
                }
                calTransferRequest.PaymentMode = paymentMode;

                if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                    CheckRatesTask task = new CheckRatesTask(getActivity(), this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }

        });


        binding.purposeOfTransfer.setOnClickListener(v -> showTransferDialog());

        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                isSendingCurrencySelected = true;
                GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                request.languageId = getSessionManager().getlanguageselection();
                GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
                        , this);
                getWalletCurrencyListTask.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });



        binding.receivngCurrencyLayout.setOnClickListener( v-> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                isSendingCurrencySelected = false;
                CountryPayOutCurrencyRequest request = new CountryPayOutCurrencyRequest();
                request.languageId = getSessionManager().getlanguageselection();
                request.countryShortName = calTransferRequest.payOutCountry;

                CountryPayoutCurrencyTask task = new CountryPayoutCurrencyTask(getContext()
                 , this);
                task.execute(request);

            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.sourceOfIncome.setOnClickListener(v -> {
            DialogSourceOfIncome dialogSourceOfIncome = new DialogSourceOfIncome(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogSourceOfIncome.show(transaction, "");
        });


    }

    public void sendAmount() {
        if (TextUtils.isEmpty(binding.purposeOfTransfer.getText().toString())) {
            onMessage(getString(R.string.plz_select_send_purpose_error));
        } else if (TextUtils.isEmpty(binding.sourceOfIncome.getText().toString())) {
            onMessage(getString(R.string.plz_select_source_of_income_error));
        } else {
            if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                tootiPayRequest.customerNo = ((MoneyTransferMainLayout) getBaseActivity())
                        .sessionManager.getCustomerNo();
                tootiPayRequest.beneficiaryNo = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                        .beneficiaryDetails.beneficiaryNo;
                tootiPayRequest.payOutCurrency = calTransferRequest.PayoutCurrency;
                tootiPayRequest.payInCurrency = binding.sendingCurrency.getText().toString();
                tootiPayRequest.transferAmount = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.payOutAmount.getText().toString()));
                tootiPayRequest.sendingAmounttomatch = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.sendingAmountField.getText().toString()));
                tootiPayRequest.sourceOfIncome = soucreOfIncome;
                tootiPayRequest.languageId = getSessionManager().getlanguageselection();
                isTootiPay = true;

                ((MoneyTransferMainLayout) getBaseActivity()).
                        bankTransferViewModel.request = tootiPayRequest;


                Bundle bundle = new Bundle();
                bundle.putString("total_payable", binding.totalPayableAmount.getText().toString());

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_cashPaymentThirdActivity_to_cashTransferSummaryFragment
                                , bundle);

            } else {
                onMessage(getString(R.string.no_internet));
            }
        }
    }

    public void showTransferDialog() {
        DialogTransferPurpose dialogTransferPurpose =
                new DialogTransferPurpose(this);
        FragmentTransaction fm = getParentFragmentManager().beginTransaction();
        dialogTransferPurpose.show(fm, "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_payment_third;
    }

    @Override
    public void onSelectTransferPurpose(PurposeOfTransferListResponse response) {
        binding.purposeOfTransfer.setText(response.purposeOfTransfer);
        tootiPayRequest.purposeOfTransfer = response.purposeOfTransferID;
    }


    public void showRates(CalTransferResponse response) {

        isRatesShowing = true;

        if(calTransferRequest.PayInCurrency.equalsIgnoreCase(calTransferRequest.PayoutCurrency)) {
            if (!TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
                binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
            } else if (!TextUtils.isEmpty(binding.payOutAmount.getText().toString())) {
                binding.sendingAmountField.setText(NumberFormatter.decimal(response.payInAmount.floatValue()));
            }
        }else if (calTransferRequest.PayInCurrency.equalsIgnoreCase(calTransferRequest.TransferCurrency)) {
            binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
        } else if (calTransferRequest.PayoutCurrency.equalsIgnoreCase(calTransferRequest.TransferCurrency)) {
            binding.sendingAmountField.setText(NumberFormatter.decimal(response.payInAmount.floatValue()));
        }
//
//        binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
       binding.sendingAmountTxt.setText(NumberFormatter.decimal(
                response.payInAmount.floatValue()));
        binding.totalPayableAmount.setText(String.valueOf(
                NumberFormatter.decimal(response.totalPayable.floatValue())));
        binding.commissionAmountTxt.setText(String.valueOf(
                NumberFormatter.decimal(response.commission.floatValue())));
        binding.afterConvertRatesLayout.setVisibility(View.VISIBLE);
        binding.convertNow.setVisibility(View.GONE);
        isRatesShowing = false;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        GetSendRecCurrencyRequest request = new GetSendRecCurrencyRequest();
        request.countryType = country.countryType;
        request.countryShortName = country.countryShortName;
        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
//        if (response.size() == 1) {
//            binding.sendingCurrency.setText(response.get(0).currencyShortName);
//            calTransferRequest.PayInCurrency = response.get(0).currencyShortName;
//            calTransferRequest.TransferCurrency = response.get(0).currencyShortName;
//            binding.payOutAmount.setText("");
//            setSendingCurrencyImage(response.get(0).image_URL);
//            binding.afterConvertRatesLayout.setVisibility(View.GONE);
//            binding.convertNow.setVisibility(View.VISIBLE);
//        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
       // }
    }


    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if(isSendingCurrencySelected) {
            setSendingCurrencyImage(response.image_URL);
            binding.sendingCurrency.setText(response.currencyShortName);
            calTransferRequest.PayInCurrency = response.currencyShortName;
            calTransferRequest.TransferCurrency = response.currencyShortName;
        } else {
            setReceivingCurrencyImage(response.image_URL);
            binding.receivingCurrency.setText(response.currencyShortName);
            calTransferRequest.PayoutCurrency = response.currencyShortName;
        }


        binding.convertNow.setVisibility(View.VISIBLE);
        binding.payOutAmount.setText("");
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetTransferRates(CalTransferResponse response) {
        showRates(response);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    @Override
    public void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response) {
        binding.sourceOfIncome.setText(response.incomeName);
        soucreOfIncome = response.id;
    }


    public void setSendingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.sendingCurrencyImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void setReceivingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.receivingCurrencyImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

}
