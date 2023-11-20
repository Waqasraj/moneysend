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
import moneylink.wallet.di.RequestHelper.GetWRBillerFieldsRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerFieldsResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnWRBillerFields;
import moneylink.wallet.utils.ProgressBar;

public class GetWRBillerFieldsTask extends AsyncTask
        <GetWRBillerFieldsRequest, Void, List<WRBillerFieldsResponse>> {

    ProgressBar progressBar;
    Context context;
    OnWRBillerFields fields;


    public GetWRBillerFieldsTask(Context context, OnWRBillerFields fields) {
        this.context = context;
        this.fields = fields;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<WRBillerFieldsResponse> wrBillerFieldsResponses) {
        super.onPostExecute(wrBillerFieldsResponses);
        progressBar.hideProgressDialogWithTitle();
        if (wrBillerFieldsResponses != null) {
         //   fields.onWRBillerField(wrBillerFieldsResponses);
        }
    }


    @Override
    protected List<WRBillerFieldsResponse> doInBackground(GetWRBillerFieldsRequest... getWRBillerFieldsRequests) {
        String xml = getWRBillerFieldsRequests[0]
                .getXML();
        Log.e("doInBackground: ", xml);
        String responseString = HTTPHelper.getResponse(getWRBillerFieldsRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WR_BILLER_FIELDS
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<WRBillerFieldsResponse> response = new ArrayList<>();

        try {
            Log.e("doInBackground:", jsonObject.toString());
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRBillerFieldsResponse").getJSONObject("GetWRBillerFieldsResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");

            if (responseCode.equals("101")) {

                jsonObject = jsonObject.getJSONObject("obj")
                        .getJSONObject("diffgr:diffgram")
                        .getJSONObject("GetBillerFields");

                JSONArray array = null;
                try {
                    array = jsonObject.getJSONArray("BillerField");
                } catch (Exception e) {
                    Log.e("doInBackground: ", e.getLocalizedMessage());
                }

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        response.add(getBillerField(array.getJSONObject(i)));
                    }
                } else {
                    response.add(getBillerField(jsonObject.getJSONObject("BillerField")));
                }


                //    delegate.onSuccessResponse("Beneficairy Added Successfully");
            } else {
                fields.onResponseMessage(message);
                response = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    private WRBillerFieldsResponse getBillerField(JSONObject jsonObject) throws JSONException {
        WRBillerFieldsResponse response = new WRBillerFieldsResponse();
        response.id = jsonObject.getInt("id");
        response.type = jsonObject.getString("type");
        response.minLength = jsonObject.getInt("minLength");
        response.maxLength = jsonObject.getInt("maxLength");

        try {
            response.fieldName = jsonObject.getString("fieldName");
        } catch (Exception exception) {
            Log.e("doInBackground: ", exception.getLocalizedMessage());
        }

        try {
            response.labelName = jsonObject.getString("labelName");
        } catch (Exception exception) {
            Log.e("doInBackground: ", exception.getLocalizedMessage());
        }

        try {
            response.description = jsonObject.getString("description");
        } catch (Exception exception) {
            Log.e("doInBackground: ", exception.getLocalizedMessage());
        }

        return response;
    }
}
