package moneylink.wallet.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;

import moneylink.wallet.AutoLogoutActivity;
import moneylink.wallet.R;
import moneylink.wallet.TotiWallet;
import moneylink.wallet.dialogs.CloseDialog;
import moneylink.wallet.dialogs.PinVerificationDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnCancelInterface;
import moneylink.wallet.interfaces.OnUserPin;
import moneylink.wallet.interfaces.SessionOutListener;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.SessionManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public abstract class TootiBaseActivity<T extends ViewDataBinding>
        extends AppCompatActivity implements BaseFragment.Callback, SessionOutListener
        , OnUserPin, OnCancelInterface {

    private static final String TAG = TootiBaseActivity.class.getName();
    public T binding;
    public SessionManager sessionManager;
    public String walletBalance = "";

    // private Timer timer;
    private Boolean isUserTimedOut = false;
    public String customerNo;

    // protected V mViewModel;

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public void showProgress() {
    }

    public void hideProgress() {
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    // public abstract V getViewModel();
    @Override
    public void onFragmentAttached() {
        Log.d(TAG, "onFragmentAttached() called");
    }

    @Override
    public void onFragmentDetached(String tag) {
        //Log.d(TAG, "onFragmentDetached() called with: tag = [" + tag + "]");
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //  if (BuildConfig.FLAVOR_country.equals("saudi") && "ar".equals(LanguageUtil.getSelectedLanguageFromSharedPreferences(this))) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        //  } else {
        //      getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        //  }
        super.onCreate(savedInstanceState);
        /**
         * starting the session for auto logout
         */
        ((TotiWallet) getApplication()).registerSessionListener(this);
        ((TotiWallet) getApplication()).startUserSession();

        sessionManager = new SessionManager(this);
        performDataBinding();
        initUi(savedInstanceState);
        Constants.hideKeyboard(this);
    }

    /**
     * abstract method to init data
     *
     * @param savedInstanceState
     */
    protected abstract void initUi(Bundle savedInstanceState);

    public T getViewDataBinding() {
        return binding;
    }

    /**
     * Method will bind the layout with view
     */
    private void performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        //    this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
    }

    public void onMessage(String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(getString(R.string.cancel), v -> snackbar.dismiss());
        snackbar.show();
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
       ((TotiWallet) getApplication()).startUserSession();
       // TotiWallet.myAutoLogoutApp.touch();
    }

    /**
     * method will call from APP class if user do not interact with phone
     * on provided time
     */
    @Override
    public void doLogout() {
        isUserTimedOut = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUserTimedOut) {
            //show TimerOut dialog
            showTimedOutWindow();
        } else {
            ((TotiWallet) getApplication()).onUserInteracted();
        }
    }


    /**
     * method will show lock screen if user is not
     * in registration module if user login then showing
     */
    public void showTimedOutWindow() {
        sessionManager.clearUser();
        Intent intent = new Intent(TootiBaseActivity.this, AutoLogoutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void clearUserData() {
        sessionManager.clearUser();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void attachBaseContext(Context newBase) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(newBase);
        }

        /**
         * getting the default language
         */
        String newLocale = sessionManager.getDefaultLanguage();
        /**
         * putting the english as default if its empty
         * or user is new
         */
        if (newLocale.isEmpty()) {
            newLocale = "en"; // will be default
            sessionManager.setDefaultLanuguage(newLocale);
            sessionManager.setlanguageselection("1");
        }

        /**
         * changing the default language and
         * change the locale of application
         */

        Log.e(TAG, "attachBaseContext: " + " " + newLocale);
        //  Context context = ContextWrapper.changeLang(newBase, newLocale);
        super.attachBaseContext(updateBaseContextLocale(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private Context updateBaseContextLocale(Context context) {
        String language = sessionManager.getDefaultLanguage();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public void getPin() {
        PinVerificationDialog dialog = new PinVerificationDialog(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    public void onClose() {
        CloseDialog dialog = new CloseDialog(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction , "");
    }


    @Override
    public void onCancel() {
        finish();
    }

    @Override
    public void onVerifiedPin() {

    }

    @Override
    public void onResponseMessage(String message) {

    }


    /**
     * getting the previous id of bottom menu so we can handle the next view
     * or handle the navigation between bottom menu
     *
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        sessionManager.setCustomerNo(savedInstanceState.getString("customer_no"));
    }

    /**
     * storing the previous id of bottom menu so we can handle the next view
     * or handle the navigation between bottom menu
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("customer_no", sessionManager.getCustomerNo());
        super.onSaveInstanceState(outState);
    }
}

