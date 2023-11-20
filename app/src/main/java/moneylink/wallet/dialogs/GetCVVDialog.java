package moneylink.wallet.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.CvvNumberDialogDesignBinding;
import moneylink.wallet.interfaces.OnEnterCVv;

public class GetCVVDialog extends BaseDialogFragment<CvvNumberDialogDesignBinding> {


    OnEnterCVv onEnterCVv;

    public GetCVVDialog(OnEnterCVv onEnterCVv) {
        this.onEnterCVv = onEnterCVv;
    }


    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.cvv.getText().toString())) {
            onEnterCVv.onResponseMessage(getString(R.string.plz_enter_valid_cvv));
            return false;
        } else if (binding.cvv.getText().length() < 3) {
            onEnterCVv.onResponseMessage(getString(R.string.plz_enter_valid_cvv));
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cvv_number_dialog_design;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.cancelButton.setVisibility(View.GONE);


        binding.cancelButton.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.yes.setOnClickListener(v -> {
            if (isValidate()) {
                cancelUpload();
                onEnterCVv.onCVV(binding.cvv.getText().toString());
            }
        });
    }
}
