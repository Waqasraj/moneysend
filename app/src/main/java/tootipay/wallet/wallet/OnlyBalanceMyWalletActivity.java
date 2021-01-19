package tootipay.wallet.wallet;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import tootipay.wallet.R;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.OnlyBalanceActivityMyWalletBinding;
import tootipay.wallet.di.RequestHelper.WalletBalanceRequest;
import tootipay.wallet.di.apicaller.WalletBalanceRequestTask;
import tootipay.wallet.interfaces.OnWalletBalanceReceived;
import tootipay.wallet.utils.IsNetworkConnection;

public class OnlyBalanceMyWalletActivity extends TootiBaseActivity<OnlyBalanceActivityMyWalletBinding>
        implements OnWalletBalanceReceived {


    @Override
    public int getLayoutId() {
        return R.layout.only_balance_activity_my_wallet;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);

        if (IsNetworkConnection.checkNetworkConnection(this)) {
            WalletBalanceRequest request = new WalletBalanceRequest();
            request.customerNo = sessionManager.getCustomerNo();
            request.languageId = sessionManager.getlanguageselection();
            WalletBalanceRequestTask task = new WalletBalanceRequestTask(this, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }


        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }

    @Override
    public void onWalletBalanceReceived(String walletBalance) {
        binding.walletBalance.setText(walletBalance);
    }

    @Override
    public void onLockWalletOption(boolean isLocked) {

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}