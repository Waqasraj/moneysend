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
import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.utils.ProgressBar;

public class GetWalletCurrencyListTask extends AsyncTask<GetWalletCurrencyListRequest
        , Void, List<GetSendRecCurrencyResponse>> {

    ProgressBar progressBar;
    Context context;
    OnSelectCurrency onSelectCurrency;

    public GetWalletCurrencyListTask(Context context, OnSelectCurrency onSelectCurrency) {
        this.context = context;
        this.onSelectCurrency = onSelectCurrency;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<GetSendRecCurrencyResponse> getSendRecCurrencyResponses) {
        super.onPostExecute(getSendRecCurrencyResponses);
        progressBar.hideProgressDialogWithTitle();
        if (getSendRecCurrencyResponses != null) {
            if (!getSendRecCurrencyResponses.isEmpty()) {
                onSelectCurrency.onCurrencyResponse(getSendRecCurrencyResponses);
            }
        }

    }

    @Override
    protected List<GetSendRecCurrencyResponse>
    doInBackground(GetWalletCurrencyListRequest... getWalletCurrencyListRequests) {
        Log.e("doInBackground: ",
                getWalletCurrencyListRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(getWalletCurrencyListRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_WALLET_CURRENCY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ",
                jsonObject.toString());
        List<GetSendRecCurrencyResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWalletCurrencyListResponse").getJSONObject("GetWalletCurrencyListResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONArray("CurrencyList");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                        response.id = jsonObject.getString("Currency_Id");
                        response.currencyShortName = jsonObject.getString("CurrencyShortName");
                        try {
                            response.image_URL = jsonObject.getString("Image_URL");
                        } catch (Exception e) {

                        }
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONObject("CurrencyList");
                    GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                    response.id = jsonObject.getString("Currency_Id");
                    response.currencyShortName = jsonObject.getString("CurrencyShortName");

                    try {
                        response.image_URL = jsonObject.getString("Image_URL");
                    } catch (Exception e) {

                    }

                    responseList.add(response);
                }

            } else {
                onSelectCurrency.onResponseMessage(message);
                responseList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
