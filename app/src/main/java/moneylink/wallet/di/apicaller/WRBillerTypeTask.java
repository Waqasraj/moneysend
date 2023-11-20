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
import moneylink.wallet.di.RequestHelper.GetWRBillerTypeRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerTypeResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnWRBillerType;
import moneylink.wallet.utils.ProgressBar;

public class WRBillerTypeTask extends AsyncTask<GetWRBillerTypeRequest, Void
        , List<WRBillerTypeResponse>> {

    ProgressBar progressBar;
    Context context;
    OnWRBillerType onWRBillerType;


    public WRBillerTypeTask(Context context, OnWRBillerType onWRBillerType) {
        this.context = context;
        this.onWRBillerType = onWRBillerType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<WRBillerTypeResponse> wrBillerTypeResponses) {
        super.onPostExecute(wrBillerTypeResponses);
        progressBar.hideProgressDialogWithTitle();

        if (!wrBillerTypeResponses.isEmpty()) {
            onWRBillerType.onBillerTypeList(wrBillerTypeResponses);
        }
    }

    @Override
    protected List<WRBillerTypeResponse> doInBackground(GetWRBillerTypeRequest... getWRBillerTypeRequests) {
        Log.e("doInBackground: ",getWRBillerTypeRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(getWRBillerTypeRequests[0].getXML(),
                SoapActionHelper.ACTION_GET_WR_BILLER_TYPE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<WRBillerTypeResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRBillerTypeResponse").getJSONObject("GetWRBillerTypeResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerType").getJSONArray("WRBillerType");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        WRBillerTypeResponse response = new WRBillerTypeResponse();
                        response.id = jsonObject.getString("ID");
                        response.billerName = jsonObject.getString("Name");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerType").getJSONObject("WRBillerType");
                    WRBillerTypeResponse response = new WRBillerTypeResponse();
                    response.id = jsonObject.getString("ID");
                    response.billerName = jsonObject.getString("Name");
                    responseList.add(response);
                }
            } else {
                onWRBillerType.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
