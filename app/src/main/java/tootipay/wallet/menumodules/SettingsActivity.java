package tootipay.wallet.menumodules;

import android.content.Intent;
import android.os.Bundle;

import tootipay.wallet.R;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityNewAboutBinding;

public class SettingsActivity extends TootiBaseActivity<ActivityNewAboutBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_new_about;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });


        binding.aboutusLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });

        binding.termsAndCondLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsActivity.class));
        });

        binding.privacyPolicyLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyActivity.class));
        });
    }
}