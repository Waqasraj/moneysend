package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class GetWRBillerNamesResponseC extends ApiResponse<List<GetWRBillerNamesResponseC>> {

    @SerializedName("BillerID")
    public String Biller_ID;

    @SerializedName("BillerName")
    public String Biller_Name;

    @SerializedName("BillerDescription")
    public String BillerDescription = "";

    @SerializedName("CountryCode")
    public String countryCode = "AE";


    public int imageIcon;

}
