package moneylink.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.billpayment.BillPaymentMainActivity;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.billpayment.helpers.BillerInputTypes;
import moneylink.wallet.billpayment.viewmodel.BillPaymentViewModel;
import moneylink.wallet.databinding.FragmentUtilityPaymentAccountBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;

import moneylink.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import moneylink.wallet.di.RequestHelper.GetCCYForWalletRequest;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.di.apicaller.GetCCYForWalletTask;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.DialogCurrency;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.interfaces.OnWRBillerFields;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.Utils;
import moneylink.wallet.utils.WalletTypeHelper;


public class UtilityPaymentAccountNoFragment extends BaseFragment<FragmentUtilityPaymentAccountBinding>
        implements OnWRBillerFields, OnSelectCurrency {


    List<GetWRBillerFieldsResponseN.Fields> wrBillerList;
    BillPaymentViewModel viewModel;
    String countryCode, billerID, skuID;

    @Override
    protected void injectView() {

    }

    public boolean isValidateOne() {
        if (binding.selectCur.getText().toString().isEmpty()) {
            onMessage(getString(R.string.select_currecny));
            return false;
        } else if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().toString().length() < wrBillerList.get(0).minLength ||
                binding.accountNo.getText().length() > wrBillerList.get(0).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));

            return false;
        }
        return true;
    }

    public boolean isValidateTwe() {
        if (binding.selectCur.getText().toString().isEmpty()) {
            onMessage(getString(R.string.select_currecny));
            return false;
        } else if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() < wrBillerList.get(0).minLength
                ||
                binding.accountNo.getText().length() > wrBillerList.get(0).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));

            return false;
        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;
        } else if (binding.accountPolicyNo.getText().length() < wrBillerList.get(1).minLength
                ||
                binding.accountPolicyNo.getText().length() > wrBillerList.get(1).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));
            return false;
        }
        return true;
    }

    public boolean isValidateThree() {

        if (binding.selectCur.getText().toString().isEmpty()) {
            onMessage(getString(R.string.select_currecny));
            return false;
        } else if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() < wrBillerList.get(0).minLength
                ||
                binding.accountNo.getText().length() > wrBillerList.get(0).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(0).labelName));

            return false;
        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;
        } else if (binding.accountPolicyNo.getText().length() < wrBillerList.get(1).minLength
                ||
                binding.accountPolicyNo.getText().length() > wrBillerList.get(1).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));

            return false;
        } else if (TextUtils.isEmpty(binding.reEnterAccountPolicy.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(wrBillerList.get(2).labelName));
            return false;
        } else if (binding.reEnterAccountPolicy.getText().length() < wrBillerList.get(2).minLength
                ||
                binding.reEnterAccountPolicy.getText().length() > wrBillerList.get(2).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(2).labelName));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        wrBillerList = new ArrayList<>();
        billerID = getArguments().getString("billerId");
        skuID = getArguments().getString("skuID");
        countryCode = getArguments().getString("countryCode");


        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        getWRBillerFields();

        binding.nextLayout.setOnClickListener(v -> {
            if (wrBillerList.size() == 1) {
                if (isValidateOne()) {
                    fillData1(1);
                }
            } else if (wrBillerList.size() == 2) {
                if (isValidateTwe()) {
                    fillData1(2);
                }
            } else if (wrBillerList.size() == 3) {
                if (isValidateThree()) {
                    fillData1(3);
                }
            }

        });


        binding.selectCur.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {

                showWallets(getSessionManager().getCustomerNo());

            } else {
                onMessage(getString(R.string.no_internet));
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


    public void fillData1(int size) {
        ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.BillerID = billerID;
        ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.SkuID = skuID;
        ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.Receiving_CountryCode = countryCode;

        if (size == 1) {
            ((BillPaymentMainActivity) getBaseActivity())
                    .billDetailsRequest.Inputs = binding.accountNo.getText().toString();
        } else if (size == 2) {
            ((BillPaymentMainActivity) getBaseActivity())
                    .billDetailsRequest.Inputs = binding.accountNo.getText().toString().concat("|")
                    .concat(binding.accountPolicyNo.getText().toString());
        } else if (size == 3) {
            ((BillPaymentMainActivity) getBaseActivity())
                    .billDetailsRequest.Inputs = binding.accountNo.getText().toString().concat("|")
                    .concat(binding.accountPolicyNo.getText().toString()).concat("|")
                    .concat(binding.reEnterAccountPolicy.getText().toString());
        }


        if (viewModel.inquiry.equalsIgnoreCase("0")) {
            if (binding.amount.getText().toString().isEmpty()) {
                onMessage("Please enter amount");
                return;
            }

            if (size == 1) {
                ((BillPaymentMainActivity) getBaseActivity())
                        .payBillRequest.input1 = binding.accountNo.getText().toString();
            } else if (size == 2) {
                ((BillPaymentMainActivity) getBaseActivity())
                        .payBillRequest.input1 = binding.accountNo.getText().toString().concat("|")
                        .concat(binding.accountPolicyNo.getText().toString());
            } else if (size == 3) {
                ((BillPaymentMainActivity) getBaseActivity())
                        .payBillRequest.input1 = binding.accountNo.getText().toString().concat("|")
                        .concat(binding.accountPolicyNo.getText().toString()).concat("|")
                        .concat(binding.reEnterAccountPolicy.getText().toString());
            }

        }

        Bundle bundle = new Bundle();
        bundle.putString("inquiry", viewModel.inquiry);
        bundle.putString("amount", binding.amount.getText().toString());
        Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_utilityPaymentAccountNoFragment_to_utilityBillerDetailsFragment, bundle);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_payment_account;
    }

    @Override
    public void onWRBillerField(List<GetWRBillerFieldsResponseN.Fields> response) {
        wrBillerList = new ArrayList<>();
        wrBillerList.addAll(response);

        if (response.size() == 1) {
            onSizeOne(response);
        } else if (response.size() == 2) {
            onSizeTwo(response);
        } else if (response.size() == 3) {
            onSizeThree(response);
        }


        if (viewModel.inquiry.equalsIgnoreCase("0")) {
            binding.amountTxt.setVisibility(View.VISIBLE);
            binding.amount.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    void onSizeOne(List<GetWRBillerFieldsResponseN.Fields> response) {
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        binding.accountNo.setHint("Enter " + response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.accountPolicyNoField.setVisibility(View.GONE);
        binding.accountPolicyNo.setVisibility(View.GONE);

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);
    }

    void onSizeTwo(List<GetWRBillerFieldsResponseN.Fields> response) {
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        binding.accountNo.setHint("Enter " + response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.accountPolicyNoField.setHint(response.get(1).labelName + "*");
        binding.accountPolicyNo.setHint("Enter " + response.get(1).description);
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);
    }

    void onSizeThree(List<GetWRBillerFieldsResponseN.Fields> response) {
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        binding.accountNo.setHint("Enter " + response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.accountPolicyNoField.setHint(response.get(1).labelName + "*");
        binding.accountPolicyNo.setHint("Enter " + response.get(1).description);
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        binding.descriptionField.setHint(response.get(2).labelName + "*");
        binding.reEnterAccountPolicy.setHint("Enter " + response.get(2).description);
        if (response.get(3).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.reEnterAccountPolicy.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    private void getWRBillerFields() {

        Utils.showCustomProgressDialog(getContext(), false);
        GetWRBillerFieldsRequestN request = new GetWRBillerFieldsRequestN();
        request.BillerID = billerID;
        request.CountryCode = countryCode;
        request.SkuID = skuID;

        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);


        viewModel.GetWRBillerFields(aeRequest, HelperKeys.key,
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
                            Log.e("getWRBillerFields: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<GetWRBillerFieldsResponseN>() {
                                }.getType();
                                GetWRBillerFieldsResponseN data = gson.fromJson(bodyy, type);

                                viewModel.inquiry = data.inquiryAvailable;
                                onWRBillerField(data.billerFields);
                                binding.mainView.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
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
        ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.Send_Currency = response.currencyShortName;
        binding.selectCur.setText(response.currencyShortName);

    }
}