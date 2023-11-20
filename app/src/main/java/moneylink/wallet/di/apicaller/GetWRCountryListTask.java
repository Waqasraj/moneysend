package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.R;
import moneylink.wallet.di.RequestHelper.GetWRCountryListRequest;
import moneylink.wallet.di.ResponseHelper.WRCountryListResponse;
import moneylink.wallet.interfaces.OnWRCountryList;
import moneylink.wallet.utils.ProgressBar;

public class GetWRCountryListTask extends
        AsyncTask<GetWRCountryListRequest, Void, List<WRCountryListResponse>> {

    ProgressBar progressBar;
    Context context;
    OnWRCountryList onWRCountryList;


    public GetWRCountryListTask(Context context, OnWRCountryList onWRCountryList) {
        this.context = context;
        this.onWRCountryList = onWRCountryList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<WRCountryListResponse> wrCountryListResponses) {
        super.onPostExecute(wrCountryListResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!wrCountryListResponses.isEmpty()) {
           // onWRCountryList.onWRCountryList(wrCountryListResponses);
        }
    }

    @Override
    protected List<WRCountryListResponse> doInBackground(GetWRCountryListRequest... getWRCountryListRequests) {

        String responseString = "";
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<WRCountryListResponse> countryList = new ArrayList<>();
        try {
            assert jsonObject != null;
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRCountryListResponse").getJSONObject("GetWRCountryListResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {

                JSONArray array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                        .getJSONObject("GetWRCountryList").getJSONArray("WRCountryList");
                //  purposeList.clear();

                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    WRCountryListResponse response = new WRCountryListResponse();
//                    response.countryShortName = object.getString("Country_Code");
//                    response.countryName = object.getString("Country_Name");
//                    response.countryCurrency = object.getString("Currency_ShortName");
//
//                    try {
//                        response.countryCode = object.getString("ISDCode");
//                    } catch (Exception e) {
//                        Log.e("doInBackground: ", e.getLocalizedMessage());
//                    }
//
//                    try {
//                        response.image_URL = object.getString("URL");
//                    } catch (Exception e) {
//                        Log.e("doInBackground: ", e.getLocalizedMessage());
//                    }
//
//                    countryList.add(response);
                }

            } else {
                onWRCountryList.onResponseMessage(message);
                countryList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countryList;
    }
}
