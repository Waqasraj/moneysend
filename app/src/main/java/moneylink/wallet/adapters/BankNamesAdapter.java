package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import moneylink.wallet.databinding.TransferListPurposeDesignBinding;
import moneylink.wallet.interfaces.OnSelectBank;

public class BankNamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {


    List<String> bankList;
    List<String> filteredBankList;
    OnSelectBank onSelectBank;

    public BankNamesAdapter(List<String> bankList, OnSelectBank onSelectBank) {
        this.bankList = bankList;
        this.filteredBankList = bankList;
        this.onSelectBank = onSelectBank;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredBankList = bankList;
                } else {
                    List<String> filteredList = new ArrayList<>();
                    for (String row : bankList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.toLowerCase().contains(charString.toLowerCase()) ||
                                row.contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    filteredBankList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBankList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredBankList = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransferListPurposeDesignBinding binding =
                TransferListPurposeDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SimpleTextViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SimpleTextViewHolder) {
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(filteredBankList.get(position));
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectBank.onSelectBankName(filteredBankList.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return filteredBankList.size();
    }
}
