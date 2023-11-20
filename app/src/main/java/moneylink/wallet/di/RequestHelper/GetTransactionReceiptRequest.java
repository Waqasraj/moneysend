package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class GetTransactionReceiptRequest {
    public String transactionNumber;
    public String clientTxnNumber = "000000";
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetTransactionReceipt>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:TransactionNumber>" + transactionNumber + "</tpay:TransactionNumber>" +
                "<tpay:ClientTxnNumber>" + clientTxnNumber + "</tpay:ClientTxnNumber>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE + "</tpay:PartnerCode>" +
                "</tpay:Req>" +
                "</tpay:GetTransactionReceipt>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
