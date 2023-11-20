package moneylink.wallet.interfaces;

import moneylink.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;

public interface OnBillerPlans extends OnMessageInterface {
    //void onBillerPlans(List<WRBillerPlanResponse> plansList);

    void onBillerPlanSelect(PrepaidPlanResponse.MobileTopUpSKU plan);
}
