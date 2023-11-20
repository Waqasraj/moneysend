package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class BillerSKUDataResponse extends ApiResponse<BillerSKUDataResponse> {
    @SerializedName("Biller_ID_")
    public String billerID;
    @SerializedName("Biller_Name_")
    public String billerName;
    @SerializedName("SKU_Data_")
    public List<SkuData> skuData;


    public static class SkuData {
        @SerializedName("SKU")
        public String skuID;
    }
}
