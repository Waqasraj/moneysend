package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class ForgotPinSetNewRequest {
    public String emailAddress = "";
    public String mobileNumber = "";
    public String newPin;
    public String confirmPin;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:SetNewPIN>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Email_Address>" + emailAddress + "</tpay:Email_Address>" +
                "<tpay:Mobile_Number>" + mobileNumber + "</tpay:Mobile_Number>" +
                "<tpay:New_PIN>" + newPin + "</tpay:New_PIN>" +
                "<tpay:Confirm_PIN>" + confirmPin + "</tpay:Confirm_PIN>" +
                "</tpay:Req>" +
                "</tpay:SetNewPIN>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
