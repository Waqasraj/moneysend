package moneylink.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.WRBillerOperatorAdapter;
import moneylink.wallet.billpayment.HelperKeys;
import moneylink.wallet.billpayment.viewmodel.BillPaymentViewModel;
import moneylink.wallet.databinding.UtilityPaymentPlansViewBinding;
import moneylink.wallet.di.AESHelper;
import moneylink.wallet.di.JSONdi.Status;
import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import moneylink.wallet.di.JSONdi.restRequest.SKUByBillerIDRequest;
import moneylink.wallet.di.JSONdi.restResponse.BillerSKUDataResponse;
import moneylink.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;

import moneylink.wallet.di.retrofit.RestClient;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnWRBillerNames;
import moneylink.wallet.utils.Utils;


public class UtilityBillPaymentPlanFragment extends BaseFragment<UtilityPaymentPlansViewBinding>
        implements OnWRBillerNames {


    List<GetWRBillerNamesResponseC> wrBillerOperatorsList;
    WRBillerOperatorAdapter adapter;

    // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    public String billerCategoryId = "";
    public String countryCode = "AE";
    public String billerId;
    public String currency;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        wrBillerOperatorsList = new ArrayList<>();


        assert getArguments() != null;
        countryCode = getArguments().getString("countryCode");
        billerCategoryId = getArguments().getString("billerCategoryId");

        setSearchView();
        setupRecyclerView();
        getBillerName();
        binding.searchView.setVisibility(View.VISIBLE);
    }


    public void getBillerName() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetWRBillerNamesRequestC request = new GetWRBillerNamesRequestC();
        request.CountryCode = countryCode;
        request.BillerTypeId = billerCategoryId;
        String body = RestClient.makeGSONString(request);


        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);


        viewModel.GetWRBillerNames(aeRequest, HelperKeys.key,
                HelperKeys.secretkey).observe(getViewLifecycleOwner()
                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                        Utils.hideCustomProgressDialog();
                    } else {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);
                            Log.e("getBillerName: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRBillerNamesResponseC>>() {
                                }.getType();
                                List<GetWRBillerNamesResponseC> data = gson.fromJson(bodyy, type);
                                onBillerNamesResponse(data);
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
    public int getLayoutId() {
        return R.layout.utility_payment_plans_view;
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


    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new
                WRBillerOperatorAdapter(getContext(), wrBillerOperatorsList, this);
        binding.recyBillername.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recyBillername.setHasFixedSize(true);
        binding.recyBillername.setAdapter(adapter);
        binding.recyBillername.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBillerNamesResponse(List<GetWRBillerNamesResponseC> responses) {
        wrBillerOperatorsList = new ArrayList<>();
        wrBillerOperatorsList.clear();
        wrBillerOperatorsList.addAll(responses);
        setupRecyclerView();
        binding.recyBillername.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSelectBillerName(GetWRBillerNamesResponseC billerName) {
        getSKUBYID(billerName.Biller_ID);
    }


    public void getSKUBYID(String id) {
        Utils.showCustomProgressDialog(getContext(), false);
        SKUByBillerIDRequest request = new SKUByBillerIDRequest();
        request.CountryCode = countryCode;
        request.Biller_ID = id;
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), HelperKeys.TxnKey);
        Log.e("getSKUBYID: ", body);

        viewModel.getWASKUByBillerName(aeRequest, HelperKeys.key,
                HelperKeys.secretkey).observe(getViewLifecycleOwner()
                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                        Utils.hideCustomProgressDialog();
                    } else {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , HelperKeys.TxnKey);
                            Log.e("getBillerName: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillerSKUDataResponse>() {
                                }.getType();
                                BillerSKUDataResponse data = gson.fromJson(bodyy, type);
                                Bundle bundle = new Bundle();
                                bundle.putString("countryCode", countryCode);
                                bundle.putString("billerId", id);
                                bundle.putString("skuID", data.skuData.get(0).skuID);

                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                                .action_utilityBillPaymentPlanFragment_to_utilityPaymentAccountNoFragment,
                                        bundle);
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
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
