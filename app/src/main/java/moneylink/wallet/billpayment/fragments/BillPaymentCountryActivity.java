package moneylink.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.WRCountryListAdapter;
import moneylink.wallet.databinding.ActivityBillPaymentCountryBinding;
import moneylink.wallet.di.RequestHelper.GetWRCountryListRequest;
import moneylink.wallet.di.ResponseHelper.GetWRCountryListResponseC;
import moneylink.wallet.di.apicaller.GetWRCountryListTask;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnWRCountryList;
import moneylink.wallet.utils.IsNetworkConnection;

public class BillPaymentCountryActivity extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRCountryList {


    WRCountryListAdapter adapter;
    List<GetWRCountryListResponseC> countryList;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        countryList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRCountryListRequest request = new GetWRCountryListRequest();
            //request.languageId = getSessionManager().getlanguageselection();

            GetWRCountryListTask task = new GetWRCountryListTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_payment_country;
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

//        Collections.sort(countryList, (o1, o2) ->
//                o1.countryName.compareToIgnoreCase(o2.countryName));
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        adapter = new
//                WRCountryListAdapter(getContext() , countryList, this);
//        binding.countryRecyclerView.setLayoutManager(mLayoutManager);
//        binding.countryRecyclerView.setHasFixedSize(true);
//        binding.countryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onWRCountryList(List<GetWRCountryListResponseC> list) {
        countryList.clear();
        countryList.addAll(list);
        binding.searchView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWRSelectCountry(GetWRCountryListResponseC country) {
//        ((BillPaymentMainActivity) getBaseActivity())
//                .plansRequest.countryCode = country.countryShortName;

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}