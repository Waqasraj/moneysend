package moneylink.wallet.interfaces;

public interface OnOTPVerified extends OnMessageInterface{
    void onOTPVerified(boolean isVerified);
}
