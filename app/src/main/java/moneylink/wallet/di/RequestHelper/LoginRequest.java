package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class LoginRequest {
    public String emailAddress = "";
    public String mobileNumber = "";
    public String password = "";
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:Login>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Email_Address>" + emailAddress + "</tpay:Email_Address>" +
                "<tpay:Mobile_Number>" + mobileNumber + "</tpay:Mobile_Number>" +
                "<tpay:Password>" + password + "</tpay:Password>" +
                "</tpay:Req>" +
                "</tpay:Login>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
