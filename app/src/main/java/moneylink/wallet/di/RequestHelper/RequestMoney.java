package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class RequestMoney {
    public String customerNo;
    public String mobileNo;
    public Double amount;
    public String description;
    public String languageId = "1";
    public String currency = "";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:RequestMoney>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:Mobile_Number>" + mobileNo + "</tpay:Mobile_Number>" +
                "<tpay:Amount>" + amount + "</tpay:Amount>" +
                "<tpay:Currency>" + currency + "</tpay:Currency>" +
                "<tpay:Description>" + description + "</tpay:Description>" +
                "</tpay:Req>" +
                "</tpay:RequestMoney>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
