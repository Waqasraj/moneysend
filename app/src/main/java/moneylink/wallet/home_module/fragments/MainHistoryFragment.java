package moneylink.wallet.home_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import moneylink.wallet.TransactionHistoryActivity;
import moneylink.wallet.WalletTransferHistoryActivity;
import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivityMainHistoryBinding;
import moneylink.wallet.di.ResponseHelper.TransactionHistoryResponse;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.ClassChangerHelper;
import moneylink.wallet.home_module.NewDashboardActivity;


import androidx.core.content.ContextCompat;

import java.util.List;

public class MainHistoryFragment extends BaseFragment<ActivityMainHistoryBinding> {


    public List<TransactionHistoryResponse> responseList;

    @Override
    public void onResume() {
        super.onResume();
        ((NewDashboardActivity) getActivity()).hideHumBurger(ClassChangerHelper.HISTORY);

    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.inculdeLayout.backBtn
                .setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.inculdeLayout.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.inculdeLayout.titleTxt.setText(getString(R.string.history));

        binding.inculdeLayout.crossBtn.setVisibility(View.GONE);

        binding.inculdeLayout.backBtn.setOnClickListener(v -> {
            ((NewDashboardActivity) getBaseActivity())
                    .moveToFragment(new HomeFragment());
        });


        binding.transactionHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TransactionHistoryActivity.class));
        });

        binding.walletHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                WalletTransferHistoryActivity.class)));

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_history;
    }

}