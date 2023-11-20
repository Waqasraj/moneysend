package moneylink.wallet.di.apicaller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.SaveCardDetailsRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.utils.ProgressBar;

public class SaveCardDetailsTask extends AsyncTask
        <SaveCardDetailsRequest, Void, String> {

    ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    Context context;
    OnSuccessMessage onSuccessMessage;


    public SaveCardDetailsTask(Context context, OnSuccessMessage onSuccessMessage) {
        this.context = context;
        this.onSuccessMessage = onSuccessMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (!s.isEmpty()) {
            onSuccessMessage.onSuccess(s);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }

    @Override
    protected String doInBackground(SaveCardDetailsRequest... saveCardDetailsRequests) {
        Log.e("XML: ", saveCardDetailsRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(saveCardDetailsRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_SAVE_CARD_DETAILS
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String message = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("SaveCardDetailsResponse")
                    .getJSONObject("SaveCardDetailsResult");
            String responseCode = jsonObject.getString("ResponseCode");
            message = jsonObject.getString("Description");
            Log.e("doInBackground: ", message);
            if (responseCode.equals("101")) {

            } else {
                onSuccessMessage.onResponseMessage(message);
                message = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
