package moneylink.wallet.interfaces;

import java.util.List;

import moneylink.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;

public interface OnWRBillerNames extends OnMessageInterface {
    void onBillerNamesResponse(List<GetWRBillerNamesResponseC> responses);

    void onSelectBillerName(GetWRBillerNamesResponseC billerName);
}
