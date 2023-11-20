package moneylink.wallet.billpayment.updated_fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import moneylink.wallet.R;
import moneylink.wallet.billpayment.BillPaymentMainActivity;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.billpayment.viewmodel.BillPaymentViewModel;
import moneylink.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restRequest.GetWRBillDetailRequestN;
import moneylink.wallet.di.JSONdi.restRequest.paykiiMobileToup.GetWAPaymentDetail;
import moneylink.wallet.di.JSONdi.restResponse.BillDetailResponse;
import moneylink.wallet.di.JSONdi.restResponse.paykiiResponse.GetPaymentDetailsResponse;

import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.SingleButtonMessageDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.NewDashboardActivity;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.utils.Utils;

public class UtilityBillerDetailsFragment extends BaseFragment<FragmentUtilityBillerDetailsBinding>
        implements OnDecisionMade {


    BillPaymentViewModel viewModel;
    int selectedID = 4;

    String enquiry = "1";
    String enteredAmount = "";

    @Override
    protected void injectView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);

        assert getArguments() != null;
        enquiry = getArguments().getString("inquiry");
        enteredAmount = getArguments().getString("amount");
        selectedID = binding.cashWallet.getId();
        if (enquiry.equalsIgnoreCase("1")) {
            getBillDetails();
        } else {
            getPaymentDetails();
        }

        binding.toPayment.setOnClickListener(v -> {
            switch (selectedID) {
                case R.id.cash_wallet:
                    payBill();
                    break;
                case R.id.radio_wallet:
                case R.id.bank_deposit:
                case R.id.radio_thorugh_card:
                    showError();
                    break;

            }
        });

        selectedID = binding.cashWallet.getId();
        binding.paymentOptionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            selectedID = radioButton.getId();
        });

    }

    void showError() {
        SingleButtonMessageDialog dialog = new SingleButtonMessageDialog("Alert", "Please select cash as payment for testing"
                , this, true);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");

    }


    void payBill() {

        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.BillerID = ((BillPaymentMainActivity) getBaseActivity()).billDetailsRequest.BillerID;
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.SkuID = ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.SkuID;
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.Receiving_CountryCode = ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.Receiving_CountryCode;
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.Send_Currency = ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest.Send_Currency;
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.Payment_TypeID = "4";
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.IpCountryName = getSessionManager().getIpCountryName();
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.IpAddress = getSessionManager().getIpAddress();

        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.payBillFragment);
    }

    void getPaymentDetails() {
        GetWAPaymentDetail rechargeRequest = new GetWAPaymentDetail();
        rechargeRequest.BillerID = ((BillPaymentMainActivity) getBaseActivity()).billDetailsRequest.BillerID;
        rechargeRequest.Send_Currency = ((BillPaymentMainActivity) getBaseActivity()).billDetailsRequest.Send_Currency;
        rechargeRequest.SkuID = ((BillPaymentMainActivity) getBaseActivity()).billDetailsRequest.SkuID;
        rechargeRequest.Entered_Amount = enteredAmount;
        String body = RestClient.makeGSONString(rechargeRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);
        Log.e("getPaymentDetails: ", body);
        viewModel.getPaymentDetails(request, HelperKeys.key,
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
                                Type type = new TypeToken<GetPaymentDetailsResponse>() {
                                }.getType();
                                GetPaymentDetailsResponse data = gson.fromJson(bodyy, type);
                                ((BillPaymentMainActivity) getBaseActivity())
                                        .payBillRequest.PayOutAmount = data.totalPayable;
                                showBillDataONEn(data);
                                //  ((BillPaymentMainActivity) getBaseActivity())
                                //        .payBillRequest.input1 = data.billDetails.output1;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });


    }

    void getBillDetails() {
        Utils.showCustomProgressDialog(getContext(), false);

        GetWRBillDetailRequestN billDetailsRequest = ((BillPaymentMainActivity) getBaseActivity())
                .billDetailsRequest;

        String body = RestClient.makeGSONString(billDetailsRequest);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

        Log.e("getBillDetails: ", body);
        viewModel.getWABillPaymentDetails(aeRequest, HelperKeys.key,
                HelperKeys.secretkey).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));

                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            BillDetailResponse data = null;
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);
                            Log.e("getBillDetails: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillDetailResponse>() {
                                }.getType();
                                data = gson.fromJson(bodyy, type);
                                showBillData(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            onMessage(response.resource.description);
                        }


                    }
                });

    }

    public void showBillDataONEn(GetPaymentDetailsResponse data) {
        binding.customerName.setText(data.billerName);
        binding.billAmount.setText(data.payoutCurrency.concat(" ").concat(data.exchangeRate));
        binding.netBillAmount.setText(data.payoutCurrency.concat(" ").concat(data.totalPayable));
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.PayOutAmount = data.totalPayable;

//        ((BillPaymentMainActivity) getBaseActivity())
//                .payBillRequest.input1 = data.billDetails.output1;
//        binding.billDueDate.setText(data.billDetails.billDueDate);
        binding.mainView.setVisibility(View.VISIBLE);
        binding.firstCardview.setVisibility(View.VISIBLE);
        binding.billDetails.setVisibility(View.VISIBLE);
    }


    public void showBillData(BillDetailResponse data) {
        binding.customerName.setText(data.billerName);
        binding.billAmount.setText(data.payOutCurrency.concat(" ").concat(data.exchangeRate));
        binding.netBillAmount.setText(data.payOutCurrency.concat(" ").concat(data.billDetails.totalDue));
        binding.billDueDate.setText(data.billDetails.billDueDate);

        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.PayOutAmount = data.billDetails.totalDue;

        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.input1 = data.billDetails.output1;
        binding.mainView.setVisibility(View.VISIBLE);
        binding.firstCardview.setVisibility(View.VISIBLE);
        binding.billDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_biller_details;
    }

    @Override
    public void showProgress() {
        super.showProgress();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void onProceed() {
        Intent intent = new Intent(getActivity(), NewDashboardActivity.class);
        startActivity(intent);
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {

    }
}