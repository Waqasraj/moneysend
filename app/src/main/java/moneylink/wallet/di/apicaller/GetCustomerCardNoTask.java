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
import moneylink.wallet.di.RequestHelper.GetCustomerCardNoRequest;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnCustomerCardNo;
import moneylink.wallet.utils.ProgressBar;


public class GetCustomerCardNoTask extends AsyncTask<GetCustomerCardNoRequest
        , Void, String> {

    ProgressBar progressBar;
    Context context;
    OnCustomerCardNo onCustomerCardNo;


    public GetCustomerCardNoTask(Context context, OnCustomerCardNo onCustomerCardNo) {
        this.context = context;
        this.onCustomerCardNo = onCustomerCardNo;
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
            if (s.equalsIgnoreCase("302")) {
                onCustomerCardNo.createCustomerCard();
            } else {
                onCustomerCardNo.onGetCustomerCardNo(s);
            }
        }
    }

    @Override
    protected String doInBackground(GetCustomerCardNoRequest... getCustomerCardNoRequests) {
        String xml = getCustomerCardNoRequests[0]
                .getXML();
        Log.e("doInBackground: ", xml);
        String responseString = HTTPHelper.getResponse(getCustomerCardNoRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_CUSTOMER_CARD_NO
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetCustomerCardNOResponse").getJSONObject("GetCustomerCardNOResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                response = jsonObject.getString("VirtualCardNo");

            } else if (response.isEmpty()) {
                response = "302";
            } else {
                onCustomerCardNo.onResponseMessage(message);
                response = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
