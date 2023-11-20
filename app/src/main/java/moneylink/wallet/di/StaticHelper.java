package moneylink.wallet.di;


public class StaticHelper {

    public static String E_KEY = "";
    public static String REQUEST_KEY = "";
    public static String KEY = "";

    static {
        System.loadLibrary("native-lib");
    }
    public static native String method();
    public static native String nonMethod();

    public static String HEADER_EMPTY = "<soapenv:Header/>";

    public static String PARTNER_CODE = "111335";
    public static String USER_NAME = "TOTIPAYAPIUSER";
    public static String UserPassword = "Brim@1234";
    public static String LANGUAGE_ID = "1";

}

