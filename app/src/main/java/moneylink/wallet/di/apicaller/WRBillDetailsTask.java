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
import moneylink.wallet.di.RequestHelper.WRBillDetailRequest;
import moneylink.wallet.di.ResponseHelper.WRBillDetailsResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnWRBillDetail;
import moneylink.wallet.utils.ProgressBar;

public class WRBillDetailsTask extends AsyncTask<WRBillDetailRequest
        , Void, WRBillDetailsResponse> {

    ProgressBar progressBar;
    Context context;
    OnWRBillDetail onWRBillDetail;


    public WRBillDetailsTask(Context context, OnWRBillDetail onWRBillDetail) {
        this.context = context;
        this.onWRBillDetail = onWRBillDetail;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(WRBillDetailsResponse wrBillDetailsResponse) {
        super.onPostExecute(wrBillDetailsResponse);
        progressBar.hideProgressDialogWithTitle();
        if (wrBillDetailsResponse != null) {
            onWRBillDetail.onBillDetails(wrBillDetailsResponse);
        }
    }

    @Override
    protected WRBillDetailsResponse doInBackground(WRBillDetailRequest... wrBillDetailRequests) {
        Log.e("XML", wrBillDetailRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(wrBillDetailRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_BILL_DETAILS
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        WRBillDetailsResponse billDetail = new WRBillDetailsResponse();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("WRBillDetailResponse").getJSONObject("WRBillDetailResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground: ", jsonObject.toString());
            if (responseCode.equals("101")) {
                billDetail.balanceORDue = jsonObject.getString("BalanceOrDue");
                billDetail.billerID = jsonObject.getString("billerID");
                billDetail.skuID = jsonObject.getString("skuID");
                billDetail.billDueDate = jsonObject.getString("BillDueDate");
                billDetail.customerName = jsonObject.getString("CustomerName");
            } else {
                onWRBillDetail.onResponseMessage(message);
                billDetail = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return billDetail;
    }
}
