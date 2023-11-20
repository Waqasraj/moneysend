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
import moneylink.wallet.di.RequestHelper.GetCardDetailsRequest;
import moneylink.wallet.di.ResponseHelper.GetCardDetailsResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetCardDetails;
import moneylink.wallet.utils.ProgressBar;

public class GetCardDetailsTask extends AsyncTask<GetCardDetailsRequest,
        Void, List<GetCardDetailsResponse>> {

    ProgressBar progressBar;
    Context context;
    OnGetCardDetails onGetCardDetails;


    public GetCardDetailsTask(Context context, OnGetCardDetails onGetCardDetails) {
        this.context = context;
        this.onGetCardDetails = onGetCardDetails;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<GetCardDetailsResponse> getCardDetailsResponses) {
        super.onPostExecute(getCardDetailsResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!getCardDetailsResponses.isEmpty()) {
            onGetCardDetails.onCardDetailsGet(getCardDetailsResponses);
        }
    }

    @Override
    protected List<GetCardDetailsResponse> doInBackground(GetCardDetailsRequest... getCardDetailsRequests) {
        Log.e("doInBackground: ", getCardDetailsRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(getCardDetailsRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_CARD_DETAILS
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<GetCardDetailsResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetCardDetailsResponse").getJSONObject("GetCardDetailsResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;
                Log.e("doInBackground: ", jsonObject.toString());
                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONArray("CardDetails");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetCardDetailsResponse response = new GetCardDetailsResponse();
                        response.cardName =  jsonObject.getString("Customer_CardName");
                        response.cardNumber = jsonObject.getString("Card_Number");
                        response.cardExpireDate = jsonObject.getString("CardExpiry_Date");
                        responseList.add(response);
                    }
                } else {
                    jsonObject =  jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONObject("CardDetails");
                    GetCardDetailsResponse response = new GetCardDetailsResponse();
                    response.cardName =  jsonObject.getString("Customer_CardName");
                    response.cardNumber = jsonObject.getString("Card_Number");
                    response.cardExpireDate = jsonObject.getString("CardExpiry_Date");
                    responseList.add(response);
                }
            } else {
                onGetCardDetails.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
