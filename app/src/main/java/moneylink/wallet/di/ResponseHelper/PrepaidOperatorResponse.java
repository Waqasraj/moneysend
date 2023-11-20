package moneylink.wallet.di.ResponseHelper;

import com.google.gson.annotations.SerializedName;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class PrepaidOperatorResponse extends ApiResponse<PrepaidOperatorResponse> {

    @SerializedName("BillerID")
    public String billerID;

    @SerializedName("BillerName")
    public String billerName;
}
