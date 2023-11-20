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

import moneylink.wallet.AdpatersViewHolder.CountryListViewHolder;
import moneylink.wallet.databinding.CountryDesignBinding;
import moneylink.wallet.di.ResponseHelper.WRCountryListResponse;
import moneylink.wallet.interfaces.OnPrepaidCountry;

public class PrepaidCountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {


    List<WRCountryListResponse> list;
    List<WRCountryListResponse> listFiltered;
    OnPrepaidCountry onWRCountryList;
    Context context;

    public PrepaidCountryAdapter(Context context, List<WRCountryListResponse> filteredList,
                                 OnPrepaidCountry onWRCountryList) {
        this.listFiltered = filteredList;
        this.context = context;
        this.list = filteredList;
        this.onWRCountryList = onWRCountryList;
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
                    List<WRCountryListResponse> filteredList = new ArrayList<>();
                    for (WRCountryListResponse row : list) {
                        if (row.countryName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.countryName.contains(charSequence)) {
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
                listFiltered = (ArrayList<WRCountryListResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryDesignBinding binding =
                CountryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CountryListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CountryListViewHolder) {
            ((CountryListViewHolder) holder).binding.purposeName.setText(
                    (listFiltered.get(position).countryName));
            ((CountryListViewHolder) holder).binding.iconImage.setVisibility(View.GONE);
            ((CountryListViewHolder) holder).binding.countryCode.setText("+" + listFiltered.get(position).callingCode);
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onWRCountryList.onSelectPrepaidCountry(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}
