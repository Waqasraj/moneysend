package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;

public interface OnWRBillerFields extends OnMessageInterface {
    void onWRBillerField(List<GetWRBillerFieldsResponseN.Fields> response);

}
