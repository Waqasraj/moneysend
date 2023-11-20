package moneylink.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentMobilePaymentLoadBinding;
import moneylink.wallet.fragments.BaseFragment;

public class MobilePaymentLoadFragment extends BaseFragment<FragmentMobilePaymentLoadBinding> {


    public MobilePaymentLoadFragment() {
        // Required empty public constructor
    }


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextLayout.setOnClickListener(v -> {
            if (binding.amount.getText().toString().isEmpty()) {
                onMessage("Please your amount");
            } else {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.doneMobileFragmetn);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mobile_payment_load;
    }


}