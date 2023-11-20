package moneylink.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.ShowCardDetailsBinding;
import moneylink.wallet.di.ResponseHelper.GetCardDetailsResponse;
import moneylink.wallet.utils.StringHelper;

public class ShowCardDetailsDialog extends BaseDialogFragment<ShowCardDetailsBinding> {

    GetCardDetailsResponse cardDetails;


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public ShowCardDetailsDialog(GetCardDetailsResponse cardDetails) {
        this.cardDetails = cardDetails;
    }

    @Override
    public int getLayoutId() {
        return R.layout.show_card_details;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.txtCardholdername.setText(cardDetails.cardName);
        binding.txtExpireMonthyear.setText(cardDetails.cardExpireDate);
        binding.txtNumber.setText(StringHelper.addDishes(cardDetails.cardNumber));


        binding.toolBar.backBtn.setOnClickListener(v -> {
            cancelUpload();
        });
    }
}
