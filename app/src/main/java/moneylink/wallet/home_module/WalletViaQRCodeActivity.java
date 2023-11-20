package moneylink.wallet.home_module;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityWalletViaQRCodeBinding;

public class WalletViaQRCodeActivity extends TootiBaseActivity<ActivityWalletViaQRCodeBinding> {

    private NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_via_q_r_code;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);

        binding.toolBar.titleTxt.setText(getString(R.string.scan_code));


        binding.toolBar.backBtn.setOnClickListener(v->{
            super.onBackPressed();
        });

        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }


    public void changeTitle(String title) {
        binding.toolBar.titleTxt.setText(title);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.barcodeReaderFragment
         || navController.getCurrentDestination().getId() == R.id.walletToWalletViaQrCode) {
            finish();
        } else {
            navController.navigateUp();
        }
    }

}