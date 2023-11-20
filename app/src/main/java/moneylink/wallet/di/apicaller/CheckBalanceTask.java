package moneylink.wallet.di.apicaller;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.CheckBalanceRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSuccessMessage;


public class CheckBalanceTask extends AsyncTask
        <CheckBalanceRequest, Void, String> {


    OnSuccessMessage onResponse;

    public CheckBalanceTask(OnSuccessMessage onResponse) {
        this.onResponse = onResponse;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!s.isEmpty()) {
            onResponse.onSuccess(s);
        } else {
            onResponse.onResponseMessage("");
        }
    }


    @Override
    protected String doInBackground(CheckBalanceRequest... checkBalanceRequests) {
        Log.e("doInBackground: ", checkBalanceRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(checkBalanceRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_CHECK_BALANCE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ", jsonObject.toString());
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("CheckBalanceResponse").getJSONObject("CheckBalanceResult");
            responseCode = jsonObject.getString("ResponseCode");

            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                responseCode = jsonObject.getString("monthly_balance");
            } else {
                onResponse.onResponseMessage(message);
                //    onMessage(message);
                responseCode = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            responseCode = "";
        }
        return responseCode;
    }
}
