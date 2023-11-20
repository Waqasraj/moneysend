package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.PurposeOfTransferListResponse;

public interface OnSelectTransferPurpose {
    void onSelectTransferPurpose(PurposeOfTransferListResponse response);
}
