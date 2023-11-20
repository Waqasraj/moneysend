package moneylink.wallet.interfaces;

import java.util.List;

public interface OnGetBankNameList extends OnMessageInterface {
    void onGetBankList(List<String> lists);
}
