package moneylink.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.AdpatersViewHolder.BeneficiaryListViewHolder;
import moneylink.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import moneylink.wallet.databinding.BeneficiaryListAdpaterDesignBinding;
import moneylink.wallet.databinding.EmptyViewBeneBinding;
import moneylink.wallet.di.ResponseHelper.GetBeneficiaryListResponse;
import moneylink.wallet.interfaces.OnSelectBeneficiary;

import java.util.ArrayList;
import java.util.List;

public class CustomerBeneficiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {
    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;
    OnSelectBeneficiary onSelectBeneficiary;
    List<GetBeneficiaryListResponse> beneficiaryListResponses;
    List<GetBeneficiaryListResponse> listFiltered;
    boolean nextHide;

    public CustomerBeneficiaryListAdapter(List<GetBeneficiaryListResponse> beneficiaryListResponses
            , OnSelectBeneficiary onSelectBeneficiary, boolean nextHide) {
        this.beneficiaryListResponses = beneficiaryListResponses;
        this.onSelectBeneficiary = onSelectBeneficiary;
        this.listFiltered = beneficiaryListResponses;
        this.nextHide = nextHide;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            BeneficiaryListAdpaterDesignBinding binding =
                    BeneficiaryListAdpaterDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new BeneficiaryListViewHolder(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BeneficiaryListViewHolder) {
            Log.e("onBindViewHolder: ", String.valueOf(position));
            GetBeneficiaryListResponse response = listFiltered.get(position);
            ((BeneficiaryListViewHolder) holder).binding.beneName.setText(response.firstName
                    .concat(" ").concat(response.lastName).concat(" (").concat(response.payOutCurrency).concat(")"));
            if (response.paymentMode.equalsIgnoreCase("bank")) {
                ((BeneficiaryListViewHolder) holder).binding.beneNumber.setText(response.accountNumber);
                ((BeneficiaryListViewHolder) holder).binding.accountTitle.setText("Account no: ");
            } else if (response.paymentMode.equalsIgnoreCase("cash")) {
                ((BeneficiaryListViewHolder) holder).binding.accountTitle.setText("Contact no: ");
                ((BeneficiaryListViewHolder) holder).binding.beneNumber.setText(response.telephone);
            }


            if(nextHide) {
                ((BeneficiaryListViewHolder) holder).binding.next.setVisibility(View.GONE);
            }

            ((BeneficiaryListViewHolder) holder).binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectBeneficiary.onSelectBeneficiary(response);
                }
            });
        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        if (listFiltered == null) {
            return 0;
        } else if (listFiltered.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }

        return listFiltered.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (listFiltered.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = beneficiaryListResponses;
                } else {
                    List<GetBeneficiaryListResponse> filteredList = new ArrayList<>();
                    for (GetBeneficiaryListResponse row : beneficiaryListResponses) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.firstName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.firstName.contains(charSequence)) {
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
                listFiltered = (ArrayList<GetBeneficiaryListResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
