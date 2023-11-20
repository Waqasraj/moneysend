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
import moneylink.wallet.di.RequestHelper.GetYLocationRequest;
import moneylink.wallet.di.ResponseHelper.YLocationResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetYLocation;
import moneylink.wallet.utils.ProgressBar;

public class GetYLocationTask extends AsyncTask<GetYLocationRequest
        , Void, List<YLocationResponse>> {

    ProgressBar progressBar;
    Context context;
    OnGetYLocation onGetYLocation;


    public GetYLocationTask(Context context, OnGetYLocation onGetYLocation) {
        this.context = context;
        this.onGetYLocation = onGetYLocation;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<YLocationResponse> yLocationResponses) {
        super.onPostExecute(yLocationResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!yLocationResponses.isEmpty()) {
            onGetYLocation.onGetYLocations(yLocationResponses);
        }
    }

    @Override
    protected List<YLocationResponse> doInBackground(GetYLocationRequest... getYLocationRequests) {
        Log.e("doInBackground: ", getYLocationRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(getYLocationRequests[0].getXML(),
                SoapActionHelper.ACTION_Y_LOCATION_ACTION
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();

        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ", jsonObject.toString());
        List<YLocationResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetYLocationResponse").getJSONObject("GetYLocationResult");
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
                        YLocationResponse response = new YLocationResponse();
                        response.locationID = jsonObject.getInt("Location_ID");
                        response.locationName = jsonObject.getString("Location_Name");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("NewDataSet").getJSONObject("Table1");
                    YLocationResponse response = new YLocationResponse();
                    response.locationID = jsonObject.getInt("Location_ID");
                    response.locationName = jsonObject.getString("Location_Name");
                    responseList.add(response);
                }
            } else {
                onGetYLocation.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
