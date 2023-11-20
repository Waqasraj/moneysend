package moneylink.wallet.menumodules;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityTermsBinding;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class TermsActivity extends TootiBaseActivity<ActivityTermsBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_terms;
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void initUi(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.termsText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}