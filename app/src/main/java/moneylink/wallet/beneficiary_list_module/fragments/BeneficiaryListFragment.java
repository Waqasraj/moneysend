package moneylink.wallet.beneficiary_list_module.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.R;
import moneylink.wallet.adapters.CustomerBeneficiaryListAdapter;
import moneylink.wallet.databinding.FragmentBeneficiaryListBinding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.BeneficiaryListRequest;
import moneylink.wallet.di.ResponseHelper.GetBeneficiaryListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnSelectBeneficiary;
import moneylink.wallet.utils.IsNetworkConnection;

public class BeneficiaryListFragment extends BaseFragment<FragmentBeneficiaryListBinding>
        implements OnSelectBeneficiary {

    public List<GetBeneficiaryListResponse> list = null;
    CustomerBeneficiaryListAdapter adapter;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        list = new ArrayList<>();
        setAccountsRecyclerView();
        setSearchView();
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetBeneficiaryListTask task = new GetBeneficiaryListTask(getContext());
            task.execute(getSessionManager().getCustomerNo());
        } else {
            onMessage(getString(R.string.no_internet));
        }

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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_beneficiary_list;
    }

    @Override
    public void showProgress() {
        super.showProgress();
        binding.mainView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        super.hideProgress();
        binding.mainView.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {

        adapter = new
                CustomerBeneficiaryListAdapter(list, this , true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.beneficiaryRecyclerView.setLayoutManager(mLayoutManager);
        binding.beneficiaryRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onSelectBeneficiary(GetBeneficiaryListResponse response) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("bene_details", response);
//        if (response.paymentMode.equalsIgnoreCase("bank")) {
//            Navigation.findNavController(getView())
//                    .navigate(R.id.action_beneficiaryListFragment_to_showBankBeneficiaryFragment
//                            , bundle);
//        } else if (response.paymentMode.equalsIgnoreCase("cash")) {
//            Navigation.findNavController(getView())
//                    .navigate(R.id.action_beneficiaryListFragment_to_showCashBeneficiaryFragment
//                            , bundle);
//        }
    }


    public class GetBeneficiaryListTask extends AsyncTask<String, Integer, List<GetBeneficiaryListResponse>> {

        Context context;

        public GetBeneficiaryListTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.mainView.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            // ProgressBar.showProgressDialogWithTitle(context, "Please Wait..");
        }

        @Override
        protected List<GetBeneficiaryListResponse> doInBackground(String... strings) {
            try {
                BeneficiaryListRequest request = new BeneficiaryListRequest();
                request.languageId = getSessionManager().getlanguageselection();
                request.customerNo = strings[0];
                String responseString = HTTPHelper.getResponse(request
                                .getXML(), SoapActionHelper.ACTION_GET_BENEFICIARY_LIST
                        , ApiHelper.METHOD_POST);
                XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
                // convert to a JSONObject
                JSONObject jsonObject = xmlToJson.toJson();

                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetBeneficiaryListResponse").getJSONObject("GetBeneficiaryListResult");
                Log.e("bene response", jsonObject.toString());
                String responseCode = jsonObject.getString("ResponseCode");
                String message = jsonObject.getString("Description");
                list = new ArrayList<>();
                if (responseCode.equals("101")) {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("BeneficiaryList");
                    Log.e("doInBackground: ", jsonObject.toString());
                    JSONArray array = null;
                    try {
                        array = jsonObject.getJSONArray("tblBeneficiaryList");
                    } catch (Exception e) {
                        Log.e("doInBackground: ", e.getLocalizedMessage());
                    }

                    list.clear();
                    list = new ArrayList<>();
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            GetBeneficiaryListResponse response = new GetBeneficiaryListResponse();
                            JSONObject object = array.getJSONObject(i);
                            response.beneficiaryNumber = object.getString("BeneficiaryNumber");
                            response.customerNo = object.getString("CustomerNo");
                            response.beneficiaryNo = object.getString("BeneficiaryNo");
                            response.firstName = object.getString("FirstName");
                            response.middleName = object.getString("MiddleName");
                            response.lastName = object.getString("LastName");
                            response.address = object.getString("Address");
                            response.telephone = object.getString("Telephone");
                            response.nationality = object.getString("Nationality");
                            response.payOutCurrency = object.getString("PayOutCurrency");
                            response.paymentMode = object.getString("PaymentMode");
                            response.paymentBranchCode = object.getString("PaymentBranchCode");

                            response.isActive = object.getString("IsActive");
                            response.purposeCode = object.getString("PurposeCode");
                            response.purposeOfTransfer = object.getString("PurposeOfTransfer");
                            response.payoutAgent = object.getString("PayoutAgent");
                            response.payOutCountryCode = object.getString("PayOutCountryCode");
                            response.customerRelation = object.getString("CustomerRelation");
                            response.payoutBranchName = object.getString("PayoutBranchName");
                            if (response.paymentMode.equalsIgnoreCase("bank")) {
                                response.bankName = object.getString("BankName");
                                response.bankCountry = object.getString("BankCountry");
                                response.branchName = object.getString("BranchName");
                                response.bankCode = object.getString("BankCode");
                                response.accountNumber = object.getString("AccountNumber");
                            } else {
                                response.payoutBranchName = object.getString("PayoutBranchName");
                            }

                            list.add(response);
                        }
                    } else {
                        jsonObject = jsonObject.getJSONObject("tblBeneficiaryList");
                        GetBeneficiaryListResponse response = new GetBeneficiaryListResponse();

                        response.customerNo = jsonObject.getString("CustomerNo");
                        response.beneficiaryNo = jsonObject.getString("BeneficiaryNo");
                        response.firstName = jsonObject.getString("FirstName");
                        response.middleName = jsonObject.getString("MiddleName");
                        response.lastName = jsonObject.getString("LastName");
                        response.address = jsonObject.getString("Address");
                        response.telephone = jsonObject.getString("Telephone");
                        response.nationality = jsonObject.getString("Nationality");
                        response.payOutCurrency = jsonObject.getString("PayOutCurrency");
                        response.paymentMode = jsonObject.getString("PaymentMode");
                        response.paymentBranchCode = jsonObject.getString("PaymentBranchCode");

                        response.isActive = jsonObject.getString("IsActive");
                        response.purposeCode = jsonObject.getString("PurposeCode");
                        response.purposeOfTransfer = jsonObject.getString("PurposeOfTransfer");
                        response.payoutAgent = jsonObject.getString("PayoutAgent");
                        response.payOutCountryCode = jsonObject.getString("PayOutCountryCode");
                        response.customerRelation = jsonObject.getString("CustomerRelation");
                        response.beneficiaryNumber = jsonObject.getString("BeneficiaryNumber");
                        if (response.paymentMode.equalsIgnoreCase("bank")) {
                            response.bankName = jsonObject.getString("BankName");
                            response.bankCountry = jsonObject.getString("BankCountry");
                            response.branchName = jsonObject.getString("BranchName");
                            response.bankCode = jsonObject.getString("BankCode");
                            response.accountNumber = jsonObject.getString("AccountNumber");
                        } else {
                            response.payoutBranchName = jsonObject.getString("PayoutBranchName");
                        }
                        list.add(response);
                    }
                    // onSuccess();
                    // adapter.notifyDataSetChanged();
                } else {
                    onMessage(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }


        @Override
        protected void onPostExecute(List<GetBeneficiaryListResponse> getBeneficiaryListTasks) {
            super.onPostExecute(getBeneficiaryListTasks);
            binding.mainView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            if (getBeneficiaryListTasks != null && !getBeneficiaryListTasks.isEmpty()) {
                setAccountsRecyclerView();
            } else if (getBeneficiaryListTasks != null) {
                if (list.isEmpty()) {
                    handleViews();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void handleViews() {
        if (list.isEmpty()) {
            binding.searchView.setVisibility(View.GONE);
            binding.titleSlectBene.setVisibility(View.GONE);
        }
    }
}