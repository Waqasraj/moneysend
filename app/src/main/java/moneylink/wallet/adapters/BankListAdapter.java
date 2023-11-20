package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import moneylink.wallet.databinding.TransferListPurposeDesignBinding;
import moneylink.wallet.di.ResponseHelper.GetBankNetworkListResponse;
import moneylink.wallet.interfaces.OnSelectBank;

import java.util.ArrayList;
import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<GetBankNetworkListResponse> list;
    List<GetBankNetworkListResponse> listFiltered;
    OnSelectBank onSelectBank;

    public BankListAdapter(List<GetBankNetworkListResponse> list,
                           OnSelectBank onSelectBank) {
        this.list = list;
        this.onSelectBank = onSelectBank;
        this.listFiltered = list;
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
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(listFiltered.get(position).branchName);
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectBank.onSelectBranch(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }


    @SuppressWarnings("unchecked")
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<GetBankNetworkListResponse> filteredList = new ArrayList<>();
                    for (GetBankNetworkListResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.bankName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.bankName.contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<GetBankNetworkListResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}


