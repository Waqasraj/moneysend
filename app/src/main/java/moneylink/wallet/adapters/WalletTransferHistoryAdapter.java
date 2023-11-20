package moneylink.wallet.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import moneylink.wallet.R;
import moneylink.wallet.databinding.EmptyViewBeneBinding;
import moneylink.wallet.databinding.TransactionHistoryDesignBinding;
import moneylink.wallet.di.ResponseHelper.WalletTransferHistoryResponse;
import moneylink.wallet.interfaces.OnSelectWalletTransaction;
import moneylink.wallet.utils.Utils;

public class WalletTransferHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;

    List<WalletTransferHistoryResponse> beneficiaryListResponses;
    OnSelectWalletTransaction onSelectTransaction;
    Context context;


    public WalletTransferHistoryAdapter(Context context, List<WalletTransferHistoryResponse> beneficiaryListResponses
            , OnSelectWalletTransaction transaction) {
        this.context = context;
        this.beneficiaryListResponses = beneficiaryListResponses;
        this.onSelectTransaction = transaction;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            TransactionHistoryDesignBinding binding =
                    TransactionHistoryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new WalletTransferHistoryAdapterView(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WalletTransferHistoryAdapterView) {
            WalletTransferHistoryResponse response = beneficiaryListResponses.get(position);

            ((WalletTransferHistoryAdapterView) holder).binding.paymentType.setText(response.paymentType);

            String timeDate = response.transactionDate.replace("+03:00"
                    , "Z");

            ((WalletTransferHistoryAdapterView) holder).binding.dateTrans.setText(Utils.getDateFromServerTime(timeDate));
            ((WalletTransferHistoryAdapterView) holder).binding.sendingAmount.setText(response.sendingAmount
                    .concat(" ").concat(response.sendingCurrency));
            ((WalletTransferHistoryAdapterView) holder).binding.status.setText(response.status);
            ((WalletTransferHistoryAdapterView) holder).binding.viewReciept.setVisibility(View.GONE);
            //underprocess
            if (response.status.equalsIgnoreCase("received")) {
                ((WalletTransferHistoryAdapterView) holder).binding.titleStatus.setText(context
                        .getResources().getString(R.string.received_from));
                ((WalletTransferHistoryAdapterView) holder).binding.beneficairyName.setText(response.senderName);
            }
//            else if (66 == 66) {
//                ((WalletTransferHistoryAdapterView) holder).binding.titleStatus.setText(context
//                        .getResources().getString(R.string.received_from));
//                ((WalletTransferHistoryAdapterView) holder).binding.beneficairyName.setText(response.senderName);
//            }
            else {
                ((WalletTransferHistoryAdapterView) holder).binding.titleStatus.setText(context.getString(R.string.transfer_to));
                ((WalletTransferHistoryAdapterView) holder).binding.beneficairyName.setText(response.receiverName);
            }

            if (!response.paymentType.isEmpty()) {
                if (response.paymentType.equalsIgnoreCase("Wallet transfer")
                        && !response.status.equalsIgnoreCase("received")) {
                    ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setVisibility(View.VISIBLE);
                } else if (response.paymentType.equalsIgnoreCase("Load Wallet")) {
                    ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setVisibility(View.GONE);
                } else {
                    ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setVisibility(View.GONE);
                }

                if (response.paymentType.equalsIgnoreCase("Wallet transfer")) {
                    ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setVisibility(View.VISIBLE);
                }
            }


            if (response.status.equalsIgnoreCase("received")) {
                ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setVisibility(View.GONE);
            }

            ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setOnClickListener(v -> {
                onSelectTransaction.onSelectWalletTransaction(response);
            });


            ((WalletTransferHistoryAdapterView) holder).binding.viewReciept.setOnClickListener(v -> {
                onSelectTransaction.onSelectReceipt(response);
            });

            if (response.paymentTypeID == 66) {
                ((WalletTransferHistoryAdapterView) holder).binding.repeatTranx.setVisibility(View.GONE);
            }

            ((WalletTransferHistoryAdapterView) holder).binding.viewReciept.setVisibility(View.GONE);
        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {
            ((EmptyBeneficiaryListViewHolder) holder).binding.errorEmpty.setText(context
                    .getResources().getString(R.string.no_history_found));
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

    public class WalletTransferHistoryAdapterView extends RecyclerView.ViewHolder {
        public TransactionHistoryDesignBinding binding;

        public WalletTransferHistoryAdapterView(@NonNull TransactionHistoryDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}

