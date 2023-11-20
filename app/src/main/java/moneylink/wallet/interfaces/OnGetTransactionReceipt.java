package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetTransactionReceiptResponse;

public interface OnGetTransactionReceipt extends OnMessageInterface {
    void onGetTransactionReceipt(GetTransactionReceiptResponse receiptResponse);
}
