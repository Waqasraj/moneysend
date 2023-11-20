package moneylink.wallet.my_bank_cards;

import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityAvalableBanksBinding;

public class AvailableBanksActivity extends TootiBaseActivity<ActivityAvalableBanksBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_avalable_banks;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}