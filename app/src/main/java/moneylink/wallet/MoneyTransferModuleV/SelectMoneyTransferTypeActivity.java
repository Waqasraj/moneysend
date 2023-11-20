package moneylink.wallet.MoneyTransferModuleV;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.MoneyTransferModuleV.wallettransfer.local.WalletTransferFirstActivity;
import moneylink.wallet.databinding.ActivitySelectTypeBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.utils.BeneficiarySelector;

public class SelectMoneyTransferTypeActivity extends BaseFragment<ActivitySelectTypeBinding>
        implements View.OnClickListener {

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity())
                .binding.toolBar.titleTxt.setText(getString(R.string.app_name));
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.bankTransferLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("transfer_type", BeneficiarySelector.BANK_TRANSFER);
            Navigation.findNavController(v).navigate(R.id
                    .action_selectMoneyTransferTypeActivity_to_selectedBeneficiaryForBankTransferFragment, bundle);
            // startActivity(new Intent(getActivity(), LoyalityPointsActivity.class));
        });


        binding.walletTransferLayout.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_selectMoneyTransferTypeActivity_to_local_transfer_navigation);
            //   startActivity(new Intent(getContext(), WalletTransferFirstActivity.class));
        });


        binding.cashTransfer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("transfer_type", BeneficiarySelector.CASH_TRANSFER);
            Navigation.findNavController(v)
                    .navigate(R.id.action_selectMoneyTransferTypeActivity_to_cash_transfer_navigation, bundle);
        });


        binding.doorToDoorLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("transfer_type", BeneficiarySelector.DOOR_TO_DOOR);
            Navigation.findNavController(v)
                    .navigate(R.id.action_selectMoneyTransferTypeActivity_to_cash_transfer_navigation, bundle);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_type;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_transfer_layout:
                // startActivity(new Intent(getApplicationContext(), WalletTransferActivity.class));
                startActivity(new Intent(getContext(), WalletTransferFirstActivity.class));

                break;
            case R.id.bank_transfer_layout:

                break;
            case R.id.cash_transfer:
                //startActivity(new Intent(getApplicationContext(), NoBaneficialyActivity.class));
//                Intent intent = new Intent(getContext(), NoBaneficialyActivity.class);
//                intent.putExtra("Type","Cash");
//                startActivity(intent);
                break;
            case R.id.door_to_door_layout:
//                Intent intent1 = new Intent(getContext(), NoBaneficialyActivity.class);
//                intent1.putExtra("Type","door_to_door");
//                startActivity(intent1);

                break;
            case R.id.local_transfer_layout:
                break;

        }
    }
}
