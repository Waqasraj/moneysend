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
import moneylink.wallet.di.RequestHelper.GetWRBillerNamesRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerNamesResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnWRBillerNames;
import moneylink.wallet.utils.ProgressBar;

public class GetWRBillerNamesTask
        extends AsyncTask<GetWRBillerNamesRequest, Void, List<WRBillerNamesResponse>> {


    ProgressBar progressBar;
    Context context;
    OnWRBillerNames onWRBillerNames;


    public GetWRBillerNamesTask(Context context, OnWRBillerNames onWRBillerNames) {
        this.context = context;
        this.onWRBillerNames = onWRBillerNames;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<WRBillerNamesResponse> wrBillerNamesResponses) {
        super.onPostExecute(wrBillerNamesResponses);
        progressBar.hideProgressDialogWithTitle();
//        if (!wrBillerNamesResponses.isEmpty()) {
//            onWRBillerNames.onBillerNamesResponse(wrBillerNamesResponses);
//        }
    }

    @Override
    protected List<WRBillerNamesResponse> doInBackground(GetWRBillerNamesRequest... getWRBillerNamesRequests) {
        Log.e("doInBackground: ", getWRBillerNamesRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(getWRBillerNamesRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WR_BILLER_NAMES
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<WRBillerNamesResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRBillerNamesResponse").getJSONObject("GetWRBillerNamesResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerName").getJSONArray("WRBillerName");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        WRBillerNamesResponse response = new WRBillerNamesResponse();
                        response.billerName = jsonObject.getString("Biller_Name");
                        response.billerType = jsonObject.getString("Biller_Type");
                        response.billerId = jsonObject.getInt("Biller_ID");
                        response.billerTypeId = jsonObject.getInt("billerTypeId");
                        response.billerCategoryId = jsonObject.getInt("billerCategoryId");
                        response.countryCode = jsonObject.getString("countryCode");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerName").getJSONObject("WRBillerName");

                    WRBillerNamesResponse response = new WRBillerNamesResponse();
                    response.billerName = jsonObject.getString("Biller_Name");
                    response.billerType = jsonObject.getString("Biller_Type");
                    response.billerId = jsonObject.getInt("Biller_ID");
                    response.billerTypeId = jsonObject.getInt("billerTypeId");
                    response.billerCategoryId = jsonObject.getInt("billerCategoryId");
                    response.countryCode = jsonObject.getString("countryCode");
                    responseList.add(response);
                }
            } else {
                onWRBillerNames.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
