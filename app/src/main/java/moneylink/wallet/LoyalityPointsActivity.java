package moneylink.wallet;

import android.os.Bundle;

import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityLoyalityPointsBinding;

public class LoyalityPointsActivity extends TootiBaseActivity<ActivityLoyalityPointsBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_loyality_points;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}