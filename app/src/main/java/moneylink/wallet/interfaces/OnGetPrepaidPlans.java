package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.GetPrepaidPlansResponse;

public interface OnGetPrepaidPlans extends OnMessageInterface {
    void onGetPrepaidPlans(List<GetPrepaidPlansResponse> plansList);

    void onSelectPlan(GetPrepaidPlansResponse plan);
}
