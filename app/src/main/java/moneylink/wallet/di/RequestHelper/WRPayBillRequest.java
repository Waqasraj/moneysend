package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.JSONdi.restRequest.paykiiMobileToup.Cred;

public class WRPayBillRequest {
    public Cred Credentials = new Cred();
    public String input1;
    public String Receiving_CountryCode;
    public String Send_Currency;
    public String BillerID;
    public String SkuID;
    public String PayOutAmount = "";
    public String Payment_TypeID;
    public String IpCountryName = "";
    public String IpAddress = "";
    public String Card_Number = "";
    public String ExpiryDate = "";
    public String SecurityCode = "";
        public String Customer_No = "";

}
