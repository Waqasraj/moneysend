package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetCountryListRequest {
    public String languageId = "1";

    public String getXML() {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetCountryList>"+
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + StaticHelper.LANGUAGE_ID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "</tpay:Req>" +
               "</tpay:GetCountryList>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

        return xml;
    }
}
