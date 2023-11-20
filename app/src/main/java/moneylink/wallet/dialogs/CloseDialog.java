package moneylink.wallet.dialogs;

import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.CancelTransactionDialogBinding;
import moneylink.wallet.interfaces.OnCancelInterface;

public class CloseDialog extends BaseDialogFragment<CancelTransactionDialogBinding> {

    OnCancelInterface onCancelInterface;

    public CloseDialog(OnCancelInterface onCancelInterface) {
        this.onCancelInterface = onCancelInterface;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cancel_transaction_dialog;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.notNumber.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.confirmNumber.setOnClickListener(v -> {
            cancelUpload();
            onCancelInterface.onCancel();
        });
    }
}
