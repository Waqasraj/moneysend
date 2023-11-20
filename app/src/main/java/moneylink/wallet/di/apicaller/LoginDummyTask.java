package moneylink.wallet.di.apicaller;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.LoginRequest;
import moneylink.wallet.di.SoapActionHelper;

public class LoginDummyTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.emailAddress = "accounts@ritmantech.com";
        loginRequest.mobileNumber = "";
        loginRequest.password = "1234";
        loginRequest.languageId = "1";
        String responseString = HTTPHelper.getResponse(loginRequest.getXML(),
                SoapActionHelper.ACTION_GET_LOGIN
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String customerNo = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("LoginResponse").getJSONObject("LoginResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("doInBackground: ", jsonObject.toString());
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                customerNo = jsonObject.getString("CustomerNumber");
            } else {

                //    onMessage(message);
                customerNo = "";
            }
            //{"ResponseCode":100,"Description":"Request failed"}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
