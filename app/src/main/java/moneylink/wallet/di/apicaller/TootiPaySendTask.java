package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.TootiPayRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSendTransferTootiPay;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;


public class TootiPaySendTask extends AsyncTask<TootiPayRequest, Integer, String> {

    ProgressBar progressBar;
    Context context;
    OnSendTransferTootiPay onMessageInterface;

    public TootiPaySendTask(Context context, OnSendTransferTootiPay onMessageInterface) {
        this.context = context;
        this.onMessageInterface = onMessageInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources()
                .getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (!s.isEmpty()) {
            onMessageInterface.onMoneyTransferSuccessfully(s);
        }
    }

    @Override
    protected String doInBackground(TootiPayRequest... tootiPayRequests) {
        String responseString = HTTPHelper.getResponse(tootiPayRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_SEND_TRANSFER
                , ApiHelper.METHOD_POST);
        Log.e("XML: ", tootiPayRequests[0]
                .getXML());
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("TotiPaySendResponse").getJSONObject("TotiPaySendResult");
            Log.e("JSON: ", jsonObject.toString());
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                Log.e("doInBackground: ", message);
                response = jsonObject.getString("TransactionNumber");
                //   onMessageInterface.onResponseMessage(message);
            } else {
                onMessageInterface.onResponseMessage(message);
                response = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}

