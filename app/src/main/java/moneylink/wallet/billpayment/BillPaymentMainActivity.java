package moneylink.wallet.billpayment;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.databinding.ActivityBillMainBinding;
import moneylink.wallet.di.JSONdi.restRequest.GetWRBillDetailRequestN;
import moneylink.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import moneylink.wallet.di.RequestHelper.WRBillerPlansRequest;
import moneylink.wallet.di.RequestHelper.WRPayBillRequest;

public class BillPaymentMainActivity extends TootiBaseActivity<ActivityBillMainBinding> {

    private NavController navController;
    public WRBillerPlansRequest plansRequest;
    public WRPayBillRequest payBillRequest;


    public GetWRBillerFieldsRequestN request;

    public GetWRBillDetailRequestN billDetailsRequest;


    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        plansRequest = new WRBillerPlansRequest();
        payBillRequest = new WRPayBillRequest();
        request = new GetWRBillerFieldsRequestN();
        billDetailsRequest = new GetWRBillDetailRequestN();

        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText("Bill Payment Services");

        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.payBillFragment || navController.getCurrentDestination()
                .getId() == R.id.utilityCategoryBFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}