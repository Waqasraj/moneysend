package moneylink.wallet.interfaces;


import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import moneylink.wallet.di.ResponseHelper.WRCountryListResponse;

public interface OnPrepaidCountry {
    void onSelectPrepaidCountry(WRCountryListResponse wrCountryListResponse);
    void onPrepaidOperator(PrepaidOperatorResponse prepaidOperatorResponse);
}
