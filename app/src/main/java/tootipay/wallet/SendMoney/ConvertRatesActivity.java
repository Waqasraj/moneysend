package tootipay.wallet.SendMoney;

import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.List;

import tootipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import tootipay.wallet.databinding.ActivityTransferMoneyBinding;
import tootipay.wallet.R;
import tootipay.wallet.di.RequestHelper.CalTransferRequest;
import tootipay.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import tootipay.wallet.di.ResponseHelper.CalTransferResponse;
import tootipay.wallet.di.ResponseHelper.GetCountryListResponse;
import tootipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import tootipay.wallet.di.apicaller.CheckRatesTask;
import tootipay.wallet.di.apicaller.GetWalletCurrencyListTask;
import tootipay.wallet.dialogs.DialogCountry;
import tootipay.wallet.dialogs.DialogCurrency;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.home_module.ClassChangerHelper;
import tootipay.wallet.home_module.NewDashboardActivity;
import tootipay.wallet.home_module.fragments.HomeFragment;
import tootipay.wallet.interfaces.OnGetTransferRates;
import tootipay.wallet.interfaces.OnSelectCountry;
import tootipay.wallet.interfaces.OnSelectCurrency;
import tootipay.wallet.utils.CountryParser;
import tootipay.wallet.utils.IsNetworkConnection;
import tootipay.wallet.utils.MoneyValueFilter;
import tootipay.wallet.utils.NumberFormatter;

import static tootipay.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class ConvertRatesActivity extends BaseFragment<ActivityTransferMoneyBinding>
        implements OnSelectCountry, OnSelectCurrency, OnGetTransferRates {

    CalTransferRequest calTransferRequest; // variable to store the calTransfer
    String paymentMode = "bank"; // default bank
    boolean isSending = false;
    boolean isRatesShowing = false;

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


    @Override
    public void onResume() {
        super.onResume();
        ((NewDashboardActivity) getBaseActivity())
                .hideHumBurger(ClassChangerHelper.SAVE_BANK);
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.toolBar.crossBtn.setVisibility(View.GONE);

        binding.toolBar.backBtn.setOnClickListener(v -> {
            ((NewDashboardActivity) getBaseActivity())
                    .moveToFragment(new HomeFragment());
        });

        binding.toolBar.titleTxt.setText(getString(R.string.best_rate));
        binding.toolBar.crossBtn.setVisibility(View.GONE);

        calTransferRequest = new CalTransferRequest();
        calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.languageId = getSessionManager().getlanguageselection();

        binding.convertNow.setOnClickListener(v -> {
            if (isValidate()) {
                if(TextUtils.isEmpty(binding.sendingAmountField.getText().toString())
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

                if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
                    CheckRatesTask task = new CheckRatesTask(getBaseActivity(), this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });

        binding.receivngCurrencyLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
                isSending = false;
                DialogCountry dialogCountry = new DialogCountry(CountryParser.RECEIVE, this
                        , true);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                dialogCountry.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });

        binding.transferNow.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseActivity(), MoneyTransferMainLayout.class);
            startActivity(intent);
        });

        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            isSending = true;
            if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
                DialogCountry dialogCountry = new DialogCountry(CountryParser.SEND, this
                        , true);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                dialogCountry.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
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
                    binding.afterConvertRatesLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(binding.payOutAmount.getText().toString()) &&
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
                    if(!TextUtils.isEmpty(binding.sendingAmountField.getText().toString())
                     && !isRatesShowing) {
                        binding.sendingAmountField.setText("");
                    }
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfer_money;
    }


    public void convertRatesRequest() {
        calTransferRequest.PaymentMode = paymentMode;
        calTransferRequest.TransferAmount = 1d;
        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
            CheckRatesTask task = new CheckRatesTask(getBaseActivity(), this);
            task.execute(calTransferRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
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
    public void onSelectCountry(GetCountryListResponse country) {
        if (isSending) {
            binding.sendingCurrency.setText(country.currencyShortName);
            calTransferRequest.PayInCurrency = country.currencyShortName;
          //  calTransferRequest.TransferCurrency = country.currencyShortName;
            setSendingCurrencyImage(country.imageURL);
        } else {
            binding.receivingCurrency.setText(country.currencyShortName);
            calTransferRequest.PayoutCurrency = country.currencyShortName;
            setReceivingCurrencyImage(country.imageURL);
        }

//        if (!TextUtils.isEmpty(binding.sendingCurrency.getText().toString())
//                && !TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
//            if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
//                binding.sendingAmountField.setText("1");
//                convertRatesRequest();
//            }
//            binding.sendingAmountField.requestFocus();
//        }
        binding.payOutAmount.setText("");
        binding.sendingAmountField.setText("");
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            if (isSending) {
                binding.sendingCurrency.setText(response.get(0).currencyShortName);
                calTransferRequest.PayInCurrency = response.get(0).currencyShortName;
              //  calTransferRequest.TransferCurrency = response.get(0).currencyShortName;
                setSendingCurrencyImage(response.get(0).image_URL);
            } else {
                binding.receivingCurrency.setText(response.get(0).currencyShortName);
                calTransferRequest.PayoutCurrency = response.get(0).currencyShortName;
                setReceivingCurrencyImage(response.get(0).image_URL);
            }

//            if (!TextUtils.isEmpty(binding.sendingCurrency.getText().toString())
//                    && !TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
//                if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
//                    binding.sendingAmountField.setText("1");
//                    convertRatesRequest();
//                }
//                binding.sendingAmountField.requestFocus();
//            }
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }

        binding.payOutAmount.setText("");
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if (isSending) {
            binding.sendingCurrency.setText(response.currencyShortName);
            setSendingCurrencyImage(response.image_URL);
            calTransferRequest.PayInCurrency = response.currencyShortName;
        } else {
            binding.receivingCurrency.setText(response.currencyShortName);
            calTransferRequest.PayoutCurrency = response.currencyShortName;
            setReceivingCurrencyImage(response.image_URL);
        }

        binding.convertNow.setVisibility(View.VISIBLE);

//        if (!TextUtils.isEmpty(binding.sendingCurrency.getText().toString())
//                && !TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
//            if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
//                binding.sendingAmountField.setText("1");
//                convertRatesRequest();
//            }
//            binding.sendingAmountField.requestFocus();
//        }
        binding.payOutAmount.setText("");
        binding.sendingAmountField.setText("");
    }

    public void showRates(CalTransferResponse response) {
        isRatesShowing = true;

        if(calTransferRequest.PayInCurrency.equalsIgnoreCase(calTransferRequest.TransferCurrency)) {
            binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
        } else if(calTransferRequest.PayoutCurrency.equalsIgnoreCase(calTransferRequest.TransferCurrency)) {
            binding.sendingAmountField.setText(NumberFormatter.decimal(response.payInAmount.floatValue()));
        }



        binding.sendingAmountTxt.setText(NumberFormatter.decimal(
                response.payInAmount.floatValue()));
        binding.totalPayableAmount.setText(NumberFormatter.decimal(
                response.totalPayable.floatValue()));
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        isRatesShowing = false;
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
