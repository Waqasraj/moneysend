package moneylink.wallet.interfaces;

public interface OnReceiptGenerator {
    void onSaveAsPdf();
    void onSentAsWhatsApp();
    void onSentAsOthers();
    void onSaveAsImage();
}
