package moneylink.wallet.adapters;

import android.content.Context;
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
import moneylink.wallet.di.ResponseHelper.YLocationResponse;

import moneylink.wallet.interfaces.OnGetYLocation;
import moneylink.wallet.utils.StringHelper;

public class YemenLocationsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<YLocationResponse> list;
    List<YLocationResponse> listFiltered;
    OnGetYLocation type;
    Context context;

    public YemenLocationsAdapter(Context context, List<YLocationResponse> listFiltered, OnGetYLocation type) {
        this.context = context;
        this.listFiltered = listFiltered;
        this.list = listFiltered;
        this.type = type;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<YLocationResponse> filteredList = new ArrayList<>();
                    for (YLocationResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.locationName.toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<YLocationResponse>) filterResults.values;
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
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(
                    StringHelper.firstLetterCap(listFiltered.get(position).locationName));
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    type.onSelectYLocation(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}


