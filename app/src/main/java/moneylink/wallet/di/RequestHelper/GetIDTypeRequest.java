package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetIDTypeRequest {
    public String countryShortName;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetIDType>" +
                "<tpay:Req>" +
                "<tpay:CountryShortName>" + countryShortName + "</tpay:CountryShortName>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryShortName>" + countryShortName + "</tpay:CountryShortName>" +
                "</tpay:Req>" +
                "</tpay:GetIDType>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
