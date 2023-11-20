package moneylink.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivityElectricityBillBinding;
import moneylink.wallet.fragments.BaseFragment;


public class ElectricityBillActivity extends BaseFragment<ActivityElectricityBillBinding> {

    String payment_type = "";

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        payment_type = getArguments().getString("payment");

        binding.addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.findNavController(view)
//                        .navigate(R.id.action_electricityBillActivity_to_addBeneficiaryTypeActivity);
                //  startActivity(new Intent(getApplicationContext(), AddBaneficilyTypeActivity.class));
            }
        });

        binding.custCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("payment",payment_type);
//                Navigation.findNavController(view)
//                        .navigate(R.id.action_electricityBillActivity_to_newOutstandingBillActivity , bundle);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_electricity_bill;
    }


}