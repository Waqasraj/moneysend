package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class ChangePin {
    public String customerNo;
    public String oldPin;
    public String newPin;
    public String confirmPin;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:ChangePIN>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_Number>" + customerNo + "</tpay:Customer_Number>" +
                "<tpay:Old_PIN>" + oldPin + "</tpay:Old_PIN>" +
                "<tpay:New_PIN>" + newPin + "</tpay:New_PIN>" +
                "<tpay:Confirm_PIN>" + confirmPin + "</tpay:Confirm_PIN>" +
                "</tpay:Req>" +
                "</tpay:ChangePIN>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
