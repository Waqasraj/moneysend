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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.AdpatersViewHolder.CountryListViewHolder;
import moneylink.wallet.R;
import moneylink.wallet.databinding.CountryDesignBinding;
import moneylink.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import moneylink.wallet.interfaces.OnWRBillerNames;
import moneylink.wallet.utils.StringHelper;

public class WRBillerOperatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<GetWRBillerNamesResponseC> list;
    List<GetWRBillerNamesResponseC> listFiltered;
    OnWRBillerNames type;
    Context context;


    public WRBillerOperatorAdapter(Context context, List<GetWRBillerNamesResponseC> listFiltered, OnWRBillerNames type) {
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
                    List<GetWRBillerNamesResponseC> filteredList = new ArrayList<>();
                    for (GetWRBillerNamesResponseC row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.Biller_Name.toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<GetWRBillerNamesResponseC>) filterResults.values;
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
                    StringHelper.firstLetterCap(listFiltered.get(position).Biller_Name));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            Glide.with(context)
                    .load("")
                    .placeholder(R.drawable.ic_utilities)
                    .into(((CountryListViewHolder) holder).binding.iconImage);
            ((CountryListViewHolder) holder).binding.iconImage.setVisibility(View.GONE);
            Log.e("list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    type.onSelectBillerName(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}
