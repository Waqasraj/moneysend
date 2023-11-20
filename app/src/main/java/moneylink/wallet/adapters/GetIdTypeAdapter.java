package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import moneylink.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import moneylink.wallet.databinding.TransferListPurposeDesignBinding;
import moneylink.wallet.di.ResponseHelper.GetIdTypeResponse;
import moneylink.wallet.interfaces.OnSelectIdType;

import java.util.List;

public class GetIdTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GetIdTypeResponse> idTypeList;
    OnSelectIdType onSelectItem;

    public GetIdTypeAdapter(List<GetIdTypeResponse> idTypeList, OnSelectIdType onSelectItem) {
        this.idTypeList = idTypeList;
        this.onSelectItem = onSelectItem;
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
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(idTypeList.get(position).idTypeName);
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectItem.onSelectIdType(idTypeList.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return idTypeList.size();
    }
}
