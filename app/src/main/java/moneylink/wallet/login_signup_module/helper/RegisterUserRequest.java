package moneylink.wallet.login_signup_module.helper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class RegisterUserRequest {
    public String phoneNumber = "";
    public String email = "";
    public String OTP;
    public String firstName;
    public String lastName;
    public String middleName;
    public String dob = "";
    public String gender;
    public String country;
    public String city;
    public String address;
    public String nationality;
    public String languageID;
    public String ipAddress = "";
    public String ipCountry = "";


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:CustomerRegistration>"+
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:FirstName>" + firstName + "</tpay:FirstName>" +
                "<tpay:MiddleName>" + middleName + "</tpay:MiddleName>" +
                "<tpay:LastName>" + lastName + "</tpay:LastName>" +
                "<tpay:Address>" + address + "</tpay:Address>" +
                "<tpay:Gender>" + gender + "</tpay:Gender>" +
                "<tpay:MobileNumber>" + phoneNumber + "</tpay:MobileNumber>" +
                "<tpay:Nationality>" + nationality + "</tpay:Nationality>" +
                "<tpay:EmailID>" + email + "</tpay:EmailID>" +
                "<tpay:ResidenceCountry>" + country + "</tpay:ResidenceCountry>" +
                "<tpay:IpAddress>" + ipAddress + "</tpay:IpAddress>" +
                "<tpay:IpCountryName>" + ipCountry + "</tpay:IpCountryName>" +
                "</tpay:Req>" +
                "</tpay:CustomerRegistration>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
