package moneylink.wallet.swift_transfer_module.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;

import android.text.TextUtils;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentSwiftBeneficairyDetailBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.swift_transfer_module.SwiftTransferActivity;

public class SwiftBeneficiaryDetailFragment extends
        BaseFragment<FragmentSwiftBeneficairyDetailBinding> {


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.beneName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene_error));
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    public void fillData() {
        if (((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails != null) {
            binding.beneName.setText(((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.beneName);
            binding.address.setText(((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.beneAddress);
            binding.contactNo.setText(((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.beneContactNo);
        }
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.beneName
                        = binding.beneName.getText().toString();
                ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.beneAddress
                        = binding.address.getText().toString();
                ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.beneContactNo
                        = binding.contactNo.getText().toString();

                Navigation.findNavController(v)
                        .navigate(R.id.action_swiftBeneficiaryDetailFragment_to_swiftTransferPaymentDetailFragment);
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swift_beneficairy_detail;
    }

}