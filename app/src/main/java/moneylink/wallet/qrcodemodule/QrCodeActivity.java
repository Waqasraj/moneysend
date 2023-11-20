package moneylink.wallet.qrcodemodule;


import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.QrCodeDesignBinding;
import moneylink.wallet.scanqrcodemodule.modals.BarcodeGenerate;
import moneylink.wallet.utils.CaptureScreenShot;
import moneylink.wallet.utils.ShareIntent;

public class QrCodeActivity extends TootiBaseActivity<QrCodeDesignBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.qr_code_design;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.qrCodeImage.setImageBitmap(BarcodeGenerate.createBarCode(sessionManager.getCustomerNo()));

        binding.toolBar.backBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });


        binding.share.setOnClickListener(v -> {
            ShareIntent intent = new ShareIntent(this);
            intent.shareImage(CaptureScreenShot.getScreenShot(binding.shareView));
        });


        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
