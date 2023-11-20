package moneylink.wallet.mywalletmoduleV.fragments;


import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;


import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivityAddMoneyWalletBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.MoneyValueFilter;
import moneylink.wallet.utils.NumberFormatter;

import static moneylink.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class LoadWalletFragment extends BaseFragment<ActivityAddMoneyWalletBinding> {

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.walletAmount.getText().toString())) {
            onMessage(getString(R.string.enter_the_amount));
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.loadCurrency.setText(getArguments().getString("wallet_type"));

        Constants.showKeyBoard(getContext());
        binding.walletAmount.requestFocus();

        binding.procedPayWallet.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.walletAmount.getText().toString())) {
                Bundle bundle = new Bundle();
                bundle.putString("load_amount", NumberFormatter.removeCommas(
                        binding.walletAmount.getText().toString()));
                bundle.putString("load_currency", binding.loadCurrency.getText().toString());
                Navigation.findNavController(getView())
                        .navigate(R.id.action_AddMoneyWalletActivity_to_choosePaymentMethodForWalletLoadFragment
                                , bundle);
            } else {
                onMessage(getString(R.string.enter_your_amount_you_want_to_load));
            }


        });


        binding.walletAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int cursorPosition = binding.walletAmount.getSelectionEnd();
                String originalStr = binding.walletAmount.getText().toString();

                //To restrict only two digits after decimal place
                binding.walletAmount.setFilters(new InputFilter[]{new MoneyValueFilter(2)});

                try {
                    binding.walletAmount.removeTextChangedListener(this);
                    String value = binding.walletAmount.getText().toString();

                    if (value != null && !value.equals("")) {
                        if (value.startsWith(".")) {
                            binding.walletAmount.setText("0.");
                        }
                        if (value.startsWith("0") && !value.startsWith("0.")) {
                            binding.walletAmount.setText("");
                        }
                        String str = binding.walletAmount.getText().toString().replaceAll(",", "");
                        if (!value.equals(""))
                            binding.walletAmount.setText(getDecimalFormattedString(str));

                        int diff = binding.walletAmount.getText().toString().length() - originalStr.length();
                        binding.walletAmount.setSelection(cursorPosition + diff);

                    }
                    binding.walletAmount.addTextChangedListener(this);
                } catch (Exception ex) {
                    Log.e("Textwatcher", ex.getMessage());
                    binding.walletAmount.addTextChangedListener(this);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_money_wallet;
    }

}