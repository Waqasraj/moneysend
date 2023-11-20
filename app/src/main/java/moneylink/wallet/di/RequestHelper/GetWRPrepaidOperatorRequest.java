package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetWRPrepaidOperatorRequest {
    public String countryShortName;
    public String mobileNo;
    public String languageId;


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetWRPrepaidOperator>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryCode>" + countryShortName + "</tpay:CountryCode>" +
                "<tpay:MobileNo>" + mobileNo + "</tpay:MobileNo>" +
                "</tpay:Req>" +
                "</tpay:GetWRPrepaidOperator>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }

}
