package moneylink.wallet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import moneylink.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import moneylink.wallet.R;
import moneylink.wallet.databinding.TransferListPurposeDesignBinding;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.interfaces.OnSelectCurrency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        Filterable {

    List<GetSendRecCurrencyResponse> list;
    OnSelectCurrency onSelectTransferPurpose;
    List<GetSendRecCurrencyResponse> listFiltered;
    Context context;
    public CurrencyListAdapter(Context context,List<GetSendRecCurrencyResponse> list,
                               OnSelectCurrency onSelectCurrency) {
        this.list = list;
        this.context = context;
        this.onSelectTransferPurpose = onSelectCurrency;
        this.listFiltered = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransferListPurposeDesignBinding binding =
                TransferListPurposeDesignBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new SimpleTextViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SimpleTextViewHolder) {
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(listFiltered.get(position).currencyShortName);
            Log.e( "list: ", String.valueOf(position));
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectTransferPurpose.onCurrencySelect(listFiltered.get(position)));


            Glide.with(context)
                    .load(listFiltered.get(position).image_URL)
                    .placeholder(R.drawable.ic_worldwide)
                    .into(((SimpleTextViewHolder) holder).binding.imageIcon);
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
                    List<GetSendRecCurrencyResponse> filteredList = new ArrayList<>();
                    for (GetSendRecCurrencyResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.currencyShortName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.currencyShortName.contains(charSequence)) {
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
                listFiltered = (ArrayList<GetSendRecCurrencyResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
