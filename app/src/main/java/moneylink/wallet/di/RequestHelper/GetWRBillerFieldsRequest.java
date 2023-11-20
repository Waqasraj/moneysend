package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetWRBillerFieldsRequest {
    public String countryCode;
    public Integer billerID;
    public Integer skuID;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetWRBillerFields>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:countryCode>" + countryCode + "</tpay:countryCode>" +
                "<tpay:BillerID>" + billerID + "</tpay:BillerID>" +
                "<tpay:SkuID>" + skuID + "</tpay:SkuID>" +
                "</tpay:Req>" +
                "</tpay:GetWRBillerFields>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
