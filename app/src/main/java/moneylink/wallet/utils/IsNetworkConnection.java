package moneylink.wallet.utils;

import android.content.Context;

/*
 * IsNetworkConnection.java is static class to check network connection. return true is yes otherwise false.
 * */
public class IsNetworkConnection {
    /**
     * Checks if the device has Internet connection.
     * @return <code>true</code> if the phone is connected to the Internet.
     */

    public static boolean checkNetworkConnection(Context context) {
        boolean connected = false;
        connected = Connection.isConnectedFast(context);
        return connected;
    }
}