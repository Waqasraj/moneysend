package moneylink.wallet.interfaces;

import moneylink.wallet.di.ResponseHelper.CustomerProfile;

public interface OnGetCustomerProfile extends OnMessageInterface {
    void onGetCustomerProfile(CustomerProfile customerProfile);
}
