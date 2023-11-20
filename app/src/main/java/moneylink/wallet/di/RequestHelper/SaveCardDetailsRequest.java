package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class SaveCardDetailsRequest {
    public String customerNo;
    public String customerCardNo;
    public String cardName;
    public String cardExpireDate;
    public String languageId;

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:SaveCardDetails>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:Customer_CardName>" + cardName + "</tpay:Customer_CardName>" +
                "<tpay:Card_Number>" + customerCardNo + "</tpay:Card_Number>" +
                "<tpay:CardExpiry_Date>" + cardExpireDate + "</tpay:CardExpiry_Date>" +
                "</tpay:Req>" +
                "</tpay:SaveCardDetails>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }

}
