package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetIDTypeRequest;
import moneylink.wallet.di.ResponseHelper.GetIdTypeResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSelectIdType;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class GetIdTypeTask extends AsyncTask<GetIDTypeRequest, Integer, List<GetIdTypeResponse>> {

    ProgressBar progressBar;
    public Context context;
    OnSelectIdType delegate;

    public GetIdTypeTask(Context context, OnSelectIdType delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected List<GetIdTypeResponse> doInBackground(GetIDTypeRequest... getIDTypeRequests) {
        String responseString = HTTPHelper.getResponse(getIDTypeRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_ID
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<GetIdTypeResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetIDTypeResponse").getJSONObject("GetIDTypeResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                //   response.beneficiaryNo = jsonObject.getString("BeneficiaryNo");
                JSONArray array = null;
                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetIDType").getJSONArray("tblIDTypeList");
                } catch (Exception e) {
                    Log.e("doInBackground: ", e.getLocalizedMessage());
                }

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetIdTypeResponse response = new GetIdTypeResponse();
                        response.id = jsonObject.getInt("IDType_ID");
                        response.idTypeName = jsonObject.getString("IDType");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetIDType").getJSONObject("tblIDTypeList");
                    GetIdTypeResponse response = new GetIdTypeResponse();
                    response.id = jsonObject.getInt("IDType_ID");
                    response.idTypeName = jsonObject.getString("IDType");
                    responseList.add(response);
                }

            } else {
                delegate.onResponseMessage(message);
                responseList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, "Please Wait.....");
    }

    @Override
    protected void onPostExecute(List<GetIdTypeResponse> getIdTypeResponses) {
        super.onPostExecute(getIdTypeResponses);
        progressBar.hideProgressDialogWithTitle();
        if (getIdTypeResponses != null) {
            delegate.onGetIdTypes(getIdTypeResponses);
        }
    }

}
