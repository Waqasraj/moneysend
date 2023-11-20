package moneylink.wallet.di.ResponseHelper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class GetWRCountryListResponseC extends ApiResponse<List<GetWRCountryListResponseC>> {

    @SerializedName("CountryCode")
    public String code;


    @SerializedName("CountryName")
    public String name;


}
