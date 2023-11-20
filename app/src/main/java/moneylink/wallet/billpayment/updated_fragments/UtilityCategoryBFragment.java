package moneylink.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.WRBillerCategoryListAdapter;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.billpayment.viewmodel.BillPaymentViewModel;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import moneylink.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import moneylink.wallet.di.RequestHelper.GetWRCountryListRequest;
import moneylink.wallet.di.ResponseHelper.GetWRCountryListResponseC;
import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.dialogs.WRCountryListDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnWRBillerCategoryResponse;
import moneylink.wallet.interfaces.OnWRCountryList;
import moneylink.wallet.utils.Utils;
import moneylink.wallet.wallet.ItemOffsetDecoration;


public class UtilityCategoryBFragment extends BaseFragment<TransferDialogPurposeBinding>
        implements OnWRBillerCategoryResponse, OnWRCountryList {

    BillPaymentViewModel viewModel;

    WRBillerCategoryListAdapter adapter;
    ImageView close_dialog;
    List<GetWRBillerCategoryResponseC> wrBillerCategoryList;
    public String billerCategoryId = "";

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);

        binding.toolBarLayout.setVisibility(View.GONE);
        close_dialog = getView().findViewById(R.id.close_dialog);


        binding.toolBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.titleOfPage.setText("Bill Payment");


        close_dialog.setOnClickListener(v -> getBaseActivity().finish());
        setSearchView();
        getCategoryBills();

    }


    void getCategoryBills() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetWRBillerCategoryRequestC request = new GetWRBillerCategoryRequestC();
        String body = RestClient.makeGSONString(request);
        Log.e("daat: ",body );
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);
 //  /KHGpOpyofigFYNkNphiaw4eYBhAqxgb1QRfiyc4AR5DF6JYXh9f/+Sw2u2BocE8In9Jmgtg9kdyTSbfilxXCQ==

        Log.e("getCategoryBills: ", aeRequest.body);

        viewModel.GetWRBillerCategory(aeRequest, HelperKeys.key,
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
                            Log.e("getCategoryBills: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRBillerCategoryResponseC>>() {
                                }.getType();
                                List<GetWRBillerCategoryResponseC> data = gson.fromJson(bodyy, type);
                                onWRCategory(data);
                                setupRecyclerView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });
    }


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

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    private void setupRecyclerView() {

        Collections.sort(wrBillerCategoryList, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
        /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());*/
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.margin_5dp);
        binding.transferPurposeList.addItemDecoration(itemDecoration);
        adapter = new
                WRBillerCategoryListAdapter(getContext(), wrBillerCategoryList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onWRCategory(List<GetWRBillerCategoryResponseC> responseList) {
        wrBillerCategoryList = new ArrayList<>();
        wrBillerCategoryList.clear();
        wrBillerCategoryList.addAll(responseList);
    }

    @Override
    public void onSelectCategory(GetWRBillerCategoryResponseC category) {
        billerCategoryId = category.Name;

        getCountries(category.Name);

    }


    @Override
    public void onWRCountryList(List<GetWRCountryListResponseC> list) {
        WRCountryListDialog dialog = new WRCountryListDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onWRSelectCountry(GetWRCountryListResponseC country) {
        Bundle bundle = new Bundle();
        bundle.putString("billerCategoryId", billerCategoryId);
        bundle.putString("countryCode", country.code);
        Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_utilityCategoryBFragment_to_utilityBillPaymentPlanFragment, bundle);
    }


    void getCountries(String billerType) {
        Utils.showCustomProgressDialog(getContext(), false);
        GetWRCountryListRequest request = new GetWRCountryListRequest();
        request.Biller_Type = billerType;
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);

        viewModel.GetCountryList(aeRequest, HelperKeys.key,
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
                            Log.e("getCategoryBills: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRCountryListResponseC>>() {
                                }.getType();
                                List<GetWRCountryListResponseC> data = gson.fromJson(bodyy, type);
                                onWRCountryList(data);
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

