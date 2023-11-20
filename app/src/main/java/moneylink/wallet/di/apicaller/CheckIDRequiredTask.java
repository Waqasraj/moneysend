package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.CheckIDRequiredRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.IsIDRequired;
import moneylink.wallet.utils.ProgressBar;

public class CheckIDRequiredTask extends AsyncTask<CheckIDRequiredRequest, Void, Boolean> {

    ProgressBar progressBar;
    Context context;
    IsIDRequired onResponse;

    public CheckIDRequiredTask(Context context, IsIDRequired onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources()
                .getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        onResponse.isIDRequired(s);

    }

    @Override
    protected Boolean doInBackground(CheckIDRequiredRequest... changePins) {
        boolean message = false;
        String responseString = HTTPHelper.getResponse(changePins[0]
                        .getXML(),
                SoapActionHelper.ACTION_CheckIDRequired
                , ApiHelper.METHOD_POST);
        Log.e("req: ", changePins[0]
                .getXML());
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        // convert to a JSONObject
        Log.e("doInBackground: ", jsonObject.toString());
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("CheckIDRequiredResponse").getJSONObject("CheckIDRequiredResult");
            responseCode = jsonObject.getString("ResponseCode");

            if (responseCode.equals("101")) {
                message = jsonObject.getBoolean("IDRequired");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
