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
import moneylink.wallet.di.RequestHelper.GetYCityRequest;
import moneylink.wallet.di.ResponseHelper.YCityResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetYCities;
import moneylink.wallet.utils.ProgressBar;

public class GetYCityTask extends AsyncTask
        <GetYCityRequest, Void, List<YCityResponse>> {


    ProgressBar progressBar;
    Context context;
    OnGetYCities onGetYCities;


    public GetYCityTask(Context context, OnGetYCities onGetYCities) {
        this.context = context;
        this.onGetYCities = onGetYCities;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(List<YCityResponse> yCityResponses) {
        super.onPostExecute(yCityResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!yCityResponses.isEmpty()) {
            onGetYCities.onGetCities(yCityResponses);
        }
    }

    @Override
    protected List<YCityResponse> doInBackground(GetYCityRequest... getYCityRequests) {
        Log.e("doInBackground: ",getYCityRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(getYCityRequests[0].getXML(),
                SoapActionHelper.ACTION_Y_CITY_ACTION
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ",jsonObject.toString());
        List<YCityResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetYCityResponse").getJSONObject("GetYCityResult");
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
                        YCityResponse response = new YCityResponse();
                        response.cityId = jsonObject.getInt("City_ID");
                        response.cityName = jsonObject.getString("City_Name");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("NewDataSet").getJSONObject("Table1");
                    YCityResponse response = new YCityResponse();
                    response.cityId = jsonObject.getInt("City_ID");
                    response.cityName = jsonObject.getString("City_Name");
                    responseList.add(response);
                }
            } else {
                onGetYCities.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
