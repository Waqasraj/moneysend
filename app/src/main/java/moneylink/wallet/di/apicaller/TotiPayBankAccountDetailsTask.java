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
import moneylink.wallet.di.RequestHelper.GetTotiPayAccDetails;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.interfaces.OnGetBankNameList;
import moneylink.wallet.utils.ProgressBar;

public class TotiPayBankAccountDetailsTask extends AsyncTask
 <GetTotiPayAccDetails , Void , List<String>> {


    ProgressBar progressBar;
    public Context context;
    OnGetBankNameList delegate;

    public TotiPayBankAccountDetailsTask(Context context, OnGetBankNameList delegate) {
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
    protected void onPostExecute(List<String> aVoid) {
        super.onPostExecute(aVoid);
        progressBar.hideProgressDialogWithTitle();
        if(!aVoid.isEmpty()) {
delegate.onGetBankList(aVoid);
        }
    }

    @Override
    protected List<String> doInBackground(GetTotiPayAccDetails... getTotiPayAccDetails) {
        Log.e("XML: ", getTotiPayAccDetails[0].getXML());
        String responseString = HTTPHelper.getResponse(getTotiPayAccDetails[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_BANK_NAMES_TOTIPAY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e( "doInBackground: ", jsonObject.toString());
        List<String> strings = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetTotiPayAccDetailsResponse").getJSONObject("GetTotiPayAccDetailsResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");

            if (responseCode.equals("101")) {

                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONArray("TotiPayAccDetails");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        strings.add(jsonObject.getString("Data"));
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONObject("TotiPayAccDetails");
                    strings.add(jsonObject.getString("Data"));
                }

            } else {
                delegate.onResponseMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
