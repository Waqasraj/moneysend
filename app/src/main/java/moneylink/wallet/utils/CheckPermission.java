package moneylink.wallet.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * @author muhammad waqas
 * @version 1.0
 * <p>
 * program will be used to check permissions
 */
public class CheckPermission {

    /**
     * method will check camera permissions
     * @param context
     * @return
     */
    public static boolean checkCameraPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * method will check location permissions
     * @param context
     * @return
     */
    public static boolean checkLocationPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

}
