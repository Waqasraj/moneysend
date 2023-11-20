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
import moneylink.wallet.di.RequestHelper.KYCImageUploadRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnResponse;
import moneylink.wallet.utils.ProgressBar;

public class KYCImageUploadTask extends AsyncTask<KYCImageUploadRequest
        , Void, String> {

    ProgressBar progressBar;
    Context context;
    OnResponse onResponse;


    public KYCImageUploadTask(Context context, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (!s.isEmpty()) {
            onResponse.onSuccess();
        }
    }

    @Override
    protected String doInBackground(KYCImageUploadRequest... kycImageUploadRequests) {
        Log.e("XML: ", kycImageUploadRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(kycImageUploadRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_UPLOAD_IMAGE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String customerNo = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("UploadKYCImageResponse").getJSONObject("UploadKYCImageResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground: ", jsonObject.toString());
            if (responseCode.equals("101")) {
                customerNo = message;
            } else {
                onResponse.onResponseMessage(message);
                //    onMessage(message);
                customerNo = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerNo;
    }
}
