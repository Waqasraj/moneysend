package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.databinding.BillerPlanDesignBinding;
import moneylink.wallet.di.ResponseHelper.WRBillerPlanResponse;
import moneylink.wallet.interfaces.OnBillerPlans;


public class WRBillerPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<WRBillerPlanResponse> list;
    OnBillerPlans plans;


    public WRBillerPlansAdapter(List<WRBillerPlanResponse> list, OnBillerPlans plans) {
        this.list = list;
        this.plans = plans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BillerPlanDesignBinding binding =
                BillerPlanDesignBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new BillerPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillerPlanViewHolder) {
//            ((BillerPlanViewHolder) holder).binding.serviceProvider.setText(list.get(position).description);
//            ((BillerPlanViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
//                plans.onBillerPlanSelect(list.get(position));
//            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BillerPlanViewHolder extends RecyclerView.ViewHolder {
        public BillerPlanDesignBinding binding;

        public BillerPlanViewHolder(@NonNull BillerPlanDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
