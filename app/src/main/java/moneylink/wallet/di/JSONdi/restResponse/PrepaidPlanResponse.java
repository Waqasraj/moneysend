package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;
import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;


public class PrepaidPlanResponse extends ApiResponse<PrepaidPlanResponse> {
    @SerializedName("Biller")
    public PrepaidOperatorResponse biller;

    @SerializedName("SKU")
    public List<MobileTopUpSKU> skuList;

    @SerializedName("Output1")
    public String outPut;

    @SerializedName("ResponseMessage")
    public String responseMessage;

    public static class MobileTopUpSKU {
        @SerializedName("CatalogVersion")
        public String catalogVersion;

        @SerializedName("SKU")
        public String sku;

        @SerializedName("Currency")
        public String currency;

        @SerializedName("Type")
        public String type;

        @SerializedName("Description")
        public String description;

        @SerializedName("Amount")
        public String amount;

        @SerializedName("MinAmount")
        public String minAmount;

        @SerializedName("MaxAmount")
        public String maxAmount;

        @SerializedName("Talktime")
        public String talkTime;

        @SerializedName("Data")
        public String data;

        @SerializedName("Validity")
        public String validity = "NA";

        @SerializedName("Benefits")
        public String benefits;
    }


}
