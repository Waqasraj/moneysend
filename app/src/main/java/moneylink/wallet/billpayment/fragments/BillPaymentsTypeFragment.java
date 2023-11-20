package moneylink.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.WRBillerTypeAdapter;
import moneylink.wallet.billpayment.BillPaymentMainActivity;
import moneylink.wallet.databinding.ActivityBillPaymentsBinding;
import moneylink.wallet.di.RequestHelper.GetWRBillerTypeRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerTypeResponse;
import moneylink.wallet.di.apicaller.WRBillerTypeTask;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnWRBillerType;
import moneylink.wallet.utils.IsNetworkConnection;

public class BillPaymentsTypeFragment extends BaseFragment<ActivityBillPaymentsBinding>
        implements OnWRBillerType {


    List<WRBillerTypeResponse> wrBillerTypeResponseList;
    WRBillerTypeAdapter adapter;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        wrBillerTypeResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();
        binding.title.setText(getString(R.string.select_recharge_type));
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerTypeRequest request = new GetWRBillerTypeRequest();
            request.languageId = getSessionManager().getlanguageselection();

            WRBillerTypeTask task = new WRBillerTypeTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_payments;
    }

    /**
     * method will init the search box
     */
    private void setSearchView() {
        binding.searchView.setMaxWidth(Integer.MAX_VALUE);
        binding.searchView.requestFocus();
        binding.searchView.setFocusable(true);
        binding.searchView.setHint(getString(R.string.search));


        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {

        Collections.sort(wrBillerTypeResponseList, (o1, o2) ->
                o1.billerName.compareToIgnoreCase(o2.billerName));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerTypeAdapter(getContext() , wrBillerTypeResponseList, this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBillerTypeList(List<WRBillerTypeResponse> billerTypeList) {
        wrBillerTypeResponseList.clear();
        wrBillerTypeResponseList.addAll(billerTypeList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBillerTypeSelect(WRBillerTypeResponse billerType) {
        ((BillPaymentMainActivity) getBaseActivity())
                .plansRequest.billerTypeID = billerType.id;

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}