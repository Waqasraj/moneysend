package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.CalTransferResponse;

public interface OnGetTransferRates extends OnMessageInterface {
    void onGetTransferRates(CalTransferResponse response);
}
