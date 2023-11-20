package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;

public interface OnSelectSourceOfIncome {
    void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response);
}
