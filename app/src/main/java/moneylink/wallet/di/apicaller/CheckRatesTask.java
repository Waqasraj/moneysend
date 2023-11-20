package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.CalTransferRequest;
import moneylink.wallet.di.ResponseHelper.CalTransferResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetTransferRates;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class CheckRatesTask  extends AsyncTask<CalTransferRequest, Integer , CalTransferResponse> {

    Context context;
    ProgressBar progressBar;
    OnGetTransferRates delegate;

    public CheckRatesTask(Context context,OnGetTransferRates delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context ,
                context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(CalTransferResponse calTransferResponse) {
        super.onPostExecute(calTransferResponse);
        progressBar.hideProgressDialogWithTitle();
        if(calTransferResponse != null) {
            delegate.onGetTransferRates(calTransferResponse);
        }
    }

    @Override
    protected CalTransferResponse doInBackground(CalTransferRequest... calTransferRequests) {
        Log.e("doInBackground: ",calTransferRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(calTransferRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_CAL_TRANSFER
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        CalTransferResponse response = new CalTransferResponse();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("CalTransferResponse").getJSONObject("CalTransferResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("JSONResponse: ",jsonObject.toString() );
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                response.exchangeRate = jsonObject.getString("ExchangeRate");
                response.vatValue = jsonObject.getString("VATValue");
                response.payInAmount = jsonObject.getString("PayInAmount");
                response.payoutAmount = jsonObject.getString("PayoutAmount");
                response.recommendedAgent = jsonObject.getInt("RecommendAgent");
                response.commission = jsonObject.getString("Commission");
                response.totalPayable = jsonObject.getString("TotalPayable");
                response.vatPercentage = jsonObject.getString("VATPercentage");
                response.payoutBranchCode = jsonObject.getInt("PayoutBranchCode");
                try{
                    response.payInCurrency = jsonObject.getString("PayInCurrency");
                } catch (Exception e) {

                }
                try{
                    response.payOutCurrency = jsonObject.getString("PayoutCurrency");
                } catch (Exception ex) {
                    Log.e( "doInBackground: ", ex.getLocalizedMessage());
                }


            } else {
                delegate.onResponseMessage(message);
            //    onMessage(message);
                response = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
