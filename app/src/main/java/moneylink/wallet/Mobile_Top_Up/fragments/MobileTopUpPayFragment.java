package moneylink.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import moneylink.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import moneylink.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import moneylink.wallet.R;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.databinding.PayBillFragmentLayoutBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.SingleButtonMessageDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.utils.Utils;

public class MobileTopUpPayFragment extends BaseFragment<PayBillFragmentLayoutBinding> implements OnDecisionMade {


    MobileTopUpViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        getBillPaymnet();

        binding.toHome.setOnClickListener(v -> {
            getBaseActivity().finish();
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.pay_bill_fragment_layout;
    }

    void getBillPaymnet() {
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.payBillRequest.Customer_No = getSessionManager().getCustomerNo();
        String body = RestClient.makeGSONString(viewModel.payBillRequest);
        Log.e("getBillPaymnet: ", body);
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
                            onMessage(response.resource.description);

                            // binding.progressBar.setVisibility(View.GONE);
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);

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
                            Utils.hideCustomProgressDialog();
                            //binding.progressBar.setVisibility(View.GONE);
                            // binding.progressBar.setVisibility(View.GONE);
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);

                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<PayBillPaymentResponse.PaymentAccount>() {
                                }.getType();
                                PayBillPaymentResponse.PaymentAccount data = gson.fromJson(bodyy, type);

                                SingleButtonMessageDialog dialog = new SingleButtonMessageDialog("Error"
                                        , data.responseMessage + "Invalid Number", this
                                        , false);
                                dialog.setCancelable(false);

                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                dialog.show(transaction, "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

    }

    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {
        getBaseActivity().finish();
    }
}
