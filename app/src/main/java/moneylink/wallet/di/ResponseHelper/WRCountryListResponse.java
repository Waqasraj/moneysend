package moneylink.wallet.di.ResponseHelper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;

public class WRCountryListResponse extends ApiResponse<List<WRCountryListResponse>> {

    @SerializedName("CountryCode")
    public String countryCode;

    @SerializedName("CountryName")
    public String countryName;

    @SerializedName("CallingCode")
    public String callingCode;

}
