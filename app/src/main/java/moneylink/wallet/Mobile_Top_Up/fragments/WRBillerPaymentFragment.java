package moneylink.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import moneylink.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import moneylink.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import moneylink.wallet.di.JSONdi.restResponse.paykiiResponse.GetPaymentDetailsResponse;
import moneylink.wallet.dialogs.SingleButtonMessageDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnDecisionMade;

public class WRBillerPaymentFragment extends BaseFragment<FragmentUtilityBillerDetailsBinding>
        implements OnDecisionMade {

    int selectedID = 4;
    MobileTopUpViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        selectedID = binding.cashWallet.getId();
        binding.paymentOptionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            selectedID = radioButton.getId();
            Log.e("setUp: ", String.valueOf(selectedID));
        });


        binding.cashWallet.setChecked(true);

        showBillData(viewModel.getPaymentDetailsResponse);

        binding.toPayment.setOnClickListener(v -> {
            switch (selectedID) {
                case R.id.cash_wallet:
                    payBill();
                    break;
                case R.id.radio_wallet:
                case R.id.bank_deposit:
                case R.id.radio_thorugh_card:
                    showError();
                    break;

            }


        });
    }


    void showError() {
        SingleButtonMessageDialog dialog = new SingleButtonMessageDialog("Alert", "Please select cash as payment for testing"
                , this, true);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");

    }

    void payBill() {
        viewModel.payBillRequest.Payment_TypeID = "4";
        viewModel.payBillRequest.IpCountryName = getSessionManager().getIpCountryName();
        viewModel.payBillRequest.IpAddress = getSessionManager().getIpAddress();

        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.payBillFragment);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_biller_details;
    }

    public void showBillData(GetPaymentDetailsResponse data) {
        binding.customerName.setText(data.billerName);
        binding.billAmount.setText(data.payoutCurrency.concat(" ").concat(data.exchangeRate));
        binding.netBillAmount.setText(data.payoutCurrency.concat(" ").concat(data.totalPayable));

        binding.mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel() {

    }
}
