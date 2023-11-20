package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.WRBillDetailsResponse;

public interface OnWRBillDetail extends OnMessageInterface {
    void onBillDetails(WRBillDetailsResponse response);
}
