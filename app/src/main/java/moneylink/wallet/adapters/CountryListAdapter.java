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

import moneylink.wallet.AdpatersViewHolder.CountryListViewHolder;
import moneylink.wallet.R;
import moneylink.wallet.databinding.CountryDesignBinding;

import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<GetCountryListResponse> list;
    OnSelectCountry onSelectTransferPurpose;
    List<GetCountryListResponse> listFiltered;
    boolean isCurrency = false;
    boolean isShowShortCode = false;
    Context context;
    boolean isWallet = false;

    public CountryListAdapter(Context context ,List<GetCountryListResponse> list,
                              OnSelectCountry onSelectTransferPurpose, boolean isWallet , boolean isCurrency
            , boolean isShowShortCode) {
        this.list = list;
        this.context = context;
        this.onSelectTransferPurpose = onSelectTransferPurpose;
        this.listFiltered = list;
        this.isWallet = isWallet;
        this.isCurrency = isCurrency;
        this.isShowShortCode = isShowShortCode;
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
                    StringHelper.firstLetterCap(listFiltered.get(position).countryName));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            if (isCurrency) {
                ((CountryListViewHolder) holder).binding.countryCode.setText(listFiltered.get(position).currencyShortName);
            } else {
                if (isShowShortCode) {
                    ((CountryListViewHolder) holder).binding.countryCode.setText(listFiltered.get(position).countryCode);
                }
            }


            if(isWallet) {
                ((CountryListViewHolder) holder).binding.purposeName.setText(
                        StringHelper.firstLetterCap(listFiltered.get(position).currencyShortName));
                ((CountryListViewHolder) holder).binding.purposeName
                        .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                ((CountryListViewHolder) holder).binding.countryCode.setVisibility(View.GONE);
            }

            Glide.with(context)
                    .load(listFiltered.get(position).imageURL)
                    .placeholder(R.drawable.ic_worldwide)
                    .into(((CountryListViewHolder) holder).binding.iconImage);

            Log.e("list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectTransferPurpose.onSelectCountry(listFiltered.get(position)));
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
                    List<GetCountryListResponse> filteredList = new ArrayList<>();
                    for (GetCountryListResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (isCurrency) {
                            if (row.currencyShortName.toLowerCase().contains(charString.toLowerCase()) ||
                                    row.currencyShortName.contains(charSequence)) {
                                filteredList.add(row);
                            }
                        } else {
                            if (row.countryName.toLowerCase().contains(charString.toLowerCase()) ||
                                    row.countryName.contains(charSequence)) {
                                filteredList.add(row);
                            }
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
                listFiltered = (ArrayList<GetCountryListResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




}

