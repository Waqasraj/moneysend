package moneylink.wallet.di.RequestHelper;


import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class CheckIDRequiredRequest {
    public String countryID;
    public String currencyID;
    public String txnAmount;

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:CheckIDRequired>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "</tpay:Credentials>" +
                "<tpay:CountryID>" + "10" + "</tpay:CountryID>" +
                "<tpay:CurrencyID>" + "1" + "</tpay:CurrencyID>" +
                "<tpay:txn_amt>" + txnAmount + "</tpay:txn_amt>" +
                "</tpay:Req>" +
                "</tpay:CheckIDRequired>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
