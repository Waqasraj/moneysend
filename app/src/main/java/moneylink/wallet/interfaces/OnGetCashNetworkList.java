package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetCashNetworkListResponse;

import java.util.List;

public interface OnGetCashNetworkList extends OnMessageInterface {
    void onGetNetworkList(List<GetCashNetworkListResponse> networkLists);
}
