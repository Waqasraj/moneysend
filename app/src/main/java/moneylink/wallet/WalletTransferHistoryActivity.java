package moneylink.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import moneylink.wallet.adapters.WalletTransferHistoryAdapter;
import moneylink.wallet.base.TootiBaseActivity;

import moneylink.wallet.databinding.WalletTransactionHistoryDesignBinding;
import moneylink.wallet.di.RequestHelper.WalletHistoryRequest;
import moneylink.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

import moneylink.wallet.di.apicaller.WalletHistoryRequestTask;
import moneylink.wallet.interfaces.OnSelectWalletTransaction;
import moneylink.wallet.interfaces.OnWalletHistoryResponse;
import moneylink.wallet.utils.IsNetworkConnection;

public class WalletTransferHistoryActivity extends TootiBaseActivity<WalletTransactionHistoryDesignBinding>
        implements OnWalletHistoryResponse, OnSelectWalletTransaction {


    WalletTransferHistoryAdapter adapter;
    List<WalletTransferHistoryResponse> responseList;
    List<WalletTransferHistoryResponse> filteredList;

    @Override
    public int getLayoutId() {
        return R.layout.wallet_transaction_history_design;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.inculdeLayout.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.inculdeLayout.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        binding.inculdeLayout.crossBtn.setOnClickListener(v->{
            onClose();
        });

        binding.inculdeLayout.crossBtn.setVisibility(View.GONE);
        binding.inculdeLayout.titleTxt.setText(getString(R.string.wallet_history));
        setAccountsRecyclerView();
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            WalletHistoryRequest request = new WalletHistoryRequest();
            request.customerNo = sessionManager.getCustomerNo();
            request.languageId = sessionManager.getlanguageselection();
            WalletHistoryRequestTask task = new WalletHistoryRequestTask(this, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        binding.inculdeLayout.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.receivedLayout.setOnClickListener(v -> {
            filteredList.clear();
            for (int i = 0; i < responseList.size(); i++) {
                if (responseList.get(i).status.equalsIgnoreCase("RECEIVED")) {
                    filteredList.add(responseList.get(i));
                }
            }

            binding.paidView.setVisibility(View.GONE);
            binding.allReceived.setVisibility(View.GONE);
            binding.receivedView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        });


        binding.paidLayout.setOnClickListener(v -> {
            filteredList.clear();
            for (int i = 0; i < responseList.size(); i++) {
                if (responseList.get(i).status.equalsIgnoreCase("Paid")) {
                    filteredList.add(responseList.get(i));
                }
            }
            adapter.notifyDataSetChanged();

            binding.paidView.setVisibility(View.VISIBLE);
            binding.allReceived.setVisibility(View.GONE);
            binding.receivedView.setVisibility(View.GONE);
        });


        binding.allLayout.setOnClickListener(v -> {
            filteredList.clear();
            filteredList.addAll(responseList);
            adapter.notifyDataSetChanged();

            binding.paidView.setVisibility(View.GONE);
            binding.allReceived.setVisibility(View.VISIBLE);
            binding.receivedView.setVisibility(View.GONE);
        });
    }

    @Override
    public void onHistory(List<WalletTransferHistoryResponse> list) {
        responseList.clear();
        filteredList.clear();
        responseList.addAll(list);
        filteredList.addAll(list);
        Log.e("onHistory: ", String.valueOf(filteredList.size()));
        adapter.notifyDataSetChanged();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {
        filteredList = new ArrayList<>();
        responseList = new ArrayList<>();
        adapter = new
                WalletTransferHistoryAdapter(this , filteredList, this);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(accountLayoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectWalletTransaction(WalletTransferHistoryResponse response) {
        Intent intent = new Intent(this, RepeatWalletTransactionActivity.class);
        intent.putExtra("customer_no", response.receiverNumber);
        intent.putExtra("receiver_name" , response.receiverName);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSelectReceipt(WalletTransferHistoryResponse response) {
        Intent intent = new Intent(this, TransactionReceiptActivity.class);
        intent.putExtra("txn_number", response.transactionNumber);
        startActivity(intent);
        finish();
    }
}
