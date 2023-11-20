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
import moneylink.wallet.di.RequestHelper.WalletBalanceRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnWalletBalanceReceived;
import moneylink.wallet.utils.ProgressBar;

public class WalletBalanceRequestTask extends
        AsyncTask<WalletBalanceRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnWalletBalanceReceived onWalletBalanceReceived;
    boolean isLogin = false;


    public WalletBalanceRequestTask(Context context, OnWalletBalanceReceived onWalletBalanceReceived) {
        this.context = context;
        this.onWalletBalanceReceived = onWalletBalanceReceived;
    }


    public WalletBalanceRequestTask(Context context, boolean isLogin, OnWalletBalanceReceived onWalletBalanceReceived) {
        this.context = context;
        this.onWalletBalanceReceived = onWalletBalanceReceived;
        this.isLogin = isLogin;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isLogin) {
            progressBar = new ProgressBar();
            progressBar.showProgressDialogWithTitle(context,
                    context.getResources().getString(R.string.loading_txt));
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!isLogin) {
            progressBar.hideProgressDialogWithTitle();
        }
        if (!s.isEmpty()) {
            onWalletBalanceReceived.onWalletBalanceReceived(s);
        }
    }


    @Override
    protected String doInBackground(WalletBalanceRequest... walletBalanceRequests) {
        Log.e("doInBackground: ", walletBalanceRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(walletBalanceRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WALLET_BALANCE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        String walletBalance = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("WalletBalanceResponse").getJSONObject("WalletBalanceResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground: ", jsonObject.toString());
            if (responseCode.equals("101")) {
                walletBalance = jsonObject.getString("WalletBalance");
            } else if (responseCode.equals("525")) {
                onWalletBalanceReceived.onLockWalletOption(false);
            }
            else {
                onWalletBalanceReceived.onResponseMessage(message);
                walletBalance = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return walletBalance;
    }
}
