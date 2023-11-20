package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;

public interface OnSelectCountry {
    void onSelectCountry(GetCountryListResponse country);
}
