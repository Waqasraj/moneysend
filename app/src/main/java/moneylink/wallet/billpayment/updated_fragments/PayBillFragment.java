package moneylink.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import moneylink.wallet.R;
import moneylink.wallet.billpayment.BillPaymentMainActivity;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.billpayment.viewmodel.BillPaymentViewModel;
import moneylink.wallet.databinding.PayBillFragmentLayoutBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.utils.Utils;

public class PayBillFragment extends BaseFragment<PayBillFragmentLayoutBinding> {

    BillPaymentViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        getBillPayment();

        binding.toHome.setOnClickListener(v -> {
            getBaseActivity().finish();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.pay_bill_fragment_layout;
    }

    void getBillPayment() {
        Utils.showCustomProgressDialog(getContext(), false);
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.Customer_No = getSessionManager().getCustomerNo();
        String body = RestClient.makeGSONString(((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

        viewModel.getPayBill(aeRequest, HelperKeys.key,
                HelperKeys.secretkey).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //   onMessage(response.resource.description);

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);
                            Log.e("getBillPaymnet: ", bodyy);
                            binding.firstCardview.setVisibility(View.VISIBLE);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<PayBillPaymentResponse>() {
                                }.getType();
                                PayBillPaymentResponse data = gson.fromJson(bodyy, type);


                                binding.firstCardview.setVisibility(View.VISIBLE);

                                binding.billNumber.setText(data.authValue);
                                binding.customerName.setText(data.billerName);
                                binding.billAmount.setText(data.totalPayable);
                                binding.transactionDateTime.setText(data.billPayRespData.responseDateTime);
                                binding.transactionrefno.setText(data.billPayRespData.entityTransactionID);
                                binding.paymentStatus.setText(data.billPayRespData.responseMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);
                            Log.e("getBillPaymnet: ", bodyy);
                            binding.firstCardview.setVisibility(View.VISIBLE);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<PayBillPaymentResponse.PaymentAccount>() {
                                }.getType();
                                PayBillPaymentResponse.PaymentAccount data = gson.fromJson(bodyy, type);


                                if (data.responseMessage.equalsIgnoreCase("Invalid reference")) {
                                    onMessage(data.responseMessage + "\n" + "Please check your mobile number");
                                } else {
                                    onMessage(data.responseMessage);
                                }
                                getBaseActivity().finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Utils.hideCustomProgressDialog();
                            //binding.progressBar.setVisibility(View.GONE);
                            //   onMessage(response.resource.description);
                        }
                    }
                });

    }

}
