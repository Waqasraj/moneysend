package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.LoginRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSuccessLogin;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class LoginRequestTask extends AsyncTask<LoginRequest, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnSuccessLogin onResponse;
    boolean isLogin = false;
    String serverError = "";

    public LoginRequestTask(Context context, OnSuccessLogin onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    public LoginRequestTask(Context context, boolean isLogin, OnSuccessLogin onResponse) {
        this.context = context;
        this.onResponse = onResponse;
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
    protected void onCancelled(String s) {
        super.onCancelled(s);
        if (!isLogin) {
            progressBar.hideProgressDialogWithTitle();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (!isLogin) {
            progressBar.hideProgressDialogWithTitle();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!isLogin) {
            progressBar.hideProgressDialogWithTitle();
        }
        if (!s.equals("")) {
            onResponse.onSuccessLogin(s);
        } else if(!serverError.equals("")){
            onResponse.onResponseMessage(context.getString(R.string.server_error));
        }
    }

    @Override
    protected String doInBackground(LoginRequest... loginRequestTasks) {
        Log.e("XML: ", loginRequestTasks[0].getXML());

        String responseString = HTTPHelper.getResponse(loginRequestTasks[0]
                        .getXML(),
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
                onResponse.onResponseMessage(message);
                //    onMessage(message);
                customerNo = "";
                serverError = "";
            }
            //{"ResponseCode":100,"Description":"Request failed"}
        } catch (JSONException e) {
            e.printStackTrace();
            serverError = "some error";
        }
        return customerNo;
    }
}
