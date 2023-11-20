package moneylink.wallet.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moneylink.wallet.R;

/**
 * Created by HS on 27-Feb-18.
 * FIL AHM
 */

public class Constants {
    public static final String WR_BILLER_PAYOUT_CURRENCY = "usd";
    public static String isWalletUpdate = "update_wallet";

    public static boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int specialCharacterCount(String count) {
        String SPECIAL_CHARS_REGEX = "[$&+,:;=\\\\?@#|/'<>.^*()%!-]";
//        String SPECIAL_CHARS_REGEX = "[!@#$%^&*()\\[\\]|;',./{}\\\\:\"<>?]";
        int specials = count.split(SPECIAL_CHARS_REGEX, -1).length - 1;
        return specials;
    }

    public static boolean validateSpecialCharacter(String character) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "[$&+,:;=\\\\?@#|/'<>.^*()%!-]";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(character);
        return matcher.matches();
    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {
        File mediaFile;

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "temp");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        if (!mediaFile.exists())
            try {
                mediaFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return mediaFile;
    }


    public static void showMessage(View view, Context context, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen._12ssp));
        textView.setMaxLines(2);
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();
    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) {

        String parsedDate = "";
        try {
            Date initDate = new SimpleDateFormat(initDateFormat, Locale.getDefault()).parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat, Locale.getDefault());
            parsedDate = formatter.format(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedDate;
    }

    public static String formatAmount(String value) {
        DecimalFormat formatter = new DecimalFormat("#,##,##,##,###");
        String yourFormattedString = "";
        try {
            yourFormattedString = formatter.format(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return yourFormattedString;
    }

    public static String stringBase64Encode(String string) {
        byte[] mString = Base64.encode(string.getBytes(), Base64.DEFAULT);
        return Arrays.toString(mString);
    }

    public static String stringBase64Decode(String string) {
        try {
            byte[] mString = Base64.decode(string.getBytes("UTF-8"), Base64.DEFAULT);
            return Arrays.toString(mString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String base64EncodeToString(String string) {
//        Base64.encodeToString(edSearchFeed.getText().toString().getBytes(), Base64.NO_WRAP)
        String mString = Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);
        return mString;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static float findNumericValue(String string) {
        try {
            return Float.parseFloat(string.replaceAll("[^.0-9]", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Dialog showMessageOkCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", okListener);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();

        return alert;
    }

    public static void showKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}