package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetYBranchRequest;
import moneylink.wallet.di.ResponseHelper.YBranchResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetYBranch;
import moneylink.wallet.utils.ProgressBar;

public class GetYBranchTask extends AsyncTask<GetYBranchRequest
         , Void , List<YBranchResponse>> {

    ProgressBar progressBar;
    Context context;
    OnGetYBranch onGetYBranch;


    public GetYBranchTask(Context context, OnGetYBranch onGetYBranch) {
        this.context = context;
        this.onGetYBranch = onGetYBranch;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<YBranchResponse> responses) {
        super.onPostExecute(responses);
        progressBar.hideProgressDialogWithTitle();
        if (!responses.isEmpty()) {
            onGetYBranch.onGetYBranch(responses);
        }
    }

    @Override
    protected List<YBranchResponse> doInBackground(GetYBranchRequest... getYBranchRequests) {
        Log.e("doInBackground: ",getYBranchRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(getYBranchRequests[0].getXML(),
                SoapActionHelper.ACTION_Y_BRANCH_ACTION
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ",jsonObject.toString());
        List<YBranchResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetYBranchesResponse").getJSONObject("GetYBranchesResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("NewDataSet").getJSONArray("Table1");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        YBranchResponse response = new YBranchResponse();
                        response.branchID = jsonObject.getInt("Branch_ID");
                        response.branchName = jsonObject.getString("Branch_Name");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("NewDataSet").getJSONObject("Table1");
                    YBranchResponse response = new YBranchResponse();
                    response.branchID = jsonObject.getInt("Branch_ID");
                    response.branchName = jsonObject.getString("Branch_Name");
                    responseList.add(response);
                }
            } else {
                onGetYBranch.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
