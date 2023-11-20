package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.ResponseHelper.WRBillerTypeResponse;

public interface OnWRBillerType extends OnMessageInterface{
    void onBillerTypeList(List<WRBillerTypeResponse> billerTypeList);
    void onBillerTypeSelect(WRBillerTypeResponse billerType);
}
