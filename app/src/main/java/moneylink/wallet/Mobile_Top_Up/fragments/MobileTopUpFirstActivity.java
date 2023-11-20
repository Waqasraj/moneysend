package moneylink.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import moneylink.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import moneylink.wallet.R;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.databinding.ActivityMobileTopUpFirstBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restRequest.PrepaidBillerNames;
import moneylink.wallet.di.JSONdi.restRequest.PrepaidOperatorsRequest;
import moneylink.wallet.di.JSONdi.restRequest.SimpleRequest;
import moneylink.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import moneylink.wallet.di.RequestHelper.GetCCYForWalletRequest;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import moneylink.wallet.di.ResponseHelper.WRCountryListResponse;
import moneylink.wallet.di.apicaller.GetCCYForWalletTask;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.DialogCurrency;
import moneylink.wallet.dialogs.WRBillerOperatorsDialog;
import moneylink.wallet.dialogs.WRPrepaidCountryDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnPrepaidCountry;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.StringHelper;
import moneylink.wallet.utils.Utils;
import moneylink.wallet.utils.WalletTypeHelper;

public class MobileTopUpFirstActivity extends BaseFragment<ActivityMobileTopUpFirstBinding>
        implements OnPrepaidCountry, OnSelectCurrency {

    MobileTopUpViewModel viewModel;
    List<PrepaidOperatorResponse> billerFields;

    List<WRCountryListResponse> countryListResponseList;


    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        binding.countryCodeTextView.setText(viewModel.callingCode);
    }

    public boolean isNumberValidate() {
        if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.mobilesignupb.getText().toString(),
                binding.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));

            return false;
        }
        return true;
    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.mobilesignupb.getText().toString(),
                binding.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (binding.selectCur.getText().toString().isEmpty()) {
            onMessage(getString(R.string.select_currecny));
            return false;
        } else if (TextUtils.isEmpty(binding.operator.getText())) {
            onMessage(getString(R.string.plz_select_operator));
            return false;
        }
        return true;
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {

        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        countryListResponseList = new ArrayList<>();
        billerFields = new ArrayList<>();
        binding.countrySpinnerSignIn.setOnClickListener(v -> {
            if (countryListResponseList.isEmpty()) {
                getPrepaidCountries();
            } else {
                openDialog();
            }

        });

        binding.operator.setOnClickListener(v -> {
            openOperatorDialog();
        });

        binding.nextLayout.setOnClickListener(v -> {
            if (isNumberValidate()) {
                getPrepaidBillers();
            }
        });

        binding.selectCur.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                showWallets(getSessionManager().getCustomerNo());
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.getPlans.setOnClickListener(v -> {
            if (isValidate()) {
                getPrepaidBillersRetry();
            }
        });


        binding.mobilesignupb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.operatorLayout.setVisibility(View.GONE);
                binding.operatorTxt.setVisibility(View.GONE);
                binding.nextLayout.setVisibility(View.VISIBLE);
                billerFields.clear();
                binding.getPlans.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void showWallets(String customerno) {
        GetCCYForWalletRequest request = new GetCCYForWalletRequest();
        request.actionType = WalletTypeHelper.RECEIVE;
        request.customerNo = customerno;
        request.languageID = getSessionManager().getlanguageselection();


        GetCCYForWalletTask task = new GetCCYForWalletTask(getContext(), this);
        task.execute(request);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_first;
    }

    void getPrepaidBillers() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            PrepaidBillerNames request = new PrepaidBillerNames();

            if (binding.countryCodeTextView.getText().toString().equalsIgnoreCase("+971")) {
                String code = StringHelper.removeFirst(binding.countryCodeTextView.getText().toString());
                request.Mobile_Number = "0" + code
                        + binding.mobilesignupb.getText().toString();
            } else {
                request.Mobile_Number = StringHelper.removeFirst(binding.countryCodeTextView.getText().toString())
                        + binding.mobilesignupb.getText().toString();
            }


            String body = RestClient.makeGSONString(request);
            Log.e("getPrepaidBillers: ", body);
            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

            viewModel.getPrepaidBillerNames(aeRequest, HelperKeys.key,
                    HelperKeys.secretkey).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            //if data came will jump to next to bundles/packages
                            if (response.resource.responseCode.equals(101)) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , HelperKeys.TxnKey);

                                Log.e("getPrepaidBillers: ", bodyy);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<PrepaidPlanResponse>() {
                                    }.getType();
                                    PrepaidPlanResponse data = gson.fromJson(bodyy, type);
                                    viewModel.prepaidPlanResponses = data;
                                    viewModel.payBillRequest.input1 = data.outPut;
                                    viewModel.billerID = data.biller.billerID;

                                    Navigation.findNavController(binding.getRoot()).navigate(R.id
                                            .WRBillerNamesFragment);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                //will find operators
                                //  onMessage(response.resource.description);
                                getPrepaidOperator();
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    void getPrepaidBillersRetry() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            PrepaidBillerNames request = new PrepaidBillerNames();
            if (binding.countryCodeTextView.getText().toString().equalsIgnoreCase("+971")) {
                String code = StringHelper.removeFirst(binding.countryCodeTextView.getText().toString());
                request.Mobile_Number = "0" + code
                        + binding.mobilesignupb.getText().toString();

            } else {
                request.Mobile_Number = binding.countryCodeTextView.getText().toString()
                        + binding.mobilesignupb.getText().toString();
            }


            request.Biller_ID = viewModel.billerID;

            String body = RestClient.makeGSONString(request);
            Log.e("getPrepaidBillers: ", body);
            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

            viewModel.getPrepaidBillerNamesRetry(aeRequest, HelperKeys.key,
                    HelperKeys.secretkey).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            //if data came will jump to next to bundles/packages
                            if (response.resource.responseCode.equals(101)) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , HelperKeys.TxnKey);
                                Log.e("getPrepaidBillers: ", bodyy);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<PrepaidPlanResponse>() {
                                    }.getType();
                                    PrepaidPlanResponse data = gson.fromJson(bodyy, type);
                                    viewModel.prepaidPlanResponses = data;
                                    viewModel.payBillRequest.input1 = data.outPut;
                                    Navigation.findNavController(binding.getRoot()).navigate(R.id
                                            .WRBillerNamesFragment);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {

                                onMessage(response.resource.description);
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    void getPrepaidCountries() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            SimpleRequest request = new SimpleRequest();

            String body = RestClient.makeGSONString(request);

            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

            viewModel.getPrepaidCountry(aeRequest, HelperKeys.key,
                    HelperKeys.secretkey).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {


                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , HelperKeys.TxnKey);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<WRCountryListResponse>>() {
                                    }.getType();
                                    List<WRCountryListResponse> data = gson.fromJson(bodyy, type);
                                    countryListResponseList.addAll(data);
                                    openDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    void openDialog() {
        WRPrepaidCountryDialog dialog = new WRPrepaidCountryDialog(countryListResponseList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    void openOperatorDialog() {
        WRBillerOperatorsDialog dialog =
                new WRBillerOperatorsDialog(billerFields, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    void getPrepaidOperator() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            PrepaidOperatorsRequest request = new PrepaidOperatorsRequest();
            request.CountryCode = viewModel.countryCode;

            String body = RestClient.makeGSONString(request);

            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

            viewModel.getBillerNamesOnNotFound(aeRequest, HelperKeys.key,
                    HelperKeys.secretkey).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {


                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , HelperKeys.TxnKey);
                                Log.e("getPrepaidOperator: ", bodyy);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<PrepaidOperatorResponse>>() {
                                    }.getType();
                                    List<PrepaidOperatorResponse> data = gson.fromJson(bodyy, type);
                                    onBillerNamesList(data);
                                    binding.operatorLayout.setVisibility(View.VISIBLE);
                                    binding.operatorTxt.setVisibility(View.VISIBLE);
                                    binding.nextLayout.setVisibility(View.GONE);

                                    binding.getPlans.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public void onSelectPrepaidCountry(WRCountryListResponse wrCountryListResponse) {
        binding.countryCodeTextView.setText("+" + wrCountryListResponse.callingCode);
        viewModel.countryCode = wrCountryListResponse.countryCode;
        viewModel.callingCode = wrCountryListResponse.callingCode;
    }

    @Override
    public void onPrepaidOperator(PrepaidOperatorResponse prepaidOperatorResponse) {
        binding.operator.setText(prepaidOperatorResponse.billerName);
        viewModel.billerID = prepaidOperatorResponse.billerID;
    }


    public void onBillerNamesList(List<PrepaidOperatorResponse> responses) {
        billerFields.addAll(responses);
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        showCurrencyDialog(response);
    }

    void showCurrencyDialog(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        viewModel.payBillRequest.Send_Currency = response.currencyShortName;
        binding.selectCur.setText(response.currencyShortName);

    }
}
