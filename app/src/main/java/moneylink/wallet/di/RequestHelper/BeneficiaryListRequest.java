package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.StaticHelper;

public class BeneficiaryListRequest {
    public String customerNo;
    public String languageId = "1";

    public String getXML() {
        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tpay=\"TPay\">"
                + "<soapenv:Header/>" +
                "<soapenv:Body>"+
                "<tpay:GetBeneficiaryList>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "</tpay:Req>" +
                "</tpay:GetBeneficiaryList>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return xml;
    }
}
