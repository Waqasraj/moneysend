package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class CreateWalletRequest {
    public String customerNo;
    public String walletCurrency;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:CreateWallet>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_Number>" + customerNo + "</tpay:Customer_Number>" +
                "<tpay:Wallet_Currency>" + walletCurrency + "</tpay:Wallet_Currency>" +
                "</tpay:Req>" +
                "</tpay:CreateWallet>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
