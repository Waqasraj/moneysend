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
import moneylink.wallet.di.RequestHelper.WalletToWalletTransferRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnResponse;
import moneylink.wallet.utils.ProgressBar;

public class WalletToWalletTransferTask extends AsyncTask<WalletToWalletTransferRequest , Void
         , String> {


    ProgressBar progressBar;
    Context context;
    OnResponse onResponse;

    public WalletToWalletTransferTask(Context context, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context , context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();;
        if(!s.isEmpty()){
            onResponse.onSuccess();
        }
    }

    @Override
    protected String doInBackground(WalletToWalletTransferRequest... walletToWalletTransferRequests) {
        Log.e("XML: ", walletToWalletTransferRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(walletToWalletTransferRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WALLET_TO_WALLET
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("WalletToWalletTransferResponse").getJSONObject("WalletToWalletTransferResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                // response.beneficiaryNo = jsonObject.getString("BeneficiaryNo");
                response = jsonObject.getString("Description");
            } else {
                onResponse.onResponseMessage(message);
                response = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
