package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.databinding.TransferListPurposeDesignBinding;
import moneylink.wallet.di.ResponseHelper.PurposeOfTransferListResponse;
import moneylink.wallet.interfaces.OnSelectTransferPurpose;

import java.util.List;

public class TransferPurposeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<PurposeOfTransferListResponse> list;
    OnSelectTransferPurpose onSelectTransferPurpose;

    public TransferPurposeAdapter(List<PurposeOfTransferListResponse> list,
                                  OnSelectTransferPurpose onSelectTransferPurpose) {
        this.list = list;
        this.onSelectTransferPurpose = onSelectTransferPurpose;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransferListPurposeDesignBinding binding =
                TransferListPurposeDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransferPurposeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TransferPurposeViewHolder) {
            ((TransferPurposeViewHolder) holder).binding.purposeName.setText(list.get(position).purposeOfTransfer);
            ((TransferPurposeViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((TransferPurposeViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectTransferPurpose
                            .onSelectTransferPurpose(list.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransferPurposeViewHolder  extends RecyclerView.ViewHolder {
        public TransferListPurposeDesignBinding binding;

        public TransferPurposeViewHolder(@NonNull TransferListPurposeDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}

