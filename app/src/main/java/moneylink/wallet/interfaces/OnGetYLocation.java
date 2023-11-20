package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.YLocationResponse;

public interface OnGetYLocation extends OnMessageInterface {
    void onGetYLocations(List<YLocationResponse> yLocations);

    void onSelectYLocation(YLocationResponse location);
}
