package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.CountryPayOutCurrencyRequest;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.utils.ProgressBar;

public class CountryPayoutCurrencyTask extends AsyncTask
 <CountryPayOutCurrencyRequest , Void , List<GetSendRecCurrencyResponse>>{

    ProgressBar progressBar;
    public Context context;
    OnSelectCurrency delegate;


    public CountryPayoutCurrencyTask(Context context, OnSelectCurrency delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context , "Please Wait.....");
    }


    @Override
    protected void onPostExecute(List<GetSendRecCurrencyResponse> getSendRecCurrencyResponses) {
        super.onPostExecute(getSendRecCurrencyResponses);
        progressBar.hideProgressDialogWithTitle();
        if (getSendRecCurrencyResponses != null) {
            delegate.onCurrencyResponse(getSendRecCurrencyResponses);
        }
    }

    @Override
    protected List<GetSendRecCurrencyResponse>
    doInBackground(CountryPayOutCurrencyRequest... countryPayOutCurrencyRequests) {
        String responseString = HTTPHelper.getResponse(countryPayOutCurrencyRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_PAY_OUT_CURRENCY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<GetSendRecCurrencyResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("APIGetPayOutCurrencyListResponse").getJSONObject("APIGetPayOutCurrencyListResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetPayOutCurrencyList").getJSONArray("TblGetPayOutCurrencyList");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                        response.id = jsonObject.getString("CountryID");
                        response.currencyName = jsonObject.getString("CurrencyName");
                        response.currencyShortName = jsonObject.getString("CurrencyShortName");
                        response.image_URL = jsonObject.getString("Image_URL");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetPayOutCurrencyList").getJSONObject("TblGetPayOutCurrencyList");
                    GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                    response.id = jsonObject.getString("CountryID");
                    response.currencyName = jsonObject.getString("CurrencyName");
                    response.currencyShortName = jsonObject.getString("CurrencyShortName");
                    response.image_URL = jsonObject.getString("Image_URL");
                    responseList.add(response);
                }

            } else {
                delegate.onResponseMessage(message);
                responseList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
