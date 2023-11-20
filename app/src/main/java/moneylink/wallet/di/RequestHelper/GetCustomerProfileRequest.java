package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetCustomerProfileRequest {
    public String customerNo = "";
    public String mobileNo = "";
    public String emailAddress = "";
    public String languageId = "1";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetCustomerProfile>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:MobileNo>" + mobileNo + "</tpay:MobileNo>" +
                "<tpay:Email_Address>" + emailAddress + "</tpay:Email_Address>" +

                "</tpay:Req>" +
                "</tpay:GetCustomerProfile>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
