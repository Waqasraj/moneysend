package moneylink.wallet.di.JSONdi.restResponse.paykiiResponse;

import com.google.gson.annotations.SerializedName;

public class GetPaymentDetailsResponse {
    @SerializedName("Biller_Name_")
    public String billerName;
    @SerializedName("PayOut_Currency_")
    public String payoutCurrency;

    @SerializedName("Exchange_Rate_")
    public String exchangeRate;

    @SerializedName("Total_Payable_")
    public String totalPayable;
}
