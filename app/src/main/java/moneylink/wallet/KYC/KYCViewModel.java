package moneylink.wallet.KYC;

import androidx.lifecycle.ViewModel;

import moneylink.wallet.di.RequestHelper.EditCustomerProfileRequest;

public class KYCViewModel extends ViewModel {
    public byte[] frontPicture;
    public byte[] backPicture;
    public byte[] customerPicture;
    public EditCustomerProfileRequest editCustomerProfileRequest = new EditCustomerProfileRequest();

}
