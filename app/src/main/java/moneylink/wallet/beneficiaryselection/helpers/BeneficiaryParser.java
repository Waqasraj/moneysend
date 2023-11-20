package moneylink.wallet.beneficiaryselection.helpers;

import moneylink.wallet.di.ResponseHelper.GetBeneficiaryListResponse;

import java.util.ArrayList;
import java.util.List;

public class BeneficiaryParser {
    public static String CASH_BENEFICIARY = "cash";
    public static String BANK_BENEFICIARY = "bank";

    public static List<GetBeneficiaryListResponse>
    getBankBeneficiary(List<GetBeneficiaryListResponse> listResponses) {
        List<GetBeneficiaryListResponse> responses = new ArrayList<>();
        for (int i = 0; i < listResponses.size(); i++) {
            if(listResponses.get(i).paymentMode.equalsIgnoreCase(BANK_BENEFICIARY)) {
                responses.add(listResponses.get(i));
            }
        }
        return responses;
    }


    public static List<GetBeneficiaryListResponse>
    getCashBeneficiary(List<GetBeneficiaryListResponse> listResponses) {
        List<GetBeneficiaryListResponse> responses = new ArrayList<>();
        for (int i = 0; i < listResponses.size(); i++) {
            if(listResponses.get(i).paymentMode.equalsIgnoreCase(CASH_BENEFICIARY)) {
                responses.add(listResponses.get(i));
            }
        }
        return responses;
    }

}
