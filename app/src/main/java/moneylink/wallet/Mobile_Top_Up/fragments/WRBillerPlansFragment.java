package moneylink.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import moneylink.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import moneylink.wallet.R;
import moneylink.wallet.adapters.WrBillerPrepaidPlansAdapter;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.databinding.FragmentWRBillerNamesBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restRequest.paykiiMobileToup.GetWAPaymentDetail;
import moneylink.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import moneylink.wallet.di.JSONdi.restResponse.paykiiResponse.GetPaymentDetailsResponse;

import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.SingleButtonMessageDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnBillerPlans;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.Utils;

public class WRBillerPlansFragment extends BaseFragment<FragmentWRBillerNamesBinding>
        implements OnBillerPlans, OnDecisionMade {

    List<PrepaidPlanResponse.MobileTopUpSKU> filteredList;
    WrBillerPrepaidPlansAdapter prepaidPlansAdapter;
    MobileTopUpViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        filteredList = new ArrayList<>();
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        filteredList.addAll(viewModel.prepaidPlanResponses.skuList);
        setupPrePaidRecyclerView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_w_r_biller_names;
    }

    private void setupPrePaidRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        prepaidPlansAdapter = new WrBillerPrepaidPlansAdapter(filteredList, this);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(prepaidPlansAdapter);
    }


    /**
     * Method will set the recycler view
     */


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    private void showSuccess(String rechargeId) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(getString(R.string.successfully_txt)
                , "RechargeId = " + rechargeId, this,
                false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onBillerPlanSelect(PrepaidPlanResponse.MobileTopUpSKU plan) {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            viewModel.payBillRequest.BillerID = viewModel.billerID;
            viewModel.payBillRequest.SkuID = plan.sku;
            viewModel.payBillRequest.Receiving_CountryCode = viewModel.countryCode;
         //   viewModel.payBillRequest.Send_Currency = plan.currency;
            viewModel.payBillRequest.PayOutAmount = plan.amount;
            GetWAPaymentDetail rechargeRequest = new GetWAPaymentDetail();
            rechargeRequest.BillerID = viewModel.billerID;
            rechargeRequest.Send_Currency = viewModel.payBillRequest.Send_Currency;
            rechargeRequest.SkuID = plan.sku;
            rechargeRequest.Entered_Amount = plan.amount;
            String body = RestClient.makeGSONString(rechargeRequest);
//{"body":"cY24ezYhS1+B3XRFJwKqXFPq32PVZVppFAAlcVK7GBG+aRuZPq7NmwLvt8Sm11Qj1zhcxx0moE2OS1AUF6IcanIk2Tor6sJnAHUPuEibklDjkaFkRlhn5nCDciM5jB0+nihJLO2XE12eN9wBnI53Fa2B15hdbQh2KODQ0P5mR\/Zyg53NsD3LZzT+1we5Zevd"}
            AERequest request = new AERequest();
            request.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

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

                                    viewModel.getPaymentDetailsResponse = data;

                                    Navigation.findNavController(binding.getRoot())
                                            .navigate(R.id.WRBillerPaymentFragment);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });

        }
    }
}