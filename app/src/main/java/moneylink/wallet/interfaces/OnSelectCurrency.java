package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;

import java.util.List;

public interface OnSelectCurrency extends OnMessageInterface{
    void onCurrencyResponse(List<GetSendRecCurrencyResponse> response);
    void onCurrencySelect(GetSendRecCurrencyResponse response);
}
