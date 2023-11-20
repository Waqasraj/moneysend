package moneylink.wallet.dialogs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.adapters.RelationListAdapter;
import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetRelationListRequest;
import moneylink.wallet.di.ResponseHelper.GetRelationListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSelectRelation;
import moneylink.wallet.utils.IsNetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class DialogRelationList extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectRelation {

    OnSelectRelation onSelectRelation;
    List<GetRelationListResponse> responseList;
    RelationListAdapter adapter;


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogRelationList(OnSelectRelation onSelectRelation) {
        this.onSelectRelation = onSelectRelation;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            new GetRelationListTask().execute();
        } else {
            onMessage(getString(R.string.no_internet));
            cancelUpload();
        }

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.plz_select_relation));

    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        // purposeList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                RelationListAdapter(responseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onSelectRelation(GetRelationListResponse relation) {
        onSelectRelation.onSelectRelation(relation);
        cancelUpload();
    }


    public class GetRelationListTask extends AsyncTask<Void, Void, List<GetRelationListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //    binding.transferPurposeList.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<GetRelationListResponse> responseList) {
            super.onPostExecute(responseList);
            // binding.transferPurposeList.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            setupRecyclerView();
        }

        @Override
        protected List<GetRelationListResponse> doInBackground(Void... voids) {
            GetRelationListRequest request = new GetRelationListRequest();
            request.languageId = getSessionManager().getlanguageselection();
            String responseString = HTTPHelper.getResponse(request
                            .getXML(),
                    SoapActionHelper.ACTION_GET_RELATION_SHIP_LIST
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();

            try {
                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetRelationListResponse").getJSONObject("GetRelationListResult");
                String responseCode = jsonObject.getString("ResponseCode");
                String message = jsonObject.getString("Description");
                if (responseCode.equals("101")) {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetRelationList");
                    JSONArray array = jsonObject.getJSONArray("tblRelationList");
                    //  purposeList.clear();
                    responseList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        GetRelationListResponse response = new GetRelationListResponse();
                        response.id = object.getInt("RelationID");
                        response.relationName = object.getString("RelationName");
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
