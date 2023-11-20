package moneylink.wallet.home_module.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.bumptech.glide.Glide;

import moneylink.wallet.KYC.KYCMainActivity;
import moneylink.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import moneylink.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import moneylink.wallet.TransactionHistoryActivity;
import moneylink.wallet.WalletTransferHistoryActivity;
import moneylink.wallet.billpayment.BillPaymentMainActivity;
import moneylink.wallet.mywalletmoduleV.CreateWalletActivity;
import moneylink.wallet.R;
import moneylink.wallet.TransferToOwnWalletActivity;
import moneylink.wallet.adapters.UserAccountsHomeAdapter;
import moneylink.wallet.databinding.FragmentHomeBinding;
import moneylink.wallet.di.RequestHelper.GetCustomerWalletDetailsRequest;
import moneylink.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;
import moneylink.wallet.di.apicaller.GetCustomerWalletDetailsTask;
import moneylink.wallet.dialogs.AlertDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.ClassChangerHelper;
import moneylink.wallet.home_module.NewDashboardActivity;
import moneylink.wallet.interfaces.OnCustomerWalletDetails;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.interfaces.OnSelectOffers;
import moneylink.wallet.mywalletmoduleV.MyWalletMainActivity;
import moneylink.wallet.prepaid_cards_module.PrepaidCardsActivity;
import moneylink.wallet.utils.BitmapHelper;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.NumberFormatter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnDecisionMade
        , OnSelectOffers, OnCustomerWalletDetails {

    ArrayList<String> stringArrayList = new ArrayList<>();
    UserAccountsHomeAdapter adapter;

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();

        ((NewDashboardActivity) getActivity()).hideHumBurger(ClassChangerHelper.HOME);

        binding.kycLayouts.setVisibility(View.GONE);


        if (getSessionManager().getCustomerImage() != null) {
            Glide.with(this)
                    .load(BitmapHelper.decodeImage(getSessionManager().getCustomerImage()))
                    .placeholder(ResourcesCompat
                            .getDrawable(getResources()
                                    , R.drawable.icon_half_logo,
                                    null))
                    .into(binding.userImageHome);
        }

        if (adapter != null && getSessionManager().isWalletNeedToUpdate()) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.clear();
            getWallet();
        }
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        // setOffers();
        setAccountsRecyclerView();
        onLoadView();

        binding.swipeRefresh.setOnRefreshListener(() -> {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.clear();
            getWallet();
        });


        binding.kycLayouts.setOnClickListener(view -> startActivity(new Intent(getActivity(), KYCMainActivity.class)));

        binding.addMoneyCardview.setOnClickListener(view -> startActivity(new Intent(getActivity(),
                MyWalletMainActivity.class)));

        binding.moneyTransfer.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), MoneyTransferMainLayout.class)));

        binding.checkRatesCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TransferToOwnWalletActivity.class);
            startActivity(intent);
        });


        binding.createWallet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateWalletActivity.class);
            startActivity(intent);
        });


        binding.transactionHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TransactionHistoryActivity.class);
            startActivity(intent);
        });


        binding.walletHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WalletTransferHistoryActivity.class);
            startActivity(intent);
        });


        Glide.with(this)
                .load(BitmapHelper.decodeImage(((NewDashboardActivity) getBaseActivity()).sessionManager.getCustomerImage()))
                .placeholder(R.drawable.icon_half_logo)
                .into(binding.userImageHome);


        binding.mobileTopUpCarview.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), MobileTopUpMainActivity.class))
                //startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.billPaymentCarview.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), BillPaymentMainActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.eGift.setOnClickListener(view ->
                //      startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
                startActivity(new Intent(getActivity(), PrepaidCardsActivity.class))
        );


    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onProceed() {
        startActivity(new Intent(getActivity(), KYCMainActivity.class));
    }

    @Override
    public void onCancel() {

    }


    public void onLoadView() {
        if (((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses == null) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses = new ArrayList<>();
        }

        getWallet();

        binding.kycLayouts.setVisibility(View.GONE);
        ((NewDashboardActivity) getBaseActivity())
                .showUserName(getSessionManager().getUserName());

        if (!((NewDashboardActivity) getBaseActivity())
                .sessionManager.getISKYCApproved()) {
            if (!((NewDashboardActivity) getBaseActivity())
                    .sessionManager.getIsDocumentsUploaded()) {
                binding.kycLayouts.setVisibility(View.VISIBLE);
                AlertDialog dialog = new AlertDialog(getString(R.string.complete_profile)
                        , getString(R.string.please_complete_kyc), this);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                dialog.show(transaction, "");
            } else {
                binding.kycLayouts.setVisibility(View.GONE);
            }
        }

        binding.kycLayouts.setVisibility(View.GONE);

        String walletBalance = NumberFormatter.decimal(getSessionManager().getWalletBalance());
        ((NewDashboardActivity) getBaseActivity()).showWalletBalance(walletBalance);
    }


    public void getWallet() {
        if (((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.isEmpty()) {

            if (!binding.swipeRefresh.isRefreshing()) {
                binding.swipeRefresh.setRefreshing(true);
            }
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetCustomerWalletDetailsRequest request = new GetCustomerWalletDetailsRequest();
                request.customerNo = getSessionManager().getCustomerNo();

                GetCustomerWalletDetailsTask task = new GetCustomerWalletDetailsTask(getContext()
                        , this, true);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        }
    }


    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {

        if (((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses == null) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses = new ArrayList<>();
        }
        adapter = new
                UserAccountsHomeAdapter(getContext(), ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.userAccountrecyclerview.setLayoutManager(accountLayoutManager);
        binding.userAccountrecyclerview.setHasFixedSize(true);
        binding.userAccountrecyclerview.setAdapter(adapter);
    }


    @Override
    public void onSelectOffer() {
        //startActivity(new Intent(getActivity(), OffersActivity.class));
    }

    @Override
    public void onCustomerWalletDetails(List<GetCustomerWalletDetailsResponse> walletList) {
        if (((NewDashboardActivity) getBaseActivity()).homeViewModel != null) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.clear();
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.addAll(walletList);
        }

        getSessionManager().putWalletNeedToUpdate(false);
        adapter.notifyDataSetChanged();
        binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onSelectWallet(GetCustomerWalletDetailsResponse wallet) {

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
        binding.swipeRefresh.setRefreshing(false);
    }


}