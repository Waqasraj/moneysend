package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class BillDetailResponse extends ApiResponse<BillDetailResponse> {


    @SerializedName("Biller_Name_")
    public String billerName;

    @SerializedName("PayOut_Currency_")
    public String payOutCurrency;

    @SerializedName("Exchange_Rate_")
    public String exchangeRate;

    @SerializedName("Total_Payable_")
    public String totalPayable;

    @SerializedName("Bill_details_")
    public BillDetails billDetails;


    public static class BillDetails {

        @SerializedName("ResponseDateTime")
        public String responseTime;

        @SerializedName("PartnerTransactionID")
        public String partnerTransactionId;

        @SerializedName("EntityTransactionID")
        public String entityTransactionID;

        @SerializedName("BillAmountDue")
        public String billAmountID;

        @SerializedName("BillDueDate")
        public String billDueDate;

        @SerializedName("BillsDue")
        public String billsDue;


        @SerializedName("Output1")
        public String output1;

        @SerializedName("Output2")
        public String output2;


        @SerializedName("TotalAmountDue")
        public String totalDue;

    }

}


