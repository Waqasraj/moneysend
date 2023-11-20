package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class WRBillDetailRequest {
    public String languageID;
    public String customerNo;
    public String countryCode;
    public Integer billerID;
    public Integer skuID;
    public String field1 = "";
    public String field2 = "";
    public String field3 = "";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:WRBillDetail>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:countryCode>" + countryCode + "</tpay:countryCode>" +
                "<tpay:BillerID>" + billerID + "</tpay:BillerID>" +
                "<tpay:SkuID>" + skuID + "</tpay:SkuID>" +
                "<tpay:Field1>" + field1 + "</tpay:Field1>" +
                "<tpay:Field2>" + field2 + "</tpay:Field2>" +
                "<tpay:Field3>" + field3 + "</tpay:Field3>" +
                "</tpay:Req>" +
                "</tpay:WRBillDetail>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
