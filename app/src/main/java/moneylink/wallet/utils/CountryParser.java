package moneylink.wallet.utils;



import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;


public class CountryParser {
    public static final int SEND = 2;
    public static final int RECEIVE = 1;
    public static final int SEND_RECEIVE = 3;


    public static List<GetCountryListResponse>
    getSendingCountries(List<GetCountryListResponse> responseList) {
        List<GetCountryListResponse> parsedList = new ArrayList<>();
        for(int i = 0 ; i<responseList.size() ; i++) {
            if(responseList.get(i).countryType != RECEIVE) {
                parsedList.add(responseList.get(i));
            }
        }
        return parsedList;
    }


    public static List<GetCountryListResponse>
    getReceivingCountries(List<GetCountryListResponse> responseList) {
        List<GetCountryListResponse> parsedList = new ArrayList<>();
        for(int i = 0 ; i < responseList.size() ; i++) {
            if(responseList.get(i).countryType != SEND) {
                parsedList.add(responseList.get(i));
            }
        }
        return parsedList;
    }
}
