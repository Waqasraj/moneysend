package moneylink.wallet.adapters;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
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
import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import moneylink.wallet.interfaces.OnPrepaidCountry;
import moneylink.wallet.utils.StringHelper;

public class PrepaidOperatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<PrepaidOperatorResponse> list;
    List<PrepaidOperatorResponse> listFiltered;
    OnPrepaidCountry type;
    Context context;


    public PrepaidOperatorAdapter(Context context, List<PrepaidOperatorResponse> listFiltered, OnPrepaidCountry type) {
        this.listFiltered = listFiltered;
        this.context = context;
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
                    List<PrepaidOperatorResponse> filteredList = new ArrayList<>();
                    for (PrepaidOperatorResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.billerName.toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<PrepaidOperatorResponse>) filterResults.values;
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
                    StringHelper.firstLetterCap(listFiltered.get(position).billerName));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


            ((CountryListViewHolder) holder).binding.iconImage.setVisibility(View.GONE);
            Log.e("operator list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    type.onPrepaidOperator(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}
