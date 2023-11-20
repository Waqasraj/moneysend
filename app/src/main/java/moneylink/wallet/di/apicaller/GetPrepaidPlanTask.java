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
import moneylink.wallet.di.RequestHelper.GetWRPrepaidPlansRequest;
import moneylink.wallet.di.ResponseHelper.GetPrepaidPlansResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetPrepaidPlans;
import moneylink.wallet.utils.ProgressBar;

public class GetPrepaidPlanTask extends
        AsyncTask<GetWRPrepaidPlansRequest, Void, List<GetPrepaidPlansResponse>> {


    ProgressBar progressBar;
    Context context;
    OnGetPrepaidPlans prepaidPlans;


    public GetPrepaidPlanTask(Context context, OnGetPrepaidPlans prepaidPlans) {
        this.context = context;
        this.prepaidPlans = prepaidPlans;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<GetPrepaidPlansResponse> getPrepaidPlansResponses) {
        super.onPostExecute(getPrepaidPlansResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!getPrepaidPlansResponses.isEmpty()) {
            prepaidPlans.onGetPrepaidPlans(getPrepaidPlansResponses);
        }
    }

    @Override
    protected List<GetPrepaidPlansResponse> doInBackground(GetWRPrepaidPlansRequest... getWRPrepaidPlansRequests) {
        Log.e("doInBackground: ", getWRPrepaidPlansRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(getWRPrepaidPlansRequests[0].getXML(),
                SoapActionHelper.ACTION_GET_PREPAID_PLAN
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ", jsonObject.toString());
        List<GetPrepaidPlansResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRPrepaidPlansResponse").getJSONObject("GetWRPrepaidPlansResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;
                JSONArray rechargeArray = null;

                try {
                    rechargeArray = jsonObject.getJSONObject("Obj").getJSONObject("rechargeSubTypes")
                    .getJSONArray("string");
                } catch (Exception e) {
                    Log.e("TAG", "rechaargeerro: " + e.getLocalizedMessage());
                }

                List<String> typesValues = new ArrayList<>();

                if (rechargeArray != null) {
                    for (int i = 0; i < rechargeArray.length(); i++) {
                        typesValues.add(rechargeArray.getJSONObject(i).getString("content"));
                    }
                } else {
                    typesValues.add(jsonObject.getJSONObject("Obj").getJSONObject("rechargeSubTypes")
                            .getString("string"));
                }


                for(int j = 0 ; j <typesValues.size() ; j++) {
                    try {
                        array = jsonObject.getJSONObject("Obj").getJSONObject("plans")
                                .getJSONObject(typesValues.get(j)).getJSONArray("plansdata");
                    } catch (Exception e) {
                        Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                    }


                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            GetPrepaidPlansResponse response = new GetPrepaidPlansResponse();
                            response.planId = object.getString("planId");
                            response.rechargeSubType = object.getString("rechargeSubType");
                            response.mrp = object.getString("MRP");
                            response.rechargeAmount = object.getString("rechargeAmount");
                            response.benefits = object.getString("benefits");
                            response.talkTime = object.getString("talktime");
                            responseList.add(response);
                        }
                    } else {
                        JSONObject object = jsonObject.getJSONObject("Obj").getJSONObject("plans")
                                .getJSONObject(typesValues.get(j)).getJSONObject("plansdata");
                        GetPrepaidPlansResponse response = new GetPrepaidPlansResponse();
                        response.planId = object.getString("planId");
                        response.rechargeSubType = object.getString("rechargeSubType");
                        response.mrp = object.getString("MRP");
                        response.rechargeAmount = object.getString("rechargeAmount");
                        response.benefits = object.getString("benefits");
                        response.talkTime = object.getString("talktime");
                        responseList.add(response);
                    }
                }


            } else {
                prepaidPlans.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("listSize: ", String.valueOf(responseList.size()));
        return responseList;
    }
}
