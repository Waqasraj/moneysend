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
import moneylink.wallet.di.RequestHelper.LoadVirtualCardRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.utils.ProgressBar;

public class LoadPrepaidCardTask extends AsyncTask
        <LoadVirtualCardRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onSuccessMessage;


    public LoadPrepaidCardTask(Context context, OnSuccessMessage onSuccessMessage) {
        this.context = context;
        this.onSuccessMessage = onSuccessMessage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
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
    protected String doInBackground(LoadVirtualCardRequest... loadVirtualCardRequests) {
        Log.e("XML: ", loadVirtualCardRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(loadVirtualCardRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_LOAD_VIRTUAL_CARD
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        Log.e("doInBackground: ", xmlToJson.toJson().toString());
        JSONObject jsonObject = xmlToJson.toJson();
        String referenceNo = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("LoadVirtualCardResponse").getJSONObject("LoadVirtualCardResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("doInBackground: ", jsonObject.toString());
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                referenceNo = jsonObject.getString("Description");
            } else {
                onSuccessMessage.onResponseMessage(message);
                referenceNo = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return referenceNo;
    }
}
