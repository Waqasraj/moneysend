package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class BeneficiaryAddRequest {
    public int countryID;
    public int cityID = -1;
    public int locationID = -1;

    public String customerNo;
    public String FirstName;  //
    public String MiddleName = ""; //
    public String LastName; //
    public String Address; //
    public String Telephone;
    public String PayOutCurrency;
    public String PaymentMode;
    public String PayOutBranchCode = "";
    public String BankName = "";
    public String BankCountry = "";
    public String BranchNameAndAddress = "";
    public String BankCode = "";
    public String AccountNumber = "";
    public String PurposeCode = "1";
    public String PayoutCountryCode;
    public String CustomerRelation;
    public String BankBranch = "";

    //extra variables
    public Integer beneficiaryCountryId;
    public String beneficiaryNo;
    public String languageId = "1";
    public int countryRegion;

    public String ipAddress = "";
    public String ipCountry = "";

    public String countryCode = "+1";
    public String countryFlag = "";
    public String countryName = "";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:BeneficiaryRegistration>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:FirstName>" + FirstName + "</tpay:FirstName>" +
                "<tpay:MiddleName>" + MiddleName + "</tpay:MiddleName>" +
                "<tpay:LastName>" + LastName + "</tpay:LastName>" +
                "<tpay:Address>" + Address + "</tpay:Address>" +
                "<tpay:Telephone>" + Telephone + "</tpay:Telephone>" +
                "<tpay:PayOutCurrency>" + PayOutCurrency + "</tpay:PayOutCurrency>" +
                "<tpay:PaymentMode>" + PaymentMode + "</tpay:PaymentMode>" +
                "<tpay:PayOutBranchCode>" + PayOutBranchCode + "</tpay:PayOutBranchCode>" +
                "<tpay:BankName>" + BankName + "</tpay:BankName>" +
                "<tpay:BankCountry>" + BankCountry + "</tpay:BankCountry>" +
                "<tpay:BranchNameAndAddress>" + BranchNameAndAddress + "</tpay:BranchNameAndAddress>" +
                "<tpay:BankCode>" + BankCode + "</tpay:BankCode>" +
                "<tpay:AccountNumber>" + AccountNumber + "</tpay:AccountNumber>" +
                "<tpay:PurposeCode>" + PurposeCode + "</tpay:PurposeCode>" +
                "<tpay:PayoutCountryCode>" + PayoutCountryCode + "</tpay:PayoutCountryCode>" +
                "<tpay:CustomerRelation>" + CustomerRelation + "</tpay:CustomerRelation>" +
                "<tpay:BankBranch>" + BankBranch + "</tpay:BankBranch>" +
                "<tpay:IpAddress>" + ipAddress + "</tpay:IpAddress>" +
                "<tpay:IpCountryName>" + ipCountry + "</tpay:IpCountryName>" +
                "</tpay:Req>" +
                "</tpay:BeneficiaryRegistration>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
