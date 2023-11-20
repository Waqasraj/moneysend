package moneylink.wallet.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressBar {
    public ProgressDialog progressDialog;


    // Method to show Progress bar
    public void showProgressDialogWithTitle(Context context, String substring) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }

    // Method to hide/ dismiss Progress bar
    public void hideProgressDialogWithTitle() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public boolean isShowing() {
        return progressDialog.isShowing();
    }
}
