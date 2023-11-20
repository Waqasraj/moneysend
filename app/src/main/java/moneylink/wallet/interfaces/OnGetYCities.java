package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.YCityResponse;

public interface OnGetYCities extends OnMessageInterface {
    void onGetCities(List<YCityResponse> citiesList);

    void onSelectYCity(YCityResponse city);
}
