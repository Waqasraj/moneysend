package moneylink.wallet.interfaces;

public interface OnCustomerCardNo extends OnMessageInterface{
    void onCreateCustomerCardNo(String customerCardNo);
    void createCustomerCard();
    void onGetCustomerCardNo(String customerCardNo);
}
