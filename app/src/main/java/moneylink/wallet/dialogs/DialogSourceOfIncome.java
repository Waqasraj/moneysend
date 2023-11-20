package moneylink.wallet.dialogs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.adapters.SourceOfIncomeListAdapter;
import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetSourceIncomeListRequest;
import moneylink.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSelectSourceOfIncome;
import moneylink.wallet.utils.IsNetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class DialogSourceOfIncome extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectSourceOfIncome {

    OnSelectSourceOfIncome onSelectSourceOfIncome;
    SourceOfIncomeListAdapter adapter;
    List<GetSourceOfIncomeListResponse> responseList;


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogSourceOfIncome(OnSelectSourceOfIncome onSelectSourceOfIncome) {
        this.onSelectSourceOfIncome = onSelectSourceOfIncome;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            new GetSourceOfIncomeListTask().execute();
        } else {
            onMessage(getString(R.string.no_internet));
            cancelUpload();
        }

        binding.closeDialog.setOnClickListener(v->{
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_the_purpose_txt));

    }

    @Override
    public void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response) {
        onSelectSourceOfIncome.onSelectSourceOfIncome(response);
        cancelUpload();
    }




    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        // purposeList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                SourceOfIncomeListAdapter(responseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }


    public class GetSourceOfIncomeListTask extends AsyncTask<Void, Void, List<GetSourceOfIncomeListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        //    binding.transferPurposeList.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<GetSourceOfIncomeListResponse> responseList) {
            super.onPostExecute(responseList);
          //  binding.transferPurposeList.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            setupRecyclerView();
        }


        @Override
        protected List<GetSourceOfIncomeListResponse> doInBackground(Void... voids) {
            GetSourceIncomeListRequest request = new GetSourceIncomeListRequest();
            request.languageId = getSessionManager().getlanguageselection();
            String responseString = HTTPHelper.getResponse(request
                            .getXML(),
                    SoapActionHelper.ACTION_GET_SOURCE_OF_INCOME
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();

            try {
                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetSourceOfIncomeListResponse").getJSONObject("GetSourceOfIncomeListResult");
                String responseCode = jsonObject.getString("ResponseCode");
                String message = jsonObject.getString("Description");
                if (responseCode.equals("101")) {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetSourceOfIncomeList");
                    JSONArray array = jsonObject.getJSONArray("tblGetSourceOfIncomeList");
                    //  purposeList.clear();
                    responseList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        GetSourceOfIncomeListResponse response = new GetSourceOfIncomeListResponse();
                        response.id = object.getInt("SourceOfIncomeID");
                        response.incomeName = object.getString("SourceOfIncomeName");
                        responseList.add(response);
                    }

                } else {
                    onMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return responseList;
        }
    }
}
