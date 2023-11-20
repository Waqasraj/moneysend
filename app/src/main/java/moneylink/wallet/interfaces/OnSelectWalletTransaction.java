package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

public interface OnSelectWalletTransaction {
    void onSelectWalletTransaction(WalletTransferHistoryResponse response);
    void onSelectReceipt(WalletTransferHistoryResponse response);
}
