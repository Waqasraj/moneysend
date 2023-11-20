package moneylink.wallet.beneficiary_list_module;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityBeneficiaryListBinding;

public class BeneficiaryListActivity extends TootiBaseActivity<ActivityBeneficiaryListBinding> {


    NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_beneficiary_list;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);


        binding.toolBar.backBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.beneficiaryListFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}