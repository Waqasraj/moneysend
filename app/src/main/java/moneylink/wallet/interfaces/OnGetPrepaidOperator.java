package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;

public interface OnGetPrepaidOperator extends OnMessageInterface {
    void onGetPrepaidOperator(PrepaidOperatorResponse operator);

    void onErrorWithCode(String code, String error);
}
