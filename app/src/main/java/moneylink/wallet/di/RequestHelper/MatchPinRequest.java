package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class MatchPinRequest {
    public String languageID;
    public String customerNO;
    public String customerPIN;

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:MatchPIN>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_Number>" + customerNO + "</tpay:Customer_Number>" +
                "<tpay:Custommer_PIN>" + customerPIN + "</tpay:Custommer_PIN>" +
                "</tpay:Req>" +
                "</tpay:MatchPIN>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
