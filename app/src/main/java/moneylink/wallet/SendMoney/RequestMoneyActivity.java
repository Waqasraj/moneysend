package moneylink.wallet.SendMoney;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import moneylink.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivityRequestMoneyBinding;
import moneylink.wallet.di.RequestHelper.GetCCYForWalletRequest;
import moneylink.wallet.di.RequestHelper.RequestMoney;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.di.apicaller.GetCCYForWalletTask;
import moneylink.wallet.di.apicaller.RequestMoneyTask;
import moneylink.wallet.dialogs.AlertDialog;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.dialogs.DialogCurrency;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.MoneyValueFilter;
import moneylink.wallet.utils.NumberFormatter;
import moneylink.wallet.utils.StringHelper;
import moneylink.wallet.utils.WalletTypeHelper;

import static moneylink.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class RequestMoneyActivity extends BaseFragment<ActivityRequestMoneyBinding>
        implements OnSelectCountry, OnSuccessMessage, OnDecisionMade, OnSelectCurrency {

    RequestMoney request;

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_code));
            return false;
        } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        } else if (!CheckValidation.isValidPhone(binding.numberLayout.countryCodeTextView.getText().toString()
                + binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        } else if (TextUtils.isEmpty(binding.requestCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_requesting_cur));
            return false;
        } else if(TextUtils.equals(StringHelper.parseNumber(binding.numberLayout.countryCodeTextView.getText().toString()
                + binding.numberLayout.mobilesignupb.getText().toString()) , getSessionManager().getCustomerPhone())) {
            onMessage("Cannot Perform Request Money For Self");
            return false;
        }
        return true;
    }


    public boolean isNumberValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_code));
            return false;
        } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        } else if (!CheckValidation.isValidPhone(binding.numberLayout.countryCodeTextView.getText().toString()
                + binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        }
        return true;
    }


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        request = new RequestMoney();
        binding.numberLayout.countrySpinnerSignIn.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry dialogCountry = new DialogCountry(this::onSelectCountry);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                dialogCountry.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.numberLayout.mobilesignupb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.requestCurrency.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                int cursorPosition = binding.sendingAmountField.getSelectionEnd();
                String originalStr = binding.sendingAmountField.getText().toString();

                //To restrict only two digits after decimal place
                binding.sendingAmountField.setFilters(new InputFilter[]{new MoneyValueFilter(2)});

                try {
                    binding.sendingAmountField.removeTextChangedListener(this);
                    String value = binding.sendingAmountField.getText().toString();

                    if (value != null && !value.equals("")) {
                        if (value.startsWith(".")) {
                            binding.sendingAmountField.setText("0.");
                        }
                        if (value.startsWith("0") && !value.startsWith("0.")) {
                            binding.sendingAmountField.setText("");
                        }
                        String str = binding.sendingAmountField.getText().toString().replaceAll(",", "");
                        if (!value.equals(""))
                            binding.sendingAmountField.setText(getDecimalFormattedString(str));

                        int diff = binding.sendingAmountField.getText().toString().length() - originalStr.length();
                        binding.sendingAmountField.setSelection(cursorPosition + diff);

                    }
                    binding.sendingAmountField.addTextChangedListener(this);
                } catch (Exception ex) {
                    Log.e("Textwatcher", ex.getMessage());
                    binding.sendingAmountField.addTextChangedListener(this);
                }
            }
        });

        binding.sendNowBtn.setOnClickListener(v -> {
            if (isValidate()) {
                String userNumber = StringHelper.parseNumber(binding.numberLayout.countryCodeTextView.getText().toString()
                        + binding.numberLayout.mobilesignupb.getText().toString());

                request.mobileNo = userNumber;
                request.customerNo = ((MoneyTransferMainLayout) getBaseActivity()).sessionManager.getCustomerNo();
                request.description = binding.description.getText().toString();
                request.amount = Double.parseDouble(NumberFormatter.removeCommas(binding.sendingAmountField.
                        getText().toString()));
                request.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    RequestMoneyTask task = new RequestMoneyTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });


        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
//                GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
//                request.languageId = getSessionManager().getlanguageselection();
//                GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
//                        , this);
//                getWalletCurrencyListTask.execute(request);
                if (isNumberValidate()) {
                    GetCCYForWalletRequest request = new GetCCYForWalletRequest();
                    request.actionType = WalletTypeHelper.RECEIVE;
                    request.customerNo = getSessionManager().getCustomerNo();
                    request.languageID = getSessionManager().getlanguageselection();


                    GetCCYForWalletTask task = new GetCCYForWalletTask(getContext(), this);
                    task.execute(request);
                }

            } else {
                onMessage(getString(R.string.no_internet));
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_request_money;
    }


    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.requestCurrency.setText("");
        binding.numberLayout.countryCodeTextView.setText(country.countryCode);
        setCountryFlag(country.imageURL);
    }

    @Override
    public void onSuccess(String s) {
        AlertDialog alertDialog = new AlertDialog(getString(R.string.request_money)
                , getString(R.string.request_money_message), this);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        alertDialog.show(fragmentTransaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {
        getBaseActivity().finish();
    }

    public void setCountryFlag(String url) {
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

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        setSendingCurrencyImage(response.image_URL);
        binding.requestCurrency.setText(response.currencyShortName);
        request.currency = response.currencyShortName;
    }

    public void setSendingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.requestCurrencyImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
