package tootipay.wallet.di.RequestHelper;

import tootipay.wallet.di.Params;
import tootipay.wallet.di.StaticHelper;

public class WalletToWalletTransferRequest {
    public String customerNo = "";
    public String receiptMobileNo = "";
    public String transferAmount = "";
    public String description = "";
    public String payInCurrency = "";
    public String receiptCurrency = "";
    public String languageId = "1";
    public String ipAddress = "";
    public String ipCountryName = "";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:WalletToWalletTransfer>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:PayIn_Currency>" + payInCurrency + "</tpay:PayIn_Currency>" +
                "<tpay:Recipient_Mobile_No>" + receiptMobileNo.replace(" ", "") + "</tpay:Recipient_Mobile_No>" +
                "<tpay:Recipient_Currency>" + receiptCurrency + "</tpay:Recipient_Currency>" +
                "<tpay:Transfer_Amount>" + transferAmount + "</tpay:Transfer_Amount>" +
                "<tpay:Description>" + description + "</tpay:Description>" +
                "<tpay:IpAddress>" + ipAddress + "</tpay:IpAddress>" +
                "<tpay:IpCountryName>" + ipCountryName + "</tpay:IpCountryName>" +
                "</tpay:Req>" +
                "</tpay:WalletToWalletTransfer>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
