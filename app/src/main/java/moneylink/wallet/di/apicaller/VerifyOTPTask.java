package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.VerifyOTPRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnOTPVerified;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class VerifyOTPTask extends AsyncTask<VerifyOTPRequest, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnOTPVerified onResponse;

    public VerifyOTPTask(Context context, OnOTPVerified onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context,
                context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (s.equalsIgnoreCase("101")) {
            onResponse.onOTPVerified(true);
        }
    }

    @Override
    protected String doInBackground(VerifyOTPRequest... verifyOTPRequests) {
        Log.e("XML" , verifyOTPRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(verifyOTPRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_VERIFY_OTP
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("VerifyOTPResponse").getJSONObject("VerifyOTPResult");
            responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e( "doInBackground: ",jsonObject.toString() );
            if (responseCode.equals("101")) {

            } else {
                onResponse.onResponseMessage(message);
                responseCode = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
