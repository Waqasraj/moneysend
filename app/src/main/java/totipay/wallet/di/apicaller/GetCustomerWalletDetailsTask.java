package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import totipay.wallet.R;
import totipay.wallet.di.ApiHelper;
import totipay.wallet.di.HTTPHelper;
import totipay.wallet.di.RequestHelper.GetCustomerWalletDetailsRequest;
import totipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;
import totipay.wallet.di.SoapActionHelper;
import totipay.wallet.interfaces.OnCustomerWalletDetails;
import totipay.wallet.utils.ProgressBar;

public class GetCustomerWalletDetailsTask extends
        AsyncTask<GetCustomerWalletDetailsRequest, Void, List<GetCustomerWalletDetailsResponse>> {

    ProgressBar progressBar;
    Context context;
    OnCustomerWalletDetails onCustomerWalletDetails;
    boolean isHome = false;

    public GetCustomerWalletDetailsTask(Context context, OnCustomerWalletDetails onCustomerWalletDetails) {
        this.context = context;
        this.onCustomerWalletDetails = onCustomerWalletDetails;
    }

    public GetCustomerWalletDetailsTask(Context context, OnCustomerWalletDetails onCustomerWalletDetails, boolean isHome) {
        this.context = context;
        this.onCustomerWalletDetails = onCustomerWalletDetails;
        this.isHome = isHome;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isHome) {
            progressBar = new ProgressBar();
            progressBar.showProgressDialogWithTitle(context
                    , context.getResources().getString(R.string.loading_txt));
        }

    }

    @Override
    protected void onPostExecute(List<GetCustomerWalletDetailsResponse> getCustomerWalletDetailsResponses) {
        super.onPostExecute(getCustomerWalletDetailsResponses);
        if (!isHome) {
            progressBar.hideProgressDialogWithTitle();
        }

        if (!getCustomerWalletDetailsResponses.isEmpty()) {
            onCustomerWalletDetails.onCustomerWalletDetails(getCustomerWalletDetailsResponses);
        }
    }

    @Override
    protected List<GetCustomerWalletDetailsResponse> doInBackground(GetCustomerWalletDetailsRequest... getCustomerWalletDetailsRequests) {
        List<GetCustomerWalletDetailsResponse> responseList = new ArrayList<>();
        Log.e("doInBackground: ", getCustomerWalletDetailsRequests[0].getXML());
        try {
            String responseString = HTTPHelper.getResponse(getCustomerWalletDetailsRequests[0]
                            .getXML(), SoapActionHelper.ACTION_GET_CUSTOMER_WALLET
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();
            Log.e("doInBackground: ", jsonObject.toString());
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetCustWalletDetailsResponse").getJSONObject("GetCustWalletDetailsResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                jsonObject = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                        .getJSONObject("DocumentElement");
                JSONArray array = null;
                try {
                    array = jsonObject.getJSONArray("WalletDetails");
                } catch (Exception e) {
                    Log.e("doInBackground: ", e.getLocalizedMessage());
                }

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        GetCustomerWalletDetailsResponse response = new GetCustomerWalletDetailsResponse();
                        jsonObject = array.getJSONObject(i);
                        response.currencyId = jsonObject.getString("CurrencyID");
                        try {
                            response.balance = jsonObject.getString("Balance");
                        } catch (Exception e) {
                            response.balance = "0.0";
                        }

                        response.currencyShortName = jsonObject.getString("CurrencyShortName");
                        response.imageURL = jsonObject.getString("Image_URL");
                        if (response.currencyShortName.equalsIgnoreCase("gbp")) {
                            response.currencyFullName = context.getString(R.string.british_pound);
                        } else if (response.currencyShortName.equalsIgnoreCase("eur")) {
                            response.currencyFullName = context.getString(R.string.euro);
                        } else if (response.currencyShortName.equalsIgnoreCase("usd")) {
                            response.currencyFullName = context.getString(R.string.un_dollar);
                        } else if(response.currencyShortName.equalsIgnoreCase("yer")) {
                            response.currencyFullName = context.getString(R.string.yemini_riyal);
                        } else if(response.currencyShortName.equalsIgnoreCase("GLD")) {
                            response.currencyFullName = context.getString(R.string.gold);
                        }

                        else {
                            response.currencyFullName = "";
                        }

                        responseList.add(response);
                    }
                }
            } else {
                onCustomerWalletDetails.onResponseMessage(message);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.swap(responseList , 0 , 1);
        return responseList;
    }
}