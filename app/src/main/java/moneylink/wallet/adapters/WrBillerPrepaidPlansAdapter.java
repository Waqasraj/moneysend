package moneylink.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import moneylink.wallet.databinding.BillerPlanDesignBinding;
import moneylink.wallet.databinding.EmptyViewBeneBinding;
import moneylink.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import moneylink.wallet.interfaces.OnBillerPlans;

public class WrBillerPrepaidPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PrepaidPlanResponse.MobileTopUpSKU> list;
    private final int EMPTY_VIEW = 1;
    OnBillerPlans plans;

    public WrBillerPrepaidPlansAdapter(List<PrepaidPlanResponse.MobileTopUpSKU> list, OnBillerPlans plans) {
        this.list = list;
        this.plans = plans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (0 == viewType) {
            BillerPlanDesignBinding binding =
                    BillerPlanDesignBinding.inflate(LayoutInflater.
                            from(parent.getContext()), parent, false);
            return new BillerPlanViewHolder(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillerPlanViewHolder) {

            ((BillerPlanViewHolder) holder).binding.txtRechargeamount.setText(list.get(position).currency.concat(" ")
                    .concat(
                            String.valueOf(list.get(position).amount)));
            ((BillerPlanViewHolder) holder).binding.txtValidity.setText(list.get(position).validity);
            ((BillerPlanViewHolder) holder).binding.txtDescription.setText(list.get(position).description);
            ((BillerPlanViewHolder) holder).binding.planDetailsLayout.setVisibility(View.VISIBLE);

            Log.e("onBindViewHolder: ", String.valueOf(position));
            ((BillerPlanViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                plans.onBillerPlanSelect(list.get(position));
            });
        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {
            ((EmptyBeneficiaryListViewHolder) holder).binding.errorEmpty.setText("No plan found");
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else if (list.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }

        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (list.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }


    public static class BillerPlanViewHolder extends RecyclerView.ViewHolder {
        public BillerPlanDesignBinding binding;

        public BillerPlanViewHolder(@NonNull BillerPlanDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}

