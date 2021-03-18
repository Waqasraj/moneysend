package tootipay.wallet;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityRepeatWalletTransactionBinding;
import tootipay.wallet.di.RequestHelper.CalTransferRequest;
import tootipay.wallet.di.RequestHelper.GetCCYForWalletRequest;
import tootipay.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import tootipay.wallet.di.RequestHelper.WalletToWalletTransferRequest;
import tootipay.wallet.di.ResponseHelper.CalTransferResponse;
import tootipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import tootipay.wallet.di.apicaller.CheckRatesTask;
import tootipay.wallet.di.apicaller.GetCCYForWalletTask;
import tootipay.wallet.di.apicaller.GetWalletCurrencyListTask;
import tootipay.wallet.di.apicaller.WalletToWalletTransferTask;
import tootipay.wallet.dialogs.DialogCurrency;
import tootipay.wallet.dialogs.SingleButtonMessageDialog;
import tootipay.wallet.dialogs.WalletToWalletConfirmDialog;
import tootipay.wallet.interfaces.OnDecisionMade;
import tootipay.wallet.interfaces.OnGetTransferRates;
import tootipay.wallet.interfaces.OnResponse;
import tootipay.wallet.interfaces.OnSelectCurrency;
import tootipay.wallet.interfaces.OnWalletTransferConfirmation;
import tootipay.wallet.utils.IsNetworkConnection;
import tootipay.wallet.utils.MoneyValueFilter;
import tootipay.wallet.utils.NumberFormatter;
import tootipay.wallet.utils.WalletTypeHelper;

import static tootipay.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class RepeatWalletTransactionActivity extends TootiBaseActivity<ActivityRepeatWalletTransactionBinding>
        implements OnResponse, OnDecisionMade
        , OnWalletTransferConfirmation,
        OnSelectCurrency, OnGetTransferRates {

    WalletToWalletTransferRequest request;
    boolean isSendingCurrency = false;
    List<GetSendRecCurrencyResponse> walletCurrency;

    CalTransferRequest calTransferRequest;
    String paymentMode = "Wallet_Transfer";
    String customerNo;

    boolean isRatesShowing = false;


    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        } else if (TextUtils.isEmpty(binding.receiverName.getText().toString())) {
            onMessage(getString(R.string.recever_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        } else if (TextUtils.isEmpty(binding.description.getText().toString())) {
            onMessage(getString(R.string.plz_enter_description));
            return false;
        }
        return true;
    }

    public boolean isCalTransferValidate() {
        if (TextUtils.isEmpty(binding.sendingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_sending_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_receiving_currency));
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repeat_wallet_transaction;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        customerNo = getIntent().getStringExtra("customer_no");
        String receiverName = getIntent().getStringExtra("receiver_name");
        binding.countrySpinnerSignIn.setVisibility(View.GONE);
        //  binding.numberLayout.countrySpinnerSignIn.setVisibility(View.GONE);
        binding.mobilesignupb.setText(customerNo);
        binding.receiverName.setText(receiverName);

        binding.mobilesignupb.setEnabled(false);
        binding.receiverName.setEnabled(false);

        if (walletCurrency == null) {
            walletCurrency = new ArrayList<>();
        }

        binding.toolbar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolbar.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.toolbar.titleTxt.setText(getString(R.string.wallet_transfer));
        binding.toolbar.crossBtn.setVisibility(View.GONE);
        binding.toolbar.crossBtn.setOnClickListener(v -> {
            onClose();
        });

        request = new WalletToWalletTransferRequest();
        calTransferRequest = new CalTransferRequest();

        binding.convertNow.setOnClickListener(v -> {
            if (isCalTransferValidate()) {
                calTransferRequest.PayoutCurrency = binding.receivingCurrency.getText().toString(); // set receiving currency
                calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
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

                calTransferRequest.languageId = sessionManager.getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(this)) {
                    CheckRatesTask task = new CheckRatesTask(this, this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }
        });

        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            isSendingCurrency = true;
            //   if (walletCurrency.isEmpty()) {
            if (IsNetworkConnection.checkNetworkConnection(this)) {
//                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
//                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(this
//                            , this);
//                    getWalletCurrencyListTask.execute(request);

                showWallets(sessionManager.getCustomerNo());
            } else {
                onMessage(getString(R.string.no_internet));
            }
            // } else {
            //   showCurrencyDialog(walletCurrency);
            // }
        });

        binding.receivngCurrencyLayout.setOnClickListener(v -> {
            isSendingCurrency = false;
            //  if (walletCurrency.isEmpty()) {
            if (IsNetworkConnection.checkNetworkConnection(this)) {
//                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
//                    request.languageId = sessionManager.getlanguageselection();
//                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(this
//                            , this);
//                    getWalletCurrencyListTask.execute(request);

                showWallets(customerNo);
            } else {
                onMessage(getString(R.string.no_internet));
            }
            //   } else {
            //     showCurrencyDialog(walletCurrency);
            //}
        });


        binding.toolbar.backBtn.setOnClickListener(v -> {
            finish();
        });

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
                    binding.mainLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
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
                    binding.mainLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(binding.sendingAmountField.getText().toString())
                            && !isRatesShowing) {
                        binding.sendingAmountField.setText("");
                    }
                }
            }
        });

        binding.sendNowBtn.setOnClickListener(v -> {
            if (isValidate()) {
                if (sessionManager.getISKYCApproved()) {
                    request.receiptMobileNo = binding.mobilesignupb.getText().toString();
                    request.customerNo = sessionManager.getCustomerNo();
                    request.description = binding.description.getText().toString();
                    request.transferAmount = NumberFormatter.removeCommas(
                            binding.sendingAmountField.getText().toString());
                    request.receiptCurrency = binding.receivingCurrency.getText().toString();
                    request.payInCurrency = binding.sendingCurrency.getText().toString();
                    request.ipAddress = sessionManager.getIpAddress();
                    request.ipCountryName = sessionManager.getIpCountryName();

                    WalletToWalletConfirmDialog dialog = new WalletToWalletConfirmDialog(
                            request, binding.receiverName.getText().toString(), this
                    );
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    dialog.show(transaction, "");

                } else {
                    onMessage(getString(R.string.plz_complete_kyc));
                }
            }
        });
    }


    public void showWallets(String customerno) {
        GetCCYForWalletRequest request = new GetCCYForWalletRequest();
        request.actionType = WalletTypeHelper.RECEIVE;
        request.customerNo = customerno;
        request.languageID = sessionManager.getlanguageselection();


        GetCCYForWalletTask task = new GetCCYForWalletTask(this, this);
        task.execute(request);
    }


    void showCurrencyDialog(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
    }

    @Override
    public void onSuccess() {
        sessionManager.putWalletNeedToUpdate(true);
        SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(getString(R.string.successfully_tranfared)
                , getString(R.string.wallet_traansaction_success), this,
                false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onProceed() {
        finish();
    }

    @Override
    public void onCancel() {
        finish();
    }

    @Override
    public void onGetTransferRates(CalTransferResponse response) {
        showRates(response);
    }

    public void showRates(CalTransferResponse response) {
        isRatesShowing = true;

        if(calTransferRequest.PayInCurrency.equalsIgnoreCase(calTransferRequest.PayoutCurrency)) {
            if (!TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
                binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
            } else if (!TextUtils.isEmpty(binding.payOutAmount.getText().toString())) {
                binding.sendingAmountField.setText(NumberFormatter.decimal(response.payInAmount.floatValue()));
            }
        } else if (calTransferRequest.PayInCurrency.equalsIgnoreCase(calTransferRequest.TransferCurrency)) {
            binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
        } else if (calTransferRequest.PayoutCurrency.equalsIgnoreCase(calTransferRequest.TransferCurrency)) {
            binding.sendingAmountField.setText(NumberFormatter.decimal(response.payInAmount.floatValue()));
        }
        binding.commissionAmountTxt.setText(String.valueOf(
                NumberFormatter.decimal(response.commission.floatValue())));
        binding.mainLayout.setVisibility(View.VISIBLE);
        binding.convertNow.setVisibility(View.GONE);

        isRatesShowing = false;
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        //   if (response.size() == 1) {
        //     binding.sendingCurrency.setText(response.get(0).currencyShortName);
        //   setSendingCurrencyImage(response.get(0).image_URL);
        //} else {
        showCurrencyDialog(response);
        //}
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if (isSendingCurrency) {
            binding.sendingCurrency.setText(response.currencyShortName);
            binding.payOutAmount.setText("");
            setSendingCurrencyImage(response.image_URL);
        } else {
            binding.receivingCurrency.setText(response.currencyShortName);
            setReceivingCurrencyImage(response.image_URL);
            binding.payOutAmount.setText("");
        }

        binding.mainLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfirmed() {
        getPin();
    }

    @Override
    public void onVerifiedPin() {
        loadWallet();
    }

    public void loadWallet() {
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            request.languageId = sessionManager.getlanguageselection();
            WalletToWalletTransferTask task = new WalletToWalletTransferTask(this
                    , this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
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