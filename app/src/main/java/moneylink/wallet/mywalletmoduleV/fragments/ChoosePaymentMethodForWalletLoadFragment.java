package moneylink.wallet.mywalletmoduleV.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.MoneyTransferModuleV.Helpers.PaymentTypeHelper;
import moneylink.wallet.R;

import moneylink.wallet.adapters.CardDetailsAdapter;
import moneylink.wallet.databinding.ActivitySelectCardBinding;
import moneylink.wallet.di.RequestHelper.LoadWalletRequest;
import moneylink.wallet.di.ResponseHelper.GetCardDetailsResponse;
import moneylink.wallet.di.apicaller.LoadWalletTask;
import moneylink.wallet.dialogs.BankDepositDialog;
import moneylink.wallet.dialogs.GetCVVDialog;
import moneylink.wallet.dialogs.SingleButtonMessageDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnCardDetailsSubmit;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.interfaces.OnEnterCVv;
import moneylink.wallet.interfaces.OnGetCardDetails;
import moneylink.wallet.interfaces.OnSuccessMessage;
import moneylink.wallet.utils.IsNetworkConnection;

public class ChoosePaymentMethodForWalletLoadFragment extends BaseFragment<ActivitySelectCardBinding>
        implements OnCardDetailsSubmit, OnSuccessMessage, OnDecisionMade, OnGetCardDetails
        , OnEnterCVv {

    String amountToLoad = "0.0";
    String currencyToLoad = "GBP";
    LoadWalletRequest request;

    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;
    int selectedID = 0;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        if (getArguments() != null) {
            amountToLoad = getArguments().getString("load_amount");
            currencyToLoad = getArguments().getString("load_currency");
        }

        request = new LoadWalletRequest();
        request.customerNo = getSessionManager().getCustomerNo();
        request.transferAmount = amountToLoad;
        request.walletCurrency = currencyToLoad;
        request.languageId = getSessionManager().getlanguageselection();
        request.ipCountryName = getSessionManager().getIpCountryName();
        request.ipAddress = getSessionManager().getIpAddress();


        selectedID = binding.radioThorughCard.getId();
        binding.paymentOptionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            selectedID = radioButton.getId();

        });


        binding.btnPay.setOnClickListener(v -> {

            if (selectedID != 0) {
                if (selectedID == binding.radioThorughCard.getId()) {
//                   AddCardDialog addCardDialog = new AddCardDialog(this);
//                   FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                   addCardDialog.show(transaction, "");
                    showCardDialog();
                } else if (selectedID == binding.radioThroughBank.getId()) {
                    request.cardNumber = "";
                    request.expireDate = "";
                    request.securityNumber = "";
                    request.languageId = getSessionManager().getlanguageselection();
                    request.paymentType = PaymentTypeHelper.BANK_DEPOSIT;
                    loadWallet();
                }
            }

        });


        binding.loadCards.setOnClickListener(v -> {
//            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
//                GetCardDetailsRequest request = new GetCardDetailsRequest();
//                request.customerNo = getSessionManager().getCustomerNo();
//                request.languageID = getSessionManager().getlanguageselection();
//
//                GetCardDetailsTask task = new GetCardDetailsTask(getContext(),
//                        this);
//                task.execute(request);
//            } else {
//                onMessage(getString(R.string.no_internet));
//            }
            showCardDialog();
        });
    }


    public void showCardDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Alert")
                .setMessage("Credit Card is not allowed for testing.")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_card;
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {
        request.cardNumber = cardNumber;
        request.expireDate = cardExpire;
        request.securityNumber = cardCVV;
        request.paymentType = PaymentTypeHelper.CREDIT_CARD;
        loadWallet();
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccess(String s) {
        getSessionManager().putWalletNeedToUpdate(true);

        if (request.paymentType == PaymentTypeHelper.CREDIT_CARD) {
            SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(
                    getString(R.string.in_process), getString(R.string.in_process_msg_card)
                    , this,
                    false);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        } else if (request.paymentType == PaymentTypeHelper.BANK_DEPOSIT) {
            BankDepositDialog depositDialog = new BankDepositDialog(s, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            depositDialog.show(transaction, "");
        }

    }

    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses) {
        this.responseList.clear();
        this.responseList.addAll(cardDetailsResponses);
        detailsAdapter.notifyDataSetChanged();

        if (cardDetailsResponses.size() > 0) {
            binding.loadCards.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectCard(GetCardDetailsResponse cardDetail) {
        request.cardNumber = cardDetail.cardNumber;
        request.expireDate = cardDetail.cardExpireDate;

        GetCVVDialog dialog = new GetCVVDialog(this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        responseList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        detailsAdapter = new
                CardDetailsAdapter(responseList, this);
        binding.cardDetailsRecyclerView.setLayoutManager(mLayoutManager);
        binding.cardDetailsRecyclerView.setHasFixedSize(true);
        binding.cardDetailsRecyclerView.setAdapter(detailsAdapter);
    }

    @Override
    public void onCVV(String cvv) {
        request.securityNumber = cvv;
        request.paymentType = PaymentTypeHelper.CREDIT_CARD;
        loadWallet();
    }


    void loadWallet() {
        if (getSessionManager().getISKYCApproved()) {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                LoadWalletTask task = new LoadWalletTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        } else {
            onMessage(getString(R.string.complete_profile));
        }
    }
}
