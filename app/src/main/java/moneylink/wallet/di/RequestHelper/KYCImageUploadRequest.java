package moneylink.wallet.di.RequestHelper;

import moneylink.wallet.di.Params;
import moneylink.wallet.di.StaticHelper;

public class KYCImageUploadRequest {
    public String imageName;
    public byte[] imagePath;
    public String customerNo;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:UploadKYCImage>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE+ "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.UserPassword + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:Image>" + imagePath + "</tpay:Image>" +
                "<tpay:Image_Name>" + imageName + "</tpay:Image_Name>" +
                "</tpay:Req>" +
                "</tpay:UploadKYCImage>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
