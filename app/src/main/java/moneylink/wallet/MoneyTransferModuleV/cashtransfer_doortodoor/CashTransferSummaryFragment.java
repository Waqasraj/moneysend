package moneylink.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.os.Bundle;

import androidx.navigation.Navigation;

import moneylink.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentMoneyTransferSummaryBinding;
import moneylink.wallet.fragments.BaseFragment;

public class CashTransferSummaryFragment extends BaseFragment<FragmentMoneyTransferSummaryBinding> {

    String totalPayable;
    boolean isCash = true;

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.summary));
    }


    @Override
    protected void injectView() {

    }


    @Override
    protected void setUp(Bundle savedInstanceState) {

        assert getArguments() != null;
        totalPayable = getArguments().getString("total_payable");
        isCash = getArguments().getBoolean("is_from_cash" , true);

        binding.beneficairyName.setText(((((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.firstName.concat(" ").concat(
                        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                                .beneficiaryDetails.lastName
                ))));

        binding.sendingCurrency.setText(((MoneyTransferMainLayout) getBaseActivity()).
                bankTransferViewModel.request.payInCurrency);

        binding.transferAmount.setText(((MoneyTransferMainLayout) getBaseActivity()).
                bankTransferViewModel.request.transferAmount.toString()
         .concat(" ").concat(((MoneyTransferMainLayout) getBaseActivity()).
                        bankTransferViewModel.request.payOutCurrency));

        binding.totalPayableAmount.setText(totalPayable.concat(" ")
         .concat(((MoneyTransferMainLayout) getBaseActivity()).
                 bankTransferViewModel.request.payInCurrency));


        binding.confirmBtn.setOnClickListener(v -> {
            if(!isCash) {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_cashTransferSummaryFragment2_to_cashPaymentFourthActivity2);
            } else {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.cashPaymentFourthActivity);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_money_transfer_summary;
    }

}