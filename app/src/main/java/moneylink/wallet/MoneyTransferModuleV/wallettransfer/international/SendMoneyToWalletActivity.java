package moneylink.wallet.MoneyTransferModuleV.wallettransfer.international;

import android.os.Bundle;

import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivitySendMoneyToWalletBinding;
import moneylink.wallet.fragments.BaseFragment;

public class SendMoneyToWalletActivity extends BaseFragment<ActivitySendMoneyToWalletBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
   
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_to_wallet;
    }




//    @OnClick({ R.id.next_layout,R.id.select_wallet})
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.next_layout:
//                startActivity(new Intent(getApplicationContext(), SendMoneyFifthActivity.class));
//                break;
//
//            case R.id.select_wallet:
//                startActivity(new Intent(getApplicationContext(),SendMoneyFifthActivity.class));
//
//                break;
//        }
//    }
}
