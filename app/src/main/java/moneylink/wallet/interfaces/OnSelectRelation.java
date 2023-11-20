package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.GetRelationListResponse;

public interface OnSelectRelation {
    void onSelectRelation(GetRelationListResponse relation);
}
