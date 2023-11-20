package moneylink.wallet.utils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * number formatter class
 * use this for number formation
 * @author waqas
 * @since 05-05-2019
 */
public class NumberFormatter {


    /**
     * method will return formated
     * string and accepting number and then
     * convert it currency format
     * @param number floating/decimal number
     * @return currencyformat string will return
     */
    public static String getFormattedCurrency(Float number){
        NumberFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(number);
        // return NumberFormat.getNumberInstance(Locale.US).format(number);
    }


    public static String decimal(Float number) {
        return String.format(Locale.ENGLISH, "%.2f", number);
    }


    public static String decimalSix(String number) {
        return String.valueOf(number);
    }

    /**
     * METHOD WILL return passing number into decimal format
     * @param number accept any flaot number
     * @return string with format decimal
     */
    public static String getDecimalFormat(Float number){
        return new DecimalFormat("##.##").format(number);
    }

    /**
     * method will remove commas from string with rejex
     * we need this method to paas some value to server
     * before passing to server we need to remove commas
     * @param number accept the number
     * @return return simple number
     */

    public static String removeCommas(String number) {
        String regex = "(?<=[\\d])(,)(?=[\\d])";
        Pattern p = Pattern.compile(regex);
        String str = number.trim();
        Matcher m = p.matcher(str);
        str = m.replaceAll("");
        return str;
    }


    public static String removeSlash(String number) {
        String regex = "(?<=[\\d])(-)(?=[\\d])";
        Pattern p = Pattern.compile(regex);
        String str = number.trim();
        Matcher m = p.matcher(str);
        str = m.replaceAll("");
        return str;
    }


}
