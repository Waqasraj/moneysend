package moneylink.wallet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import moneylink.wallet.R;
import moneylink.wallet.databinding.EmptyViewBeneBinding;
import moneylink.wallet.databinding.TransactionHistoryDesignBinding;
import moneylink.wallet.di.ResponseHelper.TransactionHistoryResponse;
import moneylink.wallet.interfaces.OnSelectTransaction;
import moneylink.wallet.utils.Utils;

import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;
    OnSelectTransaction onSelectTransaction;
    List<TransactionHistoryResponse> beneficiaryListResponses;
    Context context;

    public TransactionHistoryAdapter(Context context, List<TransactionHistoryResponse> beneficiaryListResponses
            , OnSelectTransaction onSelectTransaction) {
        this.context = context;
        this.beneficiaryListResponses = beneficiaryListResponses;
        this.onSelectTransaction = onSelectTransaction;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            TransactionHistoryDesignBinding binding =
                    TransactionHistoryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TransactionHistoryListHolder(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionHistoryListHolder) {
            Log.e("onBindViewHolder: ", String.valueOf(position));
            TransactionHistoryResponse response = beneficiaryListResponses.get(position);
            ((TransactionHistoryListHolder) holder).binding.beneficairyName.setText(response.receiverName);
            ((TransactionHistoryListHolder) holder).binding.paymentType.setText(response.paymentType);
            //  2022-02-21T12:39:54.54+03:00
            String timeDate = response.transactionDate.replace("+03:00"
                    , "Z");


            ((TransactionHistoryListHolder) holder).binding.dateTrans.setText(Utils.getDateFromServerTime(timeDate));
            ((TransactionHistoryListHolder) holder).binding.sendingAmount.setText(response.sendingAmount
                    .concat(" ").concat(response.currency));
            ((TransactionHistoryListHolder) holder).binding.status.setText(response.status);

            ((TransactionHistoryListHolder) holder).binding.repeatTranx.setVisibility(View.GONE);
            ((TransactionHistoryListHolder) holder).binding.viewReciept
                    .setOnClickListener(v -> {
                        onSelectTransaction.onSelectTransactionReceipt(response.transactionNumber);
                    });


            if (response.paymentTypeID == 1 || response.paymentTypeID == 2) {
                ((TransactionHistoryListHolder) holder).binding.viewReciept.setVisibility(View.VISIBLE);
            } else {
                ((TransactionHistoryListHolder) holder).binding.viewReciept.setVisibility(View.GONE);
            }
        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {
            ((EmptyBeneficiaryListViewHolder) holder).binding.errorEmpty
                    .setText(context.getResources().getString(R.string.no_history_found));
        }
    }

    @Override
    public int getItemCount() {
        if (beneficiaryListResponses == null) {
            return ITEM;
        } else if (beneficiaryListResponses.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }

        // Add extra view to show the footer view
        return beneficiaryListResponses.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (beneficiaryListResponses.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public static class TransactionHistoryListHolder extends RecyclerView.ViewHolder {

        public TransactionHistoryDesignBinding binding;

        public TransactionHistoryListHolder(@NonNull TransactionHistoryDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
