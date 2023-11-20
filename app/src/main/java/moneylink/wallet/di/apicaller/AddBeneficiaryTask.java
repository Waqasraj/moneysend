package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.BeneficiaryAddRequest;
import moneylink.wallet.di.ResponseHelper.AddBeneficiaryResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnApiResponse;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class AddBeneficiaryTask extends AsyncTask<BeneficiaryAddRequest , Void , AddBeneficiaryResponse> {

    ProgressBar progressBar;
    public Context context;
    OnApiResponse delegate;


    public AddBeneficiaryTask(Context context , OnApiResponse delegate) {
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
    protected void onPostExecute(AddBeneficiaryResponse addBeneficiaryResponse) {
        super.onPostExecute(addBeneficiaryResponse);
        progressBar.hideProgressDialogWithTitle();;
        if (addBeneficiaryResponse != null) {
            delegate.onSuccessResponse((Object)addBeneficiaryResponse);
        }
    }

    @Override
    protected AddBeneficiaryResponse doInBackground(BeneficiaryAddRequest... beneficiaryAddRequests) {

        String xml = beneficiaryAddRequests[0]
                .getXML();
        Log.e("doInBackground: ", xml);
        String responseString = HTTPHelper.getResponse(beneficiaryAddRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_ADD_BENEFICIARY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        AddBeneficiaryResponse response = new AddBeneficiaryResponse();
        try {
            assert jsonObject != null;
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("BeneficiaryRegistrationResponse")
                    .getJSONObject("BeneficiaryRegistrationResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                response.beneficiaryNo = jsonObject.getString("BeneficiaryNo");
            //    delegate.onSuccessResponse("Beneficairy Added Successfully");
            } else {
               delegate.onError(message);
               response = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
