package moneylink.wallet.swift_transfer_module.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.View;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentSwiftTransferPaymentDetailBinding;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.dialogs.SwiftPurposeOfTransferDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.interfaces.OnSwiftPurposeTransferSelection;
import moneylink.wallet.swift_transfer_module.SwiftTransferActivity;
import moneylink.wallet.utils.IsNetworkConnection;


public class    SwiftTransferPaymentDetailFragment extends
        BaseFragment<FragmentSwiftTransferPaymentDetailBinding>
        implements OnSwiftPurposeTransferSelection, OnSelectCountry {


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.receivingCountry.getText().toString())) {
            onMessage(getString(R.string.enter_rece_country));
            return false;
        } else if (TextUtils.isEmpty(binding.receivingAmount.getText().toString())) {
            onMessage(getString(R.string.enter_the_amount));
            return false;
        } else if (TextUtils.isEmpty(binding.purposeOfTransfer.getText().toString())) {
            onMessage(getString(R.string.select_the_purpose_txt));
            return false;
        } else if (binding.otherPurposeOfTransfer.getVisibility() == View.VISIBLE
                && TextUtils.isEmpty(binding.otherPurposeOfTransfer.getText())) {
            onMessage(getString(R.string.enter_purpose_of_transfer));
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

    private void fillData() {
        binding.receivingCountry.setText(((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.receivingCountry);
        binding.receivingAmount.setText(((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.payOutAmount);
        binding.charges.setText(((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.charges);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {

                ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.payInAmount = "0.0";
                ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.payOutAmount = binding.receivingAmount.getText().toString();
                ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.charges = binding.charges.getText().toString();


                if (binding.otherPurposeOfTransfer.getVisibility() == View.VISIBLE) {
                    ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.purposeOfTransfer
                            = binding.purposeOfTransfer.getText().toString();
                } else {
                    ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.purposeOfTransfer
                            = binding.otherPurposeOfTransfer.getText().toString();
                }

                Navigation.findNavController(v)
                        .navigate(R.id
                                .action_swiftTransferPaymentDetailFragment_to_swiftTransferBeneficiaryBankDetailsFragment);
            }

        });


        binding.purposeOfTransfer.setOnClickListener(v -> {
            SwiftPurposeOfTransferDialog dialog = new SwiftPurposeOfTransferDialog(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });


        binding.receivingCountry.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry dialogCountry = new DialogCountry(this, false,false);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                dialogCountry.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swift_transfer_payment_detail;
    }

    @Override
    public void onSwiftPurpose(String text, int index) {
        if (index == 1) {
            binding.otherTitle.setVisibility(View.VISIBLE);
            binding.otherPurposeOfTransfer.setVisibility(View.VISIBLE);
        } else {
            binding.otherTitle.setVisibility(View.GONE);
            binding.otherPurposeOfTransfer.setVisibility(View.GONE);
        }

        binding.purposeOfTransfer.setText(text);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.receivingCountry.setText(country.countryName);
        ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.receivingCountry =
                country.countryShortName;
        ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails.receivingCurrency =
                country.currencyShortName;
    }
}