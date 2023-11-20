package moneylink.wallet.home_module.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.CustomerProfile;
import moneylink.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;

public class HomeViewModel extends ViewModel {
    public CustomerProfile customerProfile = new CustomerProfile();
    public List<GetCustomerWalletDetailsResponse> walletDetailsResponses;

}
