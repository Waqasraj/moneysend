package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class WRPrepaidRechargeRequest {
    public String customerNo;
    public String payoutCurrency;
    public String payOutAmount;
    public String payInCurrency;
    public String countryCode;
    public String mobileNumber = "";
    public String planId;
    public String languageId = "1";
    public Integer paymentTypeId;
    public String cardNumber = "";
    public String expireDate = "";
    public String securityCode = "";


    public String operatorName = "";


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:WRPrepaidRecharge>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:PayOutCurrency>" + payoutCurrency + "</tpay:PayOutCurrency>" +
                "<tpay:PayOutAmount>" + payOutAmount + "</tpay:PayOutAmount>" +
                "<tpay:PayinCurrency>" + payInCurrency + "</tpay:PayinCurrency>" +
                "<tpay:CountryCode>" + countryCode + "</tpay:CountryCode>" +
                "<tpay:MobileNumber>" + mobileNumber + "</tpay:MobileNumber>" +
                "<tpay:PlanId>" + planId + "</tpay:PlanId>" +
                "<tpay:Payment_TypeID>" + paymentTypeId + "</tpay:Payment_TypeID>" +
                "<tpay:Card_Number>" + cardNumber + "</tpay:Card_Number>" +
                "<tpay:ExpiryDate>" + expireDate + "</tpay:ExpiryDate>" +
                "<tpay:SecurityCode>" + securityCode + "</tpay:SecurityCode>" +
                "</tpay:Req>" +
                "</tpay:WRPrepaidRecharge>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
