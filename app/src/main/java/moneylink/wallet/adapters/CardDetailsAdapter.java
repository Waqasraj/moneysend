package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.databinding.CardDetailsDesignBinding;
import moneylink.wallet.di.ResponseHelper.GetCardDetailsResponse;
import moneylink.wallet.interfaces.OnGetCardDetails;

public class CardDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GetCardDetailsResponse> responseList;
    OnGetCardDetails onGetCardDetails;

    public CardDetailsAdapter(List<GetCardDetailsResponse> responseList, OnGetCardDetails onGetCardDetails) {
        this.responseList = responseList;
        this.onGetCardDetails = onGetCardDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardDetailsDesignBinding binding =
                CardDetailsDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CardDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardDetailsViewHolder) {

            ((CardDetailsViewHolder) holder).binding.cardNumber.setText(responseList.get(position)
                    .cardNumber.replaceAll("\\w(?=\\w{4})", "*"));


            ((CardDetailsViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                onGetCardDetails.onSelectCard(responseList.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }


    public class CardDetailsViewHolder extends RecyclerView.ViewHolder {
        public CardDetailsDesignBinding binding;

        public CardDetailsViewHolder(@NonNull CardDetailsDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
