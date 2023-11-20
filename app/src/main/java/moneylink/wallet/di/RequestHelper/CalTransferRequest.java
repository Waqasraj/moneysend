package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.MethodNameHelper;
import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class CalTransferRequest {

    public String PayInCurrency;
    public String PayoutCurrency;
    public String TransferCurrency;
    public Double TransferAmount;
    public String PaymentMode;
    public String languageId = "1";
    public String payInCountry = "";
    public String payOutCountry = "";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                MethodNameHelper.CAL_TRANSFER_OPENER +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:PayInCurrency>" + PayInCurrency + "</tpay:PayInCurrency>" +
                "<tpay:PayoutCurrency>" + PayoutCurrency + "</tpay:PayoutCurrency>" +
                "<tpay:PayInCountry>" + payInCountry + "</tpay:PayInCountry>" +
                "<tpay:PayOutCountry>" + payOutCountry + "</tpay:PayOutCountry>" +
                "<tpay:TransferCurrency>" + TransferCurrency + "</tpay:TransferCurrency>" +
                "<tpay:TransferAmount>" + TransferAmount + "</tpay:TransferAmount>" +
                "<tpay:PaymentMode>" + PaymentMode + "</tpay:PaymentMode>" +
                "</tpay:Req>" +
                MethodNameHelper.CAL_TRANSFER_CLOSER +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
