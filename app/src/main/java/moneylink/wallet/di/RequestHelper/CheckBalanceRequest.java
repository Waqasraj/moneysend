package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class CheckBalanceRequest {
    public String mobileNO;


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:CheckBalance>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "</tpay:Credentials>" +
                "<tpay:customer_mobile_no>" + "917019201122" + "</tpay:customer_mobile_no>" +
                "</tpay:Req>" +
                "</tpay:CheckBalance>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
