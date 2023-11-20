package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class LoadVirtualCardRequest {

    public String customerNo;
    public String virtualCardNo;
    public String loadAmount;
    public String loadCurrency = "USD";
    public String languageID = "1";


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:LoadVirtualCard>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:Virtual_CardNo>" + virtualCardNo + "</tpay:Virtual_CardNo>" +
                "<tpay:Load_Amount>" + loadAmount + "</tpay:Load_Amount>" +
                "<tpay:Load_Currency>" + loadCurrency + "</tpay:Load_Currency>" +
                "</tpay:Req>" +
                "</tpay:LoadVirtualCard>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }


}
