package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

public interface OnWalletHistoryResponse extends OnMessageInterface {
    void onHistory(List<WalletTransferHistoryResponse> list);
}
