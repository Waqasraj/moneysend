package moneylink.wallet.dialogs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.adapters.TransferPurposeAdapter;
import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.PurposeOfTransferListRequest;
import moneylink.wallet.di.ResponseHelper.PurposeOfTransferListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSelectTransferPurpose;
import moneylink.wallet.utils.IsNetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class DialogTransferPurpose extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectTransferPurpose {

    OnSelectTransferPurpose onSelectTransferPurpose;
    List<PurposeOfTransferListResponse> purposeList;
    TransferPurposeAdapter adapter;

    String countryShortName = "GBR";

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogTransferPurpose(OnSelectTransferPurpose onSelectTransferPurpose) {
        this.countryShortName = countryShortName;
        this.onSelectTransferPurpose = onSelectTransferPurpose;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        //  setupRecyclerView();
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            new GetTransferPurposeTask().execute();
        } else {
            onMessage(getString(R.string.no_internet));
            cancelUpload();
        }

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_the_purpose_txt));
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        // purposeList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                TransferPurposeAdapter(purposeList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onSelectTransferPurpose(PurposeOfTransferListResponse response) {
        onSelectTransferPurpose.onSelectTransferPurpose(response);
        cancelUpload();
    }


    public class GetTransferPurposeTask extends AsyncTask<String, Integer, List<PurposeOfTransferListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.transferPurposeList.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<PurposeOfTransferListResponse> doInBackground(String... strings) {
            PurposeOfTransferListRequest request = new PurposeOfTransferListRequest();
            request.languageId = getSessionManager().getlanguageselection();
            String responseString = HTTPHelper.getResponse(request
                            .getXML(countryShortName),
                    SoapActionHelper.ACTION_GetPurposeOfTransferResult
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();

            try {
                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetPurposeOfTransferListResponse").getJSONObject("GetPurposeOfTransferListResult");
                String responseCode = jsonObject.getString("ResponseCode");
                String message = jsonObject.getString("Description");
                if (responseCode.equals("101")) {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetPurposeList");
                    JSONArray array = jsonObject.getJSONArray("tblPurposeList");
                    //  purposeList.clear();
                    purposeList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        PurposeOfTransferListResponse response = new PurposeOfTransferListResponse();
                        response.purposeOfTransferID = object.getInt("PurposeOfTransferID");
                        response.purposeOfTransfer = object.getString("PurposeOfTranfer");
                        purposeList.add(response);
                    }

                } else {
                    onMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return purposeList;
        }


        @Override
        protected void onPostExecute(List<PurposeOfTransferListResponse> purposeOfTransferListResponses) {
            super.onPostExecute(purposeOfTransferListResponses);
            binding.transferPurposeList.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            setupRecyclerView();
        }
    }
}
