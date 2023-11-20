package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;
import moneylink.wallet.login_signup_module.helper.RegisterUserRequest;

public class EditCustomerProfileRequest {
    public RegisterUserRequest customer = new RegisterUserRequest();
    public String customerNo;
    public String idNumber;
    public Integer idType;
    public String idIssueDate;
    public String idExpireDate;
    public String residenceCountry;
    public String languageId = "1";


    public String getXML() {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:EditCustomerProfile>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:FirstName>" + customer.firstName + "</tpay:FirstName>" +
                "<tpay:MiddleName>" + customer.middleName + "</tpay:MiddleName>" +
                "<tpay:LastName>" + customer.lastName + "</tpay:LastName>" +
                "<tpay:Address>" + customer.address + "</tpay:Address>" +
                "<tpay:DateOfBirth>" + customer.dob + "</tpay:DateOfBirth>" +
                "<tpay:Gender>" + customer.gender + "</tpay:Gender>" +
                "<tpay:MobileNumber>" + customer.phoneNumber + "</tpay:MobileNumber>" +
                "<tpay:Nationality>" + customer.nationality + "</tpay:Nationality>" +
                "<tpay:IDNumber>" + idNumber + "</tpay:IDNumber>" +
                "<tpay:IDType>" + idType + "</tpay:IDType>" +
                "<tpay:IDIssueDate>" + idIssueDate + "</tpay:IDIssueDate>" +
                "<tpay:IDExpiryDate>" + idExpireDate + "</tpay:IDExpiryDate>" +
                "<tpay:IsActive>" + "1" + "</tpay:IsActive>" +
                "<tpay:SourceOfFund>" + "2" + "</tpay:SourceOfFund>" +
                "<tpay:EmailID>" + customer.email + "</tpay:EmailID>" +
                "<tpay:IDtype_Description>" + "1" + "</tpay:IDtype_Description>" +
                "<tpay:ResidenceCountry>" + residenceCountry + "</tpay:ResidenceCountry>" +
                "<tpay:SourceOfFund_Desc>" + "123" + "</tpay:SourceOfFund_Desc>" +
                "</tpay:Req>" +
                "</tpay:EditCustomerProfile>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

        return xml;
    }
}
