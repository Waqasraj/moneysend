package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetYBranchRequest {
    public int cityID;
    public String languageID;
    public int locationID;

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetYBranches>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:City_ID>" + cityID + "</tpay:City_ID>" +
                "<tpay:Location_ID>" + locationID + "</tpay:Location_ID>" +
                "</tpay:Req>" +
                "</tpay:GetYBranches>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
