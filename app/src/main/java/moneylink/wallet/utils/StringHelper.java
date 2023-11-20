package moneylink.wallet.utils;

public class StringHelper {
    public static String removeFirst(String input) {
        return input.substring(1);
    }

    public static String firstLetterCap(String txt) {
        StringBuilder sb = new StringBuilder(txt);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }


    public static String parseNumber(String userNumber){
        if(userNumber.charAt(0) == '0') {
            userNumber =  StringHelper.removeFirst(userNumber);
        }

        if(userNumber.charAt(0) == '0') {
            userNumber = StringHelper.removeFirst(userNumber);
        }

        if(userNumber.charAt(0) == '+') {
            userNumber = StringHelper.removeFirst(userNumber);
        }
        return userNumber;
    }

    public static String addDishes(String original) {
        // String original = number;
        int interval = 4;
        char separator = '-';

        StringBuilder sb = new StringBuilder(original);

        for (int i = 0; i < original.length() / interval - 1; i++) {
            sb.insert(((i + 1) * interval) + i, separator);
        }
        return sb.toString();
    }
}
