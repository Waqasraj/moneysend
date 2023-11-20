package moneylink.wallet.dialogs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.adapters.CountryListAdapter;
import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetCountryListRequest;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.utils.CountryParser;
import moneylink.wallet.interfaces.OnSelectCountry;

import moneylink.wallet.utils.IsNetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class DialogCountry extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectCountry {


    OnSelectCountry onSelectCountry;
    List<GetCountryListResponse> countryListResponses;
    CountryListAdapter adapter;

    Integer countryType = -1;
    boolean isCurrency = false;

    boolean isShowShortCode = true;
    boolean isWallet = false;

    boolean isNationality = false;

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogCountry(OnSelectCountry onSelectCountry) {
        this.onSelectCountry = onSelectCountry;
    }

    public DialogCountry(OnSelectCountry onSelectCountry, boolean isNationality, boolean isShowShortCode) {
        this.onSelectCountry = onSelectCountry;
        this.isNationality = isNationality;
        this.isShowShortCode = isShowShortCode;
    }


    public DialogCountry(Integer countryType, OnSelectCountry onSelectCountry) {
        this.countryType = countryType;
        this.onSelectCountry = onSelectCountry;
    }

    public DialogCountry(Integer countryType, boolean isShowShortCode, OnSelectCountry onSelectCountry) {
        this.countryType = countryType;
        this.onSelectCountry = onSelectCountry;
        this.isShowShortCode = isShowShortCode;
    }


    public DialogCountry(Integer countryType, OnSelectCountry onSelectCountry, boolean isCurrency) {
        this.countryType = countryType;
        this.onSelectCountry = onSelectCountry;
        this.isCurrency = isCurrency;
    }

    public DialogCountry(boolean isCurrency, boolean isWallet, OnSelectCountry onSelectCountry) {
        this.onSelectCountry = onSelectCountry;
        this.isCurrency = isCurrency;
        this.isWallet = isWallet;
    }


    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setSearchView();
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            new GetCountryListTask().execute();
        } else {
            onMessage(getString(R.string.no_internet));
            cancelUpload();
        }
        if (isCurrency) {
            binding.titleOfPage.setText(getString(R.string.select_currecny));
        } else {
            binding.titleOfPage.setText(getString(R.string.select_country));
        }


        if (isNationality) {
            binding.titleOfPage.setText(getString(R.string.select_nationlity));
        }

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });
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
        // purposeList = new ArrayList<>();
        if (countryType > 0) {
            if (countryType == CountryParser.RECEIVE) {
                countryListResponses = CountryParser.getReceivingCountries(countryListResponses);
            } else if (countryType == CountryParser.SEND) {
                countryListResponses = CountryParser.getSendingCountries(countryListResponses);
            }
        }

        if (countryListResponses != null) {
            Collections.sort(countryListResponses, (o1, o2) ->
                    o1.countryName.compareToIgnoreCase(o2.countryName));
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                CountryListAdapter(getContext(), countryListResponses, this, isWallet, isCurrency, isShowShortCode);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        onSelectCountry.onSelectCountry(country);
        cancelUpload();
    }


    public class GetCountryListTask extends AsyncTask<Void, Void, List<GetCountryListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   binding.transferPurposeList.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(List<GetCountryListResponse> responseList) {
            super.onPostExecute(responseList);
            //  binding.transferPurposeList.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.VISIBLE);
            if (responseList != null) {
                setupRecyclerView();
            }
        }

        @Override
        protected List<GetCountryListResponse> doInBackground(Void... voids) {
            GetCountryListRequest request = new GetCountryListRequest();
            request.languageId = getSessionManager().getlanguageselection();
            Log.e("doInBackground: ", request.getXML());
            String responseString = HTTPHelper.getResponse(request
                            .getXML(),
                    SoapActionHelper.ACTION_GET_COUNTRY_LIST
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();
            String message = "server error";
            try {
                assert jsonObject != null;
                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetCountryListResponse").getJSONObject("GetCountryListResult");
                String responseCode = jsonObject.getString("ResponseCode");
                message = jsonObject.getString("Description");
                if (responseCode.equals("101")) {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetCountryList");
                    JSONArray array = jsonObject.getJSONArray("tblCountryList");
                    //  purposeList.clear();
                    countryListResponses = new ArrayList<>();
                    Log.e("doInBackground: ", jsonObject.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        GetCountryListResponse response = new GetCountryListResponse();
                        response.id = object.getInt("CountryID");
                        response.countryName = object.getString("CountryName");
                        response.countryShortName = object.getString("CountryShortName");
                        response.isActive = object.getBoolean("Active");
                        response.currencyShortName = object.getString("CurrencyShortName");
                        try {
                            response.countryCode = object.getString("CountryCode");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            response.imageURL = object.getString("Image_URL");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        //  response.imageURL = object.getString("Image_URL");
                        response.countryType = object.getInt("CountryType");
                        response.countryOrigin = object.getInt("Country_Origine");
                        countryListResponses.add(response);
                    }

                } else {
                    onMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return countryListResponses;
        }
    }
}
