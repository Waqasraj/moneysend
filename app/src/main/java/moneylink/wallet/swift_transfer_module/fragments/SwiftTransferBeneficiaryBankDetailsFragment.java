package moneylink.wallet.swift_transfer_module.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;

import android.text.TextUtils;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentSwiftTransferBeneficiaryBankDetailsBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.swift_transfer_module.SwiftTransferActivity;

public class SwiftTransferBeneficiaryBankDetailsFragment
        extends BaseFragment<FragmentSwiftTransferBeneficiaryBankDetailsBinding> {

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {

        if (TextUtils.isEmpty(binding.bankName.getText().toString())) {
            onMessage(getString(R.string.select_bank_name));
            return false;
        } else if (TextUtils.isEmpty(binding.accountNo.getText())) {
            onMessage(getString(R.string.enter_account_no_error));
            return false;
        } else if (TextUtils.isEmpty(binding.bankBranch.getText())) {
            onMessage(getString(R.string.enter_bank_branch));
            return false;
        } else if (TextUtils.isEmpty(binding.bankAddress.getText())) {
            onMessage(getString(R.string.bankaddress_txt_error));
            return false;
        } else if (TextUtils.isEmpty(binding.swiftBic.getText())) {
            onMessage(getString(R.string.plz_enter_swift_ibc));
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    private void fillData() {
        binding.bankName.setText( ((SwiftTransferActivity) getBaseActivity())
                .b2BTransferDetails.bankName);
        binding.bankBranch.setText( ((SwiftTransferActivity) getBaseActivity())
                .b2BTransferDetails.bankBranch);
        binding.accountNo.setText( ((SwiftTransferActivity) getBaseActivity())
                .b2BTransferDetails.accountNumber);
        binding.bankAddress.setText( ((SwiftTransferActivity) getBaseActivity())
                .b2BTransferDetails.bankAddress);
        binding.swiftBic.setText(((SwiftTransferActivity) getBaseActivity())
                .b2BTransferDetails.swiftBIC);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                ((SwiftTransferActivity) getBaseActivity())
                        .b2BTransferDetails.bankName = binding.bankName.getText().toString();
                ((SwiftTransferActivity) getBaseActivity())
                        .b2BTransferDetails.bankBranch = binding.bankBranch.getText().toString();
                ((SwiftTransferActivity) getBaseActivity())
                        .b2BTransferDetails.accountNumber = binding.accountNo.getText().toString();
                ((SwiftTransferActivity) getBaseActivity())
                        .b2BTransferDetails.bankAddress = binding.bankAddress.getText().toString();
                ((SwiftTransferActivity) getBaseActivity())
                        .b2BTransferDetails.swiftBIC = binding.swiftBic.getText().toString();
                Navigation.findNavController(v)
                        .navigate(R.id.action_swiftTransferBeneficiaryBankDetailsFragment_to_swiftTransferPreferredCorrespondentDetailsFragment);

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swift_transfer_beneficiary_bank_details;
    }

}