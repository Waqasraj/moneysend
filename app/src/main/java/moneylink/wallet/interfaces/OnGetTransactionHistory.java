package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.TransactionHistoryResponse;

import java.util.List;

public interface OnGetTransactionHistory extends OnMessageInterface {
    void onGetHistoryList(List<TransactionHistoryResponse> historyList);
}
