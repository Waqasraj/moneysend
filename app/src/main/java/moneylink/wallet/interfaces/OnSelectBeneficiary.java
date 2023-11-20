package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetBeneficiaryListResponse;

public interface OnSelectBeneficiary {
    void onSelectBeneficiary(GetBeneficiaryListResponse response);
}
