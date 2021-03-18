package tootipay.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tootipay.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import tootipay.wallet.databinding.BankDetailsDesignBinding;
import tootipay.wallet.databinding.TransferListPurposeDesignBinding;

public class BankDetailsApadterOnTransfer extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    List<String> bankList ;

    public BankDetailsApadterOnTransfer(List<String> bankList) {
        this.bankList = bankList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankDetailsDesignBinding binding =
                BankDetailsDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BankDetailsDesignHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BankDetailsDesignHolder) {
            ((BankDetailsDesignHolder) holder).binding.purposeName.setText(bankList.get(position));


        }
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public class BankDetailsDesignHolder extends RecyclerView.ViewHolder {
        public BankDetailsDesignBinding binding;

        public BankDetailsDesignHolder(@NonNull BankDetailsDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
