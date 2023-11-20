package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class B2BTransferDetails {
    public String receivingCountry = "";
    public String customerNo;
    public String beneName = "";
    public String beneAddress = "";
    public String beneContactNo = "";
    public String detailsOfPayment = "";
    public String payInAmount = "";
    public String payOutAmount = "";
    public String charges = "50";
    public String purposeOfTransfer = "";
    public String bankName = "";
    public String accountNumber = "";
    public String bankBranch = "";
    public String bankAddress = "";
    public String swiftBIC = "";
    public String correspondentBank = "";
    public String cbSwiftBIC = "";
    public String cbAccountNumber = "";
    public String languageId = "1";
    public String receivingCurrency;

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:B2BTransferDetails>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_Number>" + customerNo + "</tpay:Customer_Number>" +
                "<tpay:Bene_Name>" + beneName + "</tpay:Bene_Name>" +
                "<tpay:Bene_Address>" + beneAddress + "</tpay:Bene_Address>" +
                "<tpay:Bene_Contact_Number>" + beneContactNo + "</tpay:Bene_Contact_Number>" +
                "<tpay:Details_Of_Payment>" + detailsOfPayment + "</tpay:Details_Of_Payment>" +
                "<tpay:PayIn_Amount>" + payInAmount + "</tpay:PayIn_Amount>" +
                "<tpay:PayOut_Amount>" + payOutAmount + "</tpay:PayOut_Amount>" +
                "<tpay:Charges>" + "50" + "</tpay:Charges>" +
                "<tpay:Receiving_Currency>" + receivingCurrency + "</tpay:Receiving_Currency>" +
                "<tpay:Receiving_Country>" + receivingCountry + "</tpay:Receiving_Country>" +
                "<tpay:Purpose_Of_Transfer>" + purposeOfTransfer + "</tpay:Purpose_Of_Transfer>" +
                "<tpay:Bank_Name>" + bankName + "</tpay:Bank_Name>" +
                "<tpay:Bank_Branch>" + bankBranch + "</tpay:Bank_Branch>" +
                "<tpay:Bank_Address>" + bankAddress + "</tpay:Bank_Address>" +
                "<tpay:Swift_BIC>" + swiftBIC + "</tpay:Swift_BIC>" +
                "<tpay:Correspondent_Bank>" + correspondentBank + "</tpay:Correspondent_Bank>" +
                "<tpay:CB_Swift_BIC>" + cbSwiftBIC + "</tpay:CB_Swift_BIC>" +
                "<tpay:CB_Account_Number>" + cbAccountNumber + "</tpay:CB_Account_Number>" +
                "</tpay:Req>" +
                "</tpay:B2BTransferDetails>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
