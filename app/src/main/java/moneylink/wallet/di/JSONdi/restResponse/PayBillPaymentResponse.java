package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class PayBillPaymentResponse extends ApiResponse<PayBillPaymentResponse> {
    @SerializedName("Biller_Name_")
    @Expose
    public String billerName;
    @SerializedName("PayOut_Currency_")
    @Expose
    public String payOutCurrency;
    @SerializedName("Authenticator_Name_")
    @Expose
    public String authenticatorName;
    @SerializedName("Authenticator_Value_")
    @Expose
    public String authValue;

    @SerializedName("Exchange_Rate_")
    @Expose
    public String exchangeRate;

    @SerializedName("Total_Payable_")
    @Expose
    public String totalPayable;


    @SerializedName("Payment_data_")
    @Expose
    public PaymentAccount billPayRespData;


    public class PaymentAccount {

        @SerializedName("ResponseDateTime")
        @Expose
        public String responseDateTime;
        @SerializedName("TicketCaption")
        @Expose
        public String ticketCaption;
        @SerializedName("PartnerTransactionID")
        @Expose
        public String partnerTransactionID;
        @SerializedName("EntityTransactionID")
        @Expose
        public String entityTransactionID;

        @SerializedName("SettlementCurrency")
        @Expose
        public String settlementCurrency;
        @SerializedName("BaseCurrency")
        @Expose
        public String baseCurrency;
        @SerializedName("FXRate")
        @Expose
        public String fxRate;
        @SerializedName("ConfirmationNumber")
        @Expose
        public String confirmationNumber;

        @SerializedName("ResponseCode")
        @Expose
        public String responseCode;
        @SerializedName("ResponseMessage")
        @Expose
        public String responseMessage;
    }


}