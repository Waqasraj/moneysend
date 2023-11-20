package moneylink.wallet.MoneyTransferModuleV;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import moneylink.wallet.databinding.ActivityCashPaymentFourthBinding;
import moneylink.wallet.di.RequestHelper.TootiPayRequest;
import moneylink.wallet.di.RequestHelper.WalletBalanceRequest;
import moneylink.wallet.di.ResponseHelper.GetCardDetailsResponse;
import moneylink.wallet.di.apicaller.TootiPaySendTask;
import moneylink.wallet.di.apicaller.WalletBalanceRequestTask;
import moneylink.wallet.dialogs.BankDepositDialog;
import moneylink.wallet.dialogs.GetCVVDialog;
import moneylink.wallet.dialogs.SingleButtonMessageDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnCardDetailsSubmit;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.interfaces.OnEnterCVv;
import moneylink.wallet.interfaces.OnGetCardDetails;
import moneylink.wallet.interfaces.OnSendTransferTootiPay;
import moneylink.wallet.interfaces.OnWalletBalanceReceived;
import moneylink.wallet.utils.IsNetworkConnection;

public class MoneyTransferPaymentFragment extends BaseFragment<ActivityCashPaymentFourthBinding>
        implements OnSendTransferTootiPay, OnWalletBalanceReceived, OnDecisionMade, OnCardDetailsSubmit
        , OnGetCardDetails, OnEnterCVv {

    Float walletBalance = 0.0f;
    TootiPayRequest tootiPayRequest;
    String transactionNumber = "";

    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;
    boolean isWalletAllowed = true;
    int selectedID = 0;

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.payment));
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        tootiPayRequest = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.request;
        tootiPayRequest.ipAddress = getSessionManager().getIpAddress();
        tootiPayRequest.ipCountryName = getSessionManager().getIpCountryName();

        binding.walletText.setText(getString(R.string.wallet)
                .concat(" ").concat("(").concat(tootiPayRequest.payInCurrency).concat(")"));
        getWalletBalance();

        selectedID = binding.radioWallet.getId();
        binding.paymentOptionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            selectedID = radioButton.getId();

        });


        binding.btnPay.setOnClickListener(v -> {

            if (selectedID != 0) {
                if (selectedID == binding.radioWallet.getId()) {
                    if (isWalletAllowed) {
                        tootiPayRequest.paymentTypeId = PaymentTypeHelper.WALLET;
                        tootiPayRequest.languageId = getSessionManager().getlanguageselection();
                        getPin();
                    } else {
                        onMessage("Selected Sending Currency is not in wallet");
                    }
                } else if (selectedID == binding.radioThorughCard.getId()) {
//                    AddCardDialog dialog = new AddCardDialog(this);
//                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                    dialog.show(transaction, "");
                    showCardDialog();
                } else if (selectedID == binding.radioThroughBank.getId()) {
                    tootiPayRequest.cardNumber = "";
                    tootiPayRequest.expireDate = "";
                    tootiPayRequest.securityNumber = "";
                    tootiPayRequest.paymentTypeId = PaymentTypeHelper.BANK_DEPOSIT;
                    tootiPayRequest.languageId = getSessionManager().getlanguageselection();
                    getPin();
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
        return R.layout.activity_cash_payment_fourth;
    }

    @Override
    public void onMoneyTransferSuccessfully(String transactionNumber) {
        this.transactionNumber = transactionNumber;
        getSessionManager().putWalletNeedToUpdate(true);
        if (tootiPayRequest.paymentTypeId == PaymentTypeHelper.CREDIT_CARD) {
            SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(getString(R.string.in_process)
                    , getString(R.string.in_process_msg_card), this
                    , false);
            dialog.setCancelable(false);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        } else if (tootiPayRequest.paymentTypeId == PaymentTypeHelper.BANK_DEPOSIT) {
            BankDepositDialog depositDialog = new BankDepositDialog(this.transactionNumber, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            depositDialog.show(transaction, "");
        } else {
            goToReceipt();
        }
    }

    private void getWalletBalance() {
        WalletBalanceRequest request = new WalletBalanceRequest();
        request.customerNo = getSessionManager().getCustomerNo();
        request.walletCurrency = tootiPayRequest.payInCurrency;
        request.languageId = getSessionManager().getlanguageselection();
        WalletBalanceRequestTask task = new WalletBalanceRequestTask(getContext(),
                this);
        task.execute(request);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onWalletBalanceReceived(String walletBalance) {
        binding.walletBalance.setText(walletBalance);
    }

    @Override
    public void onLockWalletOption(boolean isLocked) {
        isWalletAllowed = isLocked;
    }

    @Override
    public void onProceed() {
        goToReceipt();
    }

    @Override
    public void onCancel() {
        goToReceipt();
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {
        tootiPayRequest.securityNumber = cardCVV;
        tootiPayRequest.cardNumber = cardNumber;
        tootiPayRequest.expireDate = cardExpire;
        tootiPayRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
        getPin();
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
        tootiPayRequest.cardNumber = cardDetail.cardNumber;
        tootiPayRequest.expireDate = cardDetail.cardExpireDate;
        tootiPayRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
        tootiPayRequest.languageId = getSessionManager().getlanguageselection();


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
        tootiPayRequest.securityNumber = cvv;
        tootiPayRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
        getPin();
    }


    void loadTotiPaySend() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            TootiPaySendTask task = new TootiPaySendTask(getActivity(), this);
            task.execute(tootiPayRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    void goToReceipt() {
        Intent intent = new Intent(getBaseActivity(), TransactionReceiptActivity.class);
        intent.putExtra("txn_number", transactionNumber);
        startActivity(intent);
        getBaseActivity().finish();
    }


    @Override
    public void onVerifiedPin() {
        loadTotiPaySend();
    }
}
