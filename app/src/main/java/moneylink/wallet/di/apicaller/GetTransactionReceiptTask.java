package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.R;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetTransactionReceiptRequest;
import moneylink.wallet.di.ResponseHelper.GetTransactionReceiptResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetTransactionReceipt;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class GetTransactionReceiptTask extends AsyncTask<GetTransactionReceiptRequest
        , Void, GetTransactionReceiptResponse> {

    Context context;
    ProgressBar progressBar;
    OnGetTransactionReceipt delegate;

    public GetTransactionReceiptTask(Context context, OnGetTransactionReceipt delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(GetTransactionReceiptResponse getTransactionReceiptResponse) {
        super.onPostExecute(getTransactionReceiptResponse);
        progressBar.hideProgressDialogWithTitle();
        if (getTransactionReceiptResponse != null) {
            delegate.onGetTransactionReceipt(getTransactionReceiptResponse);
        }
    }

    @Override
    protected GetTransactionReceiptResponse doInBackground(GetTransactionReceiptRequest... getTransactionReceiptRequests) {
        Log.e("XML" , getTransactionReceiptRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(getTransactionReceiptRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_TRANSACTION_RECEIPT
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        GetTransactionReceiptResponse response = new GetTransactionReceiptResponse();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetTransactionReceiptResponse").getJSONObject("GetTransactionReceiptResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                response.transactionNumber = jsonObject.getString("TransactionNumber");
                response.transactionDateTime = jsonObject.getString("TransactionDateTime");
                response.sendingAmount = jsonObject.getString("SendingAmount");
                response.payInCurrency = jsonObject.getString("PayInCurrency");
                response.receivingAmount = jsonObject.getString("ReceivingAmount");
                response.payoutCurrency = jsonObject.getString("PayOutCurrency");
                response.commissionCharge = jsonObject.getString("CommissionCharge");
                response.otherCharge = jsonObject.getString("OtherCharges");
                response.vatCharge = jsonObject.getString("VATCharges");
                response.vatPercentage = jsonObject.getString("VATPercentage");
                response.totalPayable = jsonObject.getString("TotalPayable");
                response.exchangeRate = jsonObject.getString("ExchangeRate");

                response.purposeOfTransfer = jsonObject.getString("PurposeOfTransfer");
                response.remitterNo = jsonObject.getString("RemitterNo");
                response.remitterName = jsonObject.getString("RemitterName");
                response.remitterContactNo = jsonObject.getString("RemitterContactNo");
                response.remitterAddress = jsonObject.getString("RemitterAddress");
            //    response.remitterDOB = jsonObject.getString("Remitter_DOB");
                response.idType = jsonObject.getString("ID_Type");

              //  response.senderIdNumber = jsonObject.getString("SenderIDNumber");
                //response.senderIdExpireDate = jsonObject.getString("SenderIDExpiry");
                response.relationWithBeneficiary = jsonObject.getString("RelationWithBeneficiary");
                response.beneficiaryNo = jsonObject.getString("BeneficiaryNo");
                response.beneficiaryName = jsonObject.getString("BeneficiaryName");
                response.beneficiaryAddress = jsonObject.getString("BeneficiaryAddress");
                response.beneficiaryContactNo = jsonObject.getString("BeneficiaryContactNo");


                response.customerEmail = jsonObject.getString("CustomerEmail");
                response.payInCountry = jsonObject.getString("PayInCountry");

                response.payOutCountry = jsonObject.getString("PayOutCountry");
                response.earnPoint = jsonObject.getString("earnPoint");
                response.availPoints = jsonObject.getString("AvailPoints");
                response.paymentMode = jsonObject.getString("PaymentMode");
                response.payoutAgent_Name = jsonObject.getString("PayoutAgent_Name");
                response.enteredBy = jsonObject.getString("EnteredBy");

                response.bankName = jsonObject.getString("BankName");
                response.bankAccountNo = jsonObject.getString("BankAccountNo");
                response.bankBranch = jsonObject.getString("BankBranch");
                response.bankAddress = jsonObject.getString("BankAddress");
                response.bankCode = jsonObject.getString("BankCode");

            } else {
                delegate.onResponseMessage(message);
                response = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
