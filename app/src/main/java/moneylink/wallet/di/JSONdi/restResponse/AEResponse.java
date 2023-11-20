package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;

public class AEResponse extends ApiResponse<AEResponse> {
    @SerializedName("body")
    public String body;
}
