package moneylink.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import moneylink.wallet.adapters.TransactionHistoryAdapter;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityTransacationHistoryBinding;
import moneylink.wallet.di.RequestHelper.TransactionHistoryRequest;
import moneylink.wallet.di.ResponseHelper.TransactionHistoryResponse;
import moneylink.wallet.di.ResponseHelper.GetTransactionReceiptResponse;
import moneylink.wallet.di.apicaller.TransactionHistoryTask;
import moneylink.wallet.interfaces.OnGetTransactionHistory;
import moneylink.wallet.interfaces.OnSelectTransaction;
import moneylink.wallet.utils.IsNetworkConnection;

public class TransactionHistoryActivity extends TootiBaseActivity<ActivityTransacationHistoryBinding>
        implements OnGetTransactionHistory, OnSelectTransaction {


    public List<TransactionHistoryResponse> responseList;
    public List<TransactionHistoryResponse> filteredList;
    TransactionHistoryAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transacation_history;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {

        setAccountsRecyclerView();
        binding.inculdeLayout.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.inculdeLayout.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        binding.inculdeLayout.crossBtn.setOnClickListener(v->{
            onClose();
        });

        binding.inculdeLayout.crossBtn.setVisibility(View.GONE);
        binding.inculdeLayout.titleTxt.setText(getString(R.string.transaction_history_txt));
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            TransactionHistoryRequest request = new TransactionHistoryRequest();
            request.customerNo = sessionManager.getCustomerNo();
            request.languageId = sessionManager.getlanguageselection();
            TransactionHistoryTask task = new TransactionHistoryTask(this, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }




        binding.paidLayout.setOnClickListener(v -> {
            filteredList.clear();
            for (int i = 0; i < responseList.size(); i++) {
                if (responseList.get(i).status.equalsIgnoreCase("paid")) {
                    filteredList.add(responseList.get(i));
                }
            }

            binding.paidView.setVisibility(View.VISIBLE);
            binding.allReceived.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        });


        binding.allLayout.setOnClickListener(v -> {
            filteredList.clear();
            filteredList.addAll(responseList);

            binding.paidView.setVisibility(View.GONE);
            binding.allReceived.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        });

        binding.inculdeLayout.backBtn.setOnClickListener(v -> finish());
    }

    @Override
    public void onGetHistoryList(List<TransactionHistoryResponse> historyList) {
        responseList.clear();
        filteredList.clear();
        filteredList.addAll(historyList);
        responseList.addAll(historyList);
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
        responseList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new
                TransactionHistoryAdapter(this , filteredList, this);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(accountLayoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectTransaction(GetTransactionReceiptResponse receiptResponse) {

    }

    @Override
    public void onSelectTransactionReceipt(String txtNumber) {
        Intent intent = new Intent(this, TransactionReceiptActivity.class);
        intent.putExtra("txn_number", txtNumber);
        startActivity(intent);
    }

    @Override
    public void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse) {

    }
}