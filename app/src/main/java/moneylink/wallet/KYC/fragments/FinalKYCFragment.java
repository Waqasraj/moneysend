package moneylink.wallet.KYC.fragments;

import android.os.Bundle;


import moneylink.wallet.databinding.ActivityFinalKycBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.R;

public class FinalKYCFragment extends BaseFragment<ActivityFinalKycBinding> {


    //cross buttons
    //kyc close
    //show card on bank
    //

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.finishBtn.setOnClickListener(v -> {
            getActivity().finish();
        });




    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_final_kyc;
    }

}
