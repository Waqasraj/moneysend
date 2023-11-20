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
import moneylink.wallet.di.RequestHelper.GetWRBillerCategoryRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerCategoryResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnWRBillerCategoryResponse;
import moneylink.wallet.utils.ProgressBar;

public class GetWRBillerCategoryTask extends AsyncTask
        <GetWRBillerCategoryRequest, Void, List<WRBillerCategoryResponse>> {

    ProgressBar progressBar;
    Context context;
    OnWRBillerCategoryResponse onResponse;

    public GetWRBillerCategoryTask(Context context, OnWRBillerCategoryResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected List<WRBillerCategoryResponse> doInBackground(GetWRBillerCategoryRequest... getWRBillerCategoryRequests) {

        String responseString = HTTPHelper.getResponse(getWRBillerCategoryRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_WR_BILLER_CATEGORY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<WRBillerCategoryResponse> responseList = new ArrayList<>();
        try {
            assert jsonObject != null;
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRBillerCategoryResponse").getJSONObject("GetWRBillerCategoryResult");
            Log.e("doInBackground: ", jsonObject.toString());
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerCategory").getJSONArray("WRBillerCategory");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        WRBillerCategoryResponse response = new WRBillerCategoryResponse();
                        response.id = jsonObject.getString("ID");
                        response.name = jsonObject.getString("Name");
                        try {
                            response.imageURL = jsonObject.getString("IconURL");
                        } catch (Exception e) {
                            Log.e("doInBackground: ", e.getLocalizedMessage());
                        }


                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerCategory").getJSONObject("WRBillerCategory");

                    WRBillerCategoryResponse response = new WRBillerCategoryResponse();
                    response.id = jsonObject.getString("ID");
                    response.name = jsonObject.getString("Name");
                    try {
                        response.imageURL = jsonObject.getString("IconURL");
                    } catch (Exception e) {
                        Log.e("doInBackground: ", e.getLocalizedMessage());
                    }
                    responseList.add(response);
                }

            } else {
                onResponse.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }


    @Override
    protected void onPostExecute(List<WRBillerCategoryResponse> responseList) {
        super.onPostExecute(responseList);
        progressBar.hideProgressDialogWithTitle();
//        if (!responseList.isEmpty()) {
//            onResponse.onWRCategory(responseList);
//        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }
}
