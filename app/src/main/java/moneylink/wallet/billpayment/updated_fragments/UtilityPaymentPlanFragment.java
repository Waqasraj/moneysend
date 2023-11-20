package moneylink.wallet.billpayment.updated_fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.WRBillerPlansAdapter;
import moneylink.wallet.billpayment.BillPaymentMainActivity;
import moneylink.wallet.databinding.FragmentWRBillerNamesBinding;
import moneylink.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import moneylink.wallet.di.RequestHelper.WRBillerPlansRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerPlanResponse;
import moneylink.wallet.di.apicaller.WRBillerPlanTask;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnBillerPlans;
import moneylink.wallet.utils.IsNetworkConnection;

public class UtilityPaymentPlanFragment extends BaseFragment<FragmentWRBillerNamesBinding>
        implements OnBillerPlans {

    WRBillerPlansRequest billerNamesRequest;
    List<WRBillerPlanResponse> billerNamesList;
    WRBillerPlansAdapter adapter;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        billerNamesList = new ArrayList<>();
        setupRecyclerView();
        binding.title.setText(getString(R.string.select_service_provider));

        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            billerNamesRequest = new WRBillerPlansRequest();
            billerNamesRequest.billerID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerID;
            billerNamesRequest.billerCategoryId = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerCategoryId;
            billerNamesRequest.billerTypeID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerTypeID;
            billerNamesRequest.countryCode = ((BillPaymentMainActivity) getBaseActivity())
                    .plansRequest.countryCode;
            billerNamesRequest.languageId = getSessionManager().getlanguageselection();
            WRBillerPlanTask task = new WRBillerPlanTask(getContext(), this);
            task.execute(billerNamesRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_w_r_biller_names;
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerPlansAdapter(billerNamesList, this);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(adapter);
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    @Override
    public void onBillerPlanSelect(PrepaidPlanResponse.MobileTopUpSKU plan) {

    }
}