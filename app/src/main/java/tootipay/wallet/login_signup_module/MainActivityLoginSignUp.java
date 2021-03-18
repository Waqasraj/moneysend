package tootipay.wallet.login_signup_module;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.MainSignupLoginLayoutBinding;
import tootipay.wallet.di.RequestHelper.GetTotiPayAccDetails;
import tootipay.wallet.di.apicaller.TotiPayBankAccountDetailsTask;
import tootipay.wallet.login_signup_module.viewmodels.LoginRegisterViewModel;

public class MainActivityLoginSignUp extends TootiBaseActivity<MainSignupLoginLayoutBinding> {

    NavController navController;
    public LoginRegisterViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.main_signup_login_layout;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        viewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

//        GetTotiPayAccDetails request = new GetTotiPayAccDetails();
//        request.languageId = sessionManager.getlanguageselection();
//        request.countryShortName = "yem";
//        TotiPayBankAccountDetailsTask task = new TotiPayBankAccountDetailsTask();
//        task.execute(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.newLoginCheck) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}
