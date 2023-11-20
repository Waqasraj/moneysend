package moneylink.wallet.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import moneylink.wallet.R;
import moneylink.wallet.interfaces.OnSelectOffers;

import java.util.List;

public class Offer_Adapter extends RecyclerView.Adapter<Offer_Adapter.ViewHolder> {

    Context activity;
    List<String> response_list;
    OnSelectOffers onSelectOffers;

    public Offer_Adapter(Context activity, List<String> response, OnSelectOffers onSelectOffers) {
        this.activity = activity;
        this.response_list = response;
        this.onSelectOffers = onSelectOffers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_adapter_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.offer.setText(response_list.get(position));
        holder.itemView.setOnClickListener(v -> {
            onSelectOffers.onSelectOffer();
        });
    }

    @Override
    public int getItemCount() {
        return response_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView offer, title;

        public ViewHolder(View itemView) {
            super(itemView);
            offer = itemView.findViewById(R.id.percentage_offer);
            title = itemView.findViewById(R.id.rate_title);
        }
    }
}
