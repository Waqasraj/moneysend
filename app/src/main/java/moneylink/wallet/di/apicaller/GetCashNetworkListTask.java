package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetCashNetworkListRequest;
import moneylink.wallet.di.ResponseHelper.GetCashNetworkListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetCashNetworkList;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class GetCashNetworkListTask extends AsyncTask<GetCashNetworkListRequest , Void , List<GetCashNetworkListResponse>> {

    ProgressBar progressBar;
    Context context;
    OnGetCashNetworkList delegate;

    public GetCashNetworkListTask(Context context, OnGetCashNetworkList delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context , context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(List<GetCashNetworkListResponse> getCashNetworkListResponses) {
        super.onPostExecute(getCashNetworkListResponses);
        progressBar.hideProgressDialogWithTitle();
        if(getCashNetworkListResponses != null && !getCashNetworkListResponses.isEmpty()) {
            delegate.onGetNetworkList(getCashNetworkListResponses);
        }
    }

    @Override
    protected List<GetCashNetworkListResponse> doInBackground(GetCashNetworkListRequest... getCashNetworkListRequests) {
        List<GetCashNetworkListResponse>  responseList = new ArrayList<>();
        try {
            String responseString = HTTPHelper.getResponse(getCashNetworkListRequests[0]
                            .getXML(), SoapActionHelper.ACTION_GET_CASH_NETWORK_LIST
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();

            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetCashNetworkListResponse").getJSONObject("GetCashNetworkListResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                        .getJSONObject("CashNetworkList");
                JSONArray array = null;
                try {
                    array = jsonObject.getJSONArray("tblCashNetworkList");
                } catch (Exception e) {
                    Log.e("doInBackground: ",e.getLocalizedMessage() );
                }

                if(array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        GetCashNetworkListResponse response = new GetCashNetworkListResponse();
                        jsonObject = array.getJSONObject(i);
                        response.payOutAgent = jsonObject.getString("PayOutAgent");
                        response.country = jsonObject.getString("Country");
                        response.countryCode = jsonObject.getString("CountryCode");
                        response.payOutBranchCode = jsonObject.getString("PayOutBranchCode");
                        response.payOutCurrencyCode = jsonObject.getString("PayOutCurrencyCode");
                        response.paymentMode = jsonObject.getString("PaymentMode");
                        response.city = jsonObject.getString("City");
                        response.location = jsonObject.getString("Location");
                        response.branchName = jsonObject.getString("BranchName");
                        response.address = jsonObject.getString("Address");
                        response.telephone = jsonObject.getString("Telephone");
                        responseList.add(response);
                    }
                } else {
                    GetCashNetworkListResponse response = new GetCashNetworkListResponse();
                    jsonObject = jsonObject.getJSONObject("tblCashNetworkList");
                    response.payOutAgent = jsonObject.getString("PayOutAgent");
                    response.country = jsonObject.getString("Country");
                    response.countryCode = jsonObject.getString("CountryCode");
                    response.payOutBranchCode = jsonObject.getString("PayOutBranchCode");
                    response.payOutCurrencyCode = jsonObject.getString("PayOutCurrencyCode");
                    response.paymentMode = jsonObject.getString("PaymentMode");
                    response.city = jsonObject.getString("City");
                    response.location = jsonObject.getString("Location");
                    response.branchName = jsonObject.getString("BranchName");
                    response.address = jsonObject.getString("Address");
                    response.telephone = jsonObject.getString("Telephone");
                    responseList.add(response);
                }
            } else {
                delegate.onResponseMessage(message);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseList;
    }
}
