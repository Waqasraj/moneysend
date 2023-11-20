package moneylink.wallet.swift_transfer_module;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivitySwiftTransferBinding;
import moneylink.wallet.di.RequestHelper.B2BTransferDetails;

public class SwiftTransferActivity extends TootiBaseActivity<ActivitySwiftTransferBinding> {

    public B2BTransferDetails b2BTransferDetails;
    NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_swift_transfer;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        b2BTransferDetails = new B2BTransferDetails();
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText(getString(R.string.tooti_wallet_txt));
        binding.toolBar.titleTxt.setText(getString(R.string.swift_transfer));

        navController = Navigation.findNavController(this, R.id.dashboard);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }


    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.swiftBeneficiaryDetailFragment) {
            super.onBackPressed();
        } else {
            navController.navigateUp();
        }
    }
}