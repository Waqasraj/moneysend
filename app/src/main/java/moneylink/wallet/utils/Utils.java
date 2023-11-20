package moneylink.wallet.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import moneylink.wallet.R;

public class Utils {
    private static Dialog dialog;
    private static CustomCircularProgressView ivProgressBar;

    public static final String DATE_TIME_FORMAT_WEB = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static void showCustomProgressDialog(Context context, boolean isCancel) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        if (isInternetConnected(context) && !((AppCompatActivity) context).isFinishing()) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.circuler_progerss_bar_two);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ivProgressBar = (CustomCircularProgressView) dialog.findViewById(R.id.ivProgressBarTwo);
            ivProgressBar.startAnimation();
            dialog.setCancelable(isCancel);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setDimAmount(0);
            dialog.show();
        }


    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static void hideCustomProgressDialog() {
        try {
            if (dialog != null && ivProgressBar != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public static String getDateFromServerTime(String serverTime) {
        Date date = null;
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Calendar serverTimeCalendar = Calendar.getInstance(timeZone);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_WEB, Locale.US);
            //   date = simpleDateFormat.parse(serverTime);
            serverTimeCalendar.setTime(simpleDateFormat.parse(serverTime));

        } catch (ParseException e) {
            Log.e(Utils.class.getName(), e.getLocalizedMessage());
        }

        return getFormatTime(serverTimeCalendar.getTime());
    }

    public static String convertServerTime(String serverTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a 'on' MMMM dd, yyyy");
        return sdf.format(serverTime);
    }

    public static String getFormatTime(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a 'on' MMMM dd, yyyy");
        return sdf.format(time);
    }

}
