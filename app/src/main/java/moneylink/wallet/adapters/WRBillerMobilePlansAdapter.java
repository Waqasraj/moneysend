package moneylink.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.Mobile_Top_Up.helpers.MobileTopUpType;
import moneylink.wallet.databinding.BillerPlanDesignBinding;
import moneylink.wallet.di.ResponseHelper.WRBillerPlanResponse;
import moneylink.wallet.interfaces.OnBillerPlans;

public class WRBillerMobilePlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<WRBillerPlanResponse> list;
    OnBillerPlans plans;


    public WRBillerMobilePlansAdapter(List<WRBillerPlanResponse> list, OnBillerPlans plans) {
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

            if (list.get(position).billerTypeId == MobileTopUpType.PRE_PAID) {
//                ((BillerPlanViewHolder) holder).binding.billerName.setText(list.get(position).billerName);
//                ((BillerPlanViewHolder) holder).binding.payableAmount.
//                        setText(list.get(position).payableAmount.toString() + " USD");
//                ((BillerPlanViewHolder) holder).binding.description.setText(list.get(position).description);
//                ((BillerPlanViewHolder) holder).binding.mobilePlanLayout.setVisibility(View.VISIBLE);
//                ((BillerPlanViewHolder) holder).binding.serviceProvider.setVisibility(View.GONE);
            } else {
//                ((BillerPlanViewHolder) holder).binding.serviceProvider.setText(list.get(position).description);
//                ((BillerPlanViewHolder) holder).binding.mobilePlanLayout.setVisibility(View.GONE);
//                ((BillerPlanViewHolder) holder).binding.serviceProvider.setVisibility(View.VISIBLE);
            }

            ((BillerPlanViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
              //  plans.onBillerPlanSelect(list.get(position));
            });
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

