package moneylink.wallet.MoneyTransferModuleV.banktransfer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import moneylink.wallet.databinding.ActivityCashPaymentThirdBinding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.CalTransferRequest;
import moneylink.wallet.di.RequestHelper.CheckIDRequiredRequest;
import moneylink.wallet.di.RequestHelper.GetCountryListRequest;
import moneylink.wallet.di.RequestHelper.GetSendRecCurrencyRequest;
import moneylink.wallet.di.RequestHelper.TootiPayRequest;
import moneylink.wallet.di.ResponseHelper.CalTransferResponse;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;
import moneylink.wallet.di.ResponseHelper.PurposeOfTransferListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.di.apicaller.CheckIDRequiredTask;
import moneylink.wallet.di.apicaller.CheckRatesTask;
import moneylink.wallet.di.apicaller.GetSendRecCurrencyTask;
import moneylink.wallet.dialogs.DialogCurrency;
import moneylink.wallet.dialogs.DialogSourceOfIncome;
import moneylink.wallet.dialogs.DialogTransferPurpose;
import moneylink.wallet.fragments.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.interfaces.IsIDRequired;
import moneylink.wallet.interfaces.OnGetTransferRates;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.interfaces.OnSelectSourceOfIncome;
import moneylink.wallet.interfaces.OnSelectTransferPurpose;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.MoneyValueFilter;
import moneylink.wallet.utils.NumberFormatter;
import moneylink.wallet.utils.ProgressBar;

import static moneylink.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class SendMoneyViaBankThirdActivity extends BaseFragment<ActivityCashPaymentThirdBinding>
        implements OnSelectTransferPurpose, OnSelectCountry, OnSelectCurrency, OnGetTransferRates
        , OnSelectSourceOfIncome, IsIDRequired {


    CalTransferRequest calTransferRequest; // variable to store the calTransfer
    Integer soucreOfIncome;
    String paymentMode = "bank"; // default bank
    boolean isTootiPay = false;
    TootiPayRequest tootiPayRequest;
    boolean showingBreakPoint = false;

    CheckIDRequiredRequest requiredRequest;
    List<GetCountryListResponse> countryListResponses;


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_sending_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
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
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.bank_transfer));
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        tootiPayRequest = new TootiPayRequest();
        calTransferRequest = new CalTransferRequest();
        requiredRequest = new CheckIDRequiredRequest();
        binding.receivingCurrency.setText(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.payOutCurrency);
        calTransferRequest.PayoutCurrency = binding.receivingCurrency.getText().toString(); // set receiving currency
        calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.TransferCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        setReceivingCurrencyImage(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryDetails.imageURL);
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
                calTransferRequest.PaymentMode = paymentMode;
                calTransferRequest.TransferAmount = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.sendingAmountField
                                .getText().toString()));

                if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                    CheckRatesTask task = new CheckRatesTask(getActivity(), this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }

        });
        binding.purposeOfTransfer.setOnClickListener(v -> showTransferDialog());

//        binding.sendingCurrencyLayout.setOnClickListener(v -> {
//            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
//                GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
//                request.languageId = getSessionManager().getlanguageselection();
//                GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
//                        , this);
//                getWalletCurrencyListTask.execute(request);
//            } else {
//                onMessage(getString(R.string.no_internet));
//            }
//        });


        binding.sourceOfIncome.setOnClickListener(v -> {
            DialogSourceOfIncome dialogSourceOfIncome = new DialogSourceOfIncome(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogSourceOfIncome.show(transaction, "");
        });


        binding.sendingAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.afterConvertRatesLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
                    binding.commissionLayout.setVisibility(View.GONE);
                    binding.sendingAmountLayout.setVisibility(View.GONE);
                    binding.viewPriceBreakDown.setText(getString(R.string.view_price_break_down));
                    binding.payOutAmount.setText("");
                }
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

    }


    public void goToNext(TootiPayRequest request) {
        ((MoneyTransferMainLayout) getBaseActivity()).
                bankTransferViewModel.request = request;

        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            requiredRequest.txnAmount = NumberFormatter.removeCommas(binding.sendingAmountField.getText().
                    toString());
            CheckIDRequiredTask task = new CheckIDRequiredTask(getActivity(), this);
            task.execute(requiredRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_payment_third;
    }


    public void sendAmount() {
        if (TextUtils.isEmpty(binding.purposeOfTransfer.getText().toString())) {
            onMessage(getString(R.string.plz_select_send_purpose_error));
        } else if (TextUtils.isEmpty(binding.sourceOfIncome.getText().toString())) {
            onMessage(getString(R.string.plz_select_source_of_income_error));
        } else if (!TextUtils.isEmpty(binding.purposeOfTransfer.getText().toString())
                && !TextUtils.isEmpty(binding.sourceOfIncome.getText().toString())) {
            if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                tootiPayRequest.customerNo = ((MoneyTransferMainLayout) getBaseActivity())
                        .sessionManager.getCustomerNo();
                tootiPayRequest.beneficiaryNo = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                        .beneficiaryDetails.beneficiaryNo;
                tootiPayRequest.payOutCurrency = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                        .beneficiaryDetails.payOutCurrency;
                tootiPayRequest.payInCurrency = binding.sendingCurrency.getText().toString();
                tootiPayRequest.transferAmount = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.payOutAmount.getText().toString()));
                tootiPayRequest.sendingAmounttomatch = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.sendingAmountField.getText().toString()));
                tootiPayRequest.sourceOfIncome = soucreOfIncome;
                tootiPayRequest.languageId = getSessionManager().getlanguageselection();
                isTootiPay = true;

                goToNext(tootiPayRequest);

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
    public void onSelectTransferPurpose(PurposeOfTransferListResponse response) {
        binding.purposeOfTransfer.setText(response.purposeOfTransfer);
        tootiPayRequest.purposeOfTransfer = response.purposeOfTransferID;
        Constants.hideKeyboard(getBaseActivity());
    }


    public void showRates(CalTransferResponse response) {
        binding.payOutAmount.setText(NumberFormatter.decimalSix(response.payoutAmount));
        binding.sendingAmountTxt.setText(NumberFormatter.decimalSix(
                response.payInAmount));
        binding.commissionAmountTxt.setText(NumberFormatter.decimalSix(
                response.commission));
        binding.totalPayableAmount.setText(NumberFormatter.decimalSix(
                response.totalPayable));
        binding.afterConvertRatesLayout.setVisibility(View.VISIBLE);
        binding.convertNow.setVisibility(View.GONE);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        GetSendRecCurrencyRequest request = new GetSendRecCurrencyRequest();
        request.countryType = country.countryType;
        request.countryShortName = country.countryShortName;
        request.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        binding.afterConvertRatesLayout.setVisibility(View.GONE);
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            binding.sendingCurrency.setText(response.get(0).currencyShortName);
            calTransferRequest.PayInCurrency = response.get(0).currencyShortName;
            calTransferRequest.TransferCurrency = response.get(0).currencyShortName;
            requiredRequest.currencyID = response.get(0).id;
            requiredRequest.countryID = response.get(0).currencyShortName;
            binding.payOutAmount.setText("");
            setSendingCurrencyImage(response.get(0).image_URL);
            binding.afterConvertRatesLayout.setVisibility(View.GONE);
            binding.convertNow.setVisibility(View.VISIBLE);
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }
    }


    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        binding.sendingCurrency.setText(response.currencyShortName);
        calTransferRequest.PayInCurrency = response.currencyShortName;
        calTransferRequest.TransferCurrency = response.currencyShortName;
        requiredRequest.currencyID = response.id;
        requiredRequest.countryID = response.currencyShortName;
        setSendingCurrencyImage(response.image_URL);
        binding.payOutAmount.setText("");
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);

        requiredRequest.currencyID = response.id;
        requiredRequest.countryID = response.currencyShortName;
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            new GetCountryListTask(getContext()).execute();
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
    public void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response) {
        binding.sourceOfIncome.setText(response.incomeName);
        soucreOfIncome = response.id;
        Constants.hideKeyboard(getBaseActivity());
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

    @Override
    public void isIDRequired(boolean isIdRequired) {
        if (isIdRequired) {
            Bundle bundle = new Bundle();
            bundle.putString("total_payable", binding.totalPayableAmount.getText().toString());
            bundle.putBoolean("is_from_cash", false);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.checkIdRequirementFragment , bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("total_payable", binding.totalPayableAmount.getText().toString());
            bundle.putBoolean("is_from_cash", false);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_sendMoneyViaBankThirdActivity_to_cashTransferSummaryFragment2
                            , bundle);
        }
    }


    public void getCountryId(List<GetCountryListResponse> getCountryListResponses) {
        for (GetCountryListResponse res :
                getCountryListResponses) {
            if (requiredRequest.countryID.equalsIgnoreCase(res.currencyShortName)) {
                requiredRequest.countryID = String.valueOf(res.id);
                break;
            }
        }
    }

    public class GetCountryListTask extends AsyncTask<Void, Void, List<GetCountryListResponse>> {


        ProgressBar progressBar;
        Context context;

        public GetCountryListTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressBar();
            progressBar.showProgressDialogWithTitle(context, context.getResources()
                    .getString(R.string.loading_txt));
        }

        @Override
        protected void onPostExecute(List<GetCountryListResponse> getCountryListResponses) {
            super.onPostExecute(getCountryListResponses);
            progressBar.hideProgressDialogWithTitle();
            if (getCountryListResponses != null) {
                getCountryId(getCountryListResponses);
            }
        }

        @Override
        protected List<GetCountryListResponse> doInBackground(Void... voids) {
            GetCountryListRequest request = new GetCountryListRequest();
            request.languageId = "1";
            Log.e("doInBackground: ", request.getXML());
            String responseString = HTTPHelper.getResponse(request
                            .getXML(),
                    SoapActionHelper.ACTION_GET_COUNTRY_LIST
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();
            String message = "server error";
            try {
                assert jsonObject != null;
                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetCountryListResponse").getJSONObject("GetCountryListResult");
                String responseCode = jsonObject.getString("ResponseCode");
                message = jsonObject.getString("Description");
                if (responseCode.equals("101")) {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetCountryList");
                    JSONArray array = jsonObject.getJSONArray("tblCountryList");
                    //  purposeList.clear();
                    countryListResponses = new ArrayList<>();
                    Log.e("doInBackground: ", jsonObject.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        GetCountryListResponse response = new GetCountryListResponse();
                        response.id = object.getInt("CountryID");
                        response.countryName = object.getString("CountryName");
                        response.countryShortName = object.getString("CountryShortName");
                        response.isActive = object.getBoolean("Active");
                        response.currencyShortName = object.getString("CurrencyShortName");
                        try {
                            response.countryCode = object.getString("CountryCode");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            response.imageURL = object.getString("Image_URL");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        //  response.imageURL = object.getString("Image_URL");
                        response.countryType = object.getInt("CountryType");
                        response.countryOrigin = object.getInt("Country_Origine");
                        countryListResponses.add(response);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return countryListResponses;
        }
    }
}
