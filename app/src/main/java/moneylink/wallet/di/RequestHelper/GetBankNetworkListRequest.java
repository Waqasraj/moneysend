package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetBankNetworkListRequest {
    public String countryCode = "";
    public String branchIFSC = "";
    public String city = "city";
    public String bankName = "";
    public String branchName = "";
    public String languageId = "1";


    public String getXML() {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetBankNetworkList>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryCode>" + countryCode + "</tpay:CountryCode>" +
                "<tpay:BankName>" + bankName + "</tpay:BankName>" +
                "<tpay:BranchName>" + branchName + "</tpay:BranchName>" +
                "<tpay:City>" + city + "</tpay:City>" +
                "<tpay:BranchIFSC>" + branchIFSC + "</tpay:BranchIFSC>" +
                "</tpay:Req>" +
                "</tpay:GetBankNetworkList>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

        return xml;
    }
}
