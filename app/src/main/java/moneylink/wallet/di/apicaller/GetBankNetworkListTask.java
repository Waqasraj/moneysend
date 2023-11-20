package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetBankNetworkListRequest;

import moneylink.wallet.di.ResponseHelper.GetBankNetworkListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetBankNetworkListTaskInterface;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class GetBankNetworkListTask extends AsyncTask<GetBankNetworkListRequest, Void
        , List<GetBankNetworkListResponse>> {

    ProgressBar progressBar;
    public Context context;

    OnGetBankNetworkListTaskInterface delegate;


    public GetBankNetworkListTask(Context context, OnGetBankNetworkListTaskInterface delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources()
                .getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<GetBankNetworkListResponse> getBankNetworkListResponse) {
        super.onPostExecute(getBankNetworkListResponse);
        progressBar.hideProgressDialogWithTitle();
        if (getBankNetworkListResponse != null && !getBankNetworkListResponse.isEmpty()) {
            delegate.onSuccess(getBankNetworkListResponse);
        }

    }

    @Override
    protected List<GetBankNetworkListResponse> doInBackground(GetBankNetworkListRequest... getBankNetworkListRequests) {
        String responseString = HTTPHelper.getResponse(getBankNetworkListRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_BANK_NETWORK_LIST
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<GetBankNetworkListResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("GetBankNetworkListResponse").getJSONObject("GetBankNetworkListResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("BankNetworkList").getJSONArray("tblBankNetworkList");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetBankNetworkListResponse response = new GetBankNetworkListResponse();
                        response.bankName = jsonObject.getString("BankName");
                        response.branchName = jsonObject.getString("BranchName");
                        response.bankAddress = jsonObject.getString("BankAddress");
                        response.bankCode = jsonObject.getString("BankCode");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("BankNetworkList").getJSONObject("tblBankNetworkList");
                    Log.e("doInBackground: ", jsonObject.toString());
                    GetBankNetworkListResponse response = new GetBankNetworkListResponse();
                    response.bankName = jsonObject.getString("BankName");
                    response.branchName = jsonObject.getString("BranchName");
                    response.bankAddress = jsonObject.getString("BankAddress");
                    response.bankCode = jsonObject.getString("BankCode");
                    responseList.add(response);
                }

            } else {
                delegate.onMessageResponse(message);
                responseList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
