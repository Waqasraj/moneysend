package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetTransactionReceiptResponse;

public interface OnSelectTransaction {
    void onSelectTransaction(GetTransactionReceiptResponse receiptResponse);
    void onSelectTransactionReceipt(String txtNumber);
    void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse);
}
