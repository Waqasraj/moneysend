package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetCashNetworkListResponse;

public interface OnSelectPayoutAgent {
    void onSelectPayoutAgent(GetCashNetworkListResponse response);
}
