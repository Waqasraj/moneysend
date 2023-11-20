package moneylink.wallet.Mobile_Top_Up;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import moneylink.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityMobileTopUpMainBinding;

public class MobileTopUpMainActivity extends TootiBaseActivity<ActivityMobileTopUpMainBinding> {

    NavController navController;
    public MobileTopUpViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        viewModel = new ViewModelProvider(this).get(MobileTopUpViewModel.class);

        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.titleTxt.setText(getString(R.string.mobile_top_up));
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.mobileTopUpFirstActivity
                || navController.getCurrentDestination().getId() == R.id.payBillFragment) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}