package moneylink.wallet.dialogs;

import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.AlertDialogLayoutBinding;
import moneylink.wallet.interfaces.OnDecisionMade;

public class AlertDialog extends BaseDialogFragment<AlertDialogLayoutBinding> {

    String title;
    String message;
    OnDecisionMade onDecisionMade;

    public AlertDialog(String title, String message, OnDecisionMade onDecisionMade) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
    }

    @Override
    public int getLayoutId() {
        return R.layout.alert_dialog_layout;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        if(title != "") {
            binding.title.setText(title);
            binding.message.setText(message);
        }

        binding.cancelButton.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            onDecisionMade.onProceed();
        });
    }
}
