package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.GetWRCountryListResponseC;

public interface OnWRCountryList extends OnMessageInterface {
    void onWRCountryList(List<GetWRCountryListResponseC> list);
    void onWRSelectCountry(GetWRCountryListResponseC country);
}
