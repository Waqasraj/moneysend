package moneylink.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.databinding.EmptyViewBeneBinding;

public class EmptyBeneficiaryListViewHolder extends RecyclerView.ViewHolder {
    public EmptyViewBeneBinding binding;

    public EmptyBeneficiaryListViewHolder(@NonNull EmptyViewBeneBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
