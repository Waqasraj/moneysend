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
import moneylink.wallet.di.RequestHelper.WRBillerPlansRequest;
import moneylink.wallet.di.ResponseHelper.WRBillerPlanResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnBillerPlans;
import moneylink.wallet.utils.ProgressBar;

public class WRBillerPlanTask extends AsyncTask
        <WRBillerPlansRequest, Void, List<WRBillerPlanResponse>> {

    ProgressBar progressBar;
    Context context;
    OnBillerPlans onBillerPlans;


    public WRBillerPlanTask(Context context, OnBillerPlans onWRBillerType) {
        this.context = context;
        this.onBillerPlans = onWRBillerType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(List<WRBillerPlanResponse> wrBillerPlanResponses) {
        super.onPostExecute(wrBillerPlanResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!wrBillerPlanResponses.isEmpty()) {
           // onBillerPlans.onBillerPlans(wrBillerPlanResponses);
        }
    }

    @Override
    protected List<WRBillerPlanResponse> doInBackground(WRBillerPlansRequest... wrBillerPlansRequests) {
        Log.e("doInBackground: ", wrBillerPlansRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(wrBillerPlansRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WR_BILLER_PLAN
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<WRBillerPlanResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRBillerPlanResponse").getJSONObject("GetWRBillerPlanResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;
                Log.e("doInBackground: ", jsonObject.toString());
                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerPlans").getJSONArray("WRBillerPlan");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        WRBillerPlanResponse response = new WRBillerPlanResponse();
                        response.billerName = jsonObject.getString("Biller_Name");
                        response.billerType = jsonObject.getString("Biller_Type");
                        response.billerSKUId = jsonObject.getInt("Biller_SkuId");
                        response.minAmount = jsonObject.getDouble("MinAmount");
                        response.maxAmount = jsonObject.getDouble("MaxAmount");
                        response.payableAmount = jsonObject.getDouble("AmountPayable");
                        response.billerId = jsonObject.getInt("Biller_ID");
                        response.billerTypeId = jsonObject.getInt("billerTypeId");
                        response.billerCategoryID = jsonObject.getInt("billerCategoryId");
                        response.countryCode = jsonObject.getString("countryCode");
                        response.description = jsonObject.getString("Description");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetWRBillerPlans").getJSONObject("WRBillerPlan");
                    WRBillerPlanResponse response = new WRBillerPlanResponse();
                    response.billerName = jsonObject.getString("Biller_Name");
                    response.billerType = jsonObject.getString("Biller_Type");
                    response.billerSKUId = jsonObject.getInt("Biller_SkuId");
                    response.minAmount = jsonObject.getDouble("MinAmount");
                    response.maxAmount = jsonObject.getDouble("MaxAmount");
                    response.payableAmount = jsonObject.getDouble("AmountPayable");
                    response.billerId = jsonObject.getInt("Biller_ID");
                    response.billerTypeId = jsonObject.getInt("billerTypeId");
                    response.billerCategoryID = jsonObject.getInt("billerCategoryId");
                    response.countryCode = jsonObject.getString("countryCode");
                    response.description = jsonObject.getString("Description");
                    responseList.add(response);
                }
            } else {
                onBillerPlans.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
