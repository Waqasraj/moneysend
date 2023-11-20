package moneylink.wallet.interfaces;

public interface OnSelectItem<T> {
    void onSelectItem(T item);
    void onResponseMessage(String message);
}
