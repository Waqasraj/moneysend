package moneylink.wallet.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import moneylink.wallet.AutoLogoutActivity;
import moneylink.wallet.di.ResponseHelper.CustomerProfile;


import moneylink.wallet.login_signup_module.helper.RegisterUserRequest;

/**
 * Created by HS on 01-Mar-18.
 * FIL AHM
 */

public class SessionManager {

    private static final String PREF_NAME = "login_session";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;
    private final int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }



    /**
     * Create login session
     */
    public void createLoginSession() {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // commit changes
        editor.commit();
    }

    public void setIsLogin(boolean login) {
        editor.putBoolean(IS_LOGIN, login);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUserInBackgroundOrForeground(Context context) {
        setIsLogin(false);
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, AutoLogoutActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);
    }


    public void walletBalance(Double walletBalance) {
        editor.putFloat("wallet_balance", walletBalance.floatValue());
        editor.commit();
    }

    public Float getWalletBalance() {
        return pref.getFloat("wallet_balance", 0.0f);
    }


    public void setCustomerNo(String customerNo) {
        editor.putString("customer_no", customerNo);
        editor.commit();
    }

    public void setCustomer(RegisterUserRequest userRequest) {
        editor.putString("first_name", userRequest.firstName);
        editor.putString("last_name", userRequest.lastName);
        editor.putString("middle_name", userRequest.middleName);
        editor.putString("last_name", userRequest.lastName);
        editor.putString("address", userRequest.address);
        editor.putString("dob", userRequest.dob);
        editor.putString("gender", userRequest.gender);
        editor.putString("phone_number", userRequest.phoneNumber);
        editor.putString("nationlaity", userRequest.nationality);
        editor.putString("emailID", userRequest.email);
        editor.putString("residenceCountry", userRequest.country);
        editor.commit();
    }

    public void setCustomerGet(CustomerProfile userRequest) {
        // editor.putString("customer_no" , userRequest.customerNo);
        editor.putString("first_name", userRequest.firstName);
        editor.putString("last_name", userRequest.lastName);
        editor.putString("middle_name", userRequest.middleName);
        editor.putString("last_name", userRequest.lastName);
        editor.putString("address", userRequest.address);
        editor.putString("dob", userRequest.dateOfBirth);
        editor.putString("gender", userRequest.gender.trim());
        editor.putString("phone_number", userRequest.mobileNo);
        editor.putString("nationlaity", userRequest.nationality);
        editor.putString("emailID", userRequest.email);
        editor.putString("residenceCountry", userRequest.residenceCountry);
        editor.commit();
    }


    public String getUserName() {
        return pref.getString("first_name", "").concat(" ")
                .concat(pref.getString("last_name", ""));
    }


    public void customerImage(String customerImage) {
        editor.putString("customer_image", customerImage);
        editor.commit();
    }


    public String getCustomerImage() {
        return pref.getString("customer_image", "");
    }


    public void putCustomerPhone(String phone) {
        editor.putString("phone_number", phone);
        editor.commit();
    }


    public void putEmailAddress(String email) {
        editor.putString("emailID", email);
        editor.commit();
    }

    public void userEmailRemember(String email) {
        editor.putString("userEmailRemember", email);
        editor.commit();
    }

    public String getRememberEmail() {
        return pref.getString("userEmailRemember", "");
    }

    public void userPhoneRemember(String url, String iso, String number) {
        editor.putString("url", url);
        editor.putString("iso_remember", iso);
        editor.putString("number_remember", number);
        editor.commit();
    }


    public String getURLFlag() {
        return pref.getString("url", "");
    }

    public String getUserCountryCode() {
        return pref.getString("iso_remember", "+1");
    }

    public String getUserNumberRemember() {
        return pref.getString("number_remember", "");
    }

    public String getCustomerPhone() {
        return pref.getString("phone_number", "");
    }

    public String getEmail() {
        return pref.getString("emailID", "");
    }


    public RegisterUserRequest getCustomerDetails() {
        RegisterUserRequest userRequest = new RegisterUserRequest();
        userRequest.firstName = pref.getString("first_name", "");
        userRequest.lastName = pref.getString("last_name", "");
        userRequest.middleName = pref.getString("middle_name", "");
        userRequest.address = pref.getString("address", "");
        userRequest.dob = pref.getString("dob", "");
        userRequest.gender = pref.getString("gender", "");
        userRequest.phoneNumber = pref.getString("phone_number", "");
        userRequest.nationality = pref.getString("nationlaity", "");
        userRequest.email = pref.getString("emailID", "");
        userRequest.country = pref.getString("residenceCountry", "");
        return userRequest;
    }


    public String getResidenceCountry() {
        return pref.getString("residenceCountry", "GBR");
    }


    public void setDocumentsUploaded(Boolean customerISNew) {
        editor.putBoolean("is_user_new", customerISNew);
        editor.commit();
    }

    public boolean getIsDocumentsUploaded() {
        return pref.getBoolean("is_user_new", true);
    }

    public void isKYCApproved(Boolean status) {
        editor.putBoolean("isKYCApproved", status);
        editor.commit();
    }


    public void putWalletNeedToUpdate(boolean isNeed) {
        editor.putBoolean(Constants.isWalletUpdate, isNeed);
        editor.commit();
    }


    public boolean isWalletNeedToUpdate() {
        return pref.getBoolean(Constants.isWalletUpdate, true);
    }


    public Boolean getISKYCApproved() {
        return true;
    }


    public String getCustomerNo() {
        return pref.getString("customer_no", "");
    }

    public boolean getCustomerStatus() {
        return pref.getBoolean("is_user_new", false);
    }


    public void updateUserProfile(String userDetail) {
        editor.putString("user_detail", userDetail);
        editor.commit();
    }

    public boolean getRememberMe() {
        return pref.getBoolean("remember_me", false);
    }

    public void setRememberMe(boolean rememberMe) {
        editor.putBoolean("remember_me", rememberMe);
        editor.commit();
    }

    public String getDeviceToken() {
        return pref.getString("device_token", "");
    }

    public String getLoginWith() {
        return pref.getString("login_with", "");
    }

    public String getuserflagimage() {
        return pref.getString("flag_image", "");
    }

    public void setDeviceToken(String deviceToken) {
        editor.putString("device_token", deviceToken);
        editor.commit();
    }

    public String getlanguageselection() {
        return pref.getString("language_selection", "1");
    }

    public void setlanguageselection(String languageId) {
        editor.putString("language_selection", languageId);
        editor.commit();
    }

    public void setLoginWith(String loginwith) {
        editor.putString("login_with", loginwith);
        editor.commit();
    }

    public void setuserflagimage(String flagimage) {
        editor.putString("flag_image", flagimage);
        editor.commit();
    }


    public boolean getLogin() {
        return pref.getBoolean("is_login", false);
    }

    public void setLogin(boolean flag) {
        editor.putBoolean("is_login", flag);
        editor.commit();
    }


    public void setWalletBalance(float balance) {
        editor.putFloat("wallet_balance", balance);
        editor.commit();
    }


    public boolean getVerify() {
        return pref.getBoolean("is_verify", false);
    }

    public boolean getLogoutVerify() {
        return pref.getBoolean("is_logout_verify", false);
    }

    public void setVerify(boolean flag) {
        editor.putBoolean("is_verify", flag);
        editor.commit();
    }

    public void setLogoutVerify(boolean flag) {
        editor.putBoolean("is_logout_verify", flag);
        editor.commit();
    }

    public boolean isUserDataEmpty() {
        return pref.getString("gender", "").isEmpty();
    }

    public void clearUser() {
        editor.putString("first_name", "");
        editor.putString("last_name", "");
        editor.putString("middle_name", "");
        editor.putString("last_name", "");
        editor.putString("address", "");
        editor.putString("dob", "");
        editor.putString("gender", "");
        editor.putString("phone_number", "");
        editor.putString("nationlaity", "");
        editor.putString("emailID", "");
        editor.putString("residenceCountry", "");
        editor.putString("customer_no", "");
        editor.putFloat("wallet_balance", 0.0f);
        editor.commit();
    }


    public void setDefaultLanuguage(String language) {
        editor.putString("default_language", language);
        editor.commit();
    }


    public String getDefaultLanguage() {
        return pref.getString("default_language", "");
    }

    public void setVirtualCardNo(String customerCardNo) {
        editor.putString("virtual_card_no", customerCardNo);
        editor.commit();
    }

    public String getVirtualCardNo() {
        return pref.getString("virtual_card_no", "");
    }

    public void setIpAddress(String ip) {
        editor.putString("setIpAddress", ip);
        editor.commit();
    }

    public void IpCountryName(String country_name) {
        editor.putString("IpCountryName", country_name);
        editor.commit();
    }

    public String getIpAddress() {
        return pref.getString("setIpAddress", "");
    }

    public String getIpCountryName() {
        return pref.getString("IpCountryName", "");
    }


    public void setLoginPhone(String phone) {
        editor.putString("setLoginPhone", phone);
        editor.commit();
    }


    public String getLoginPhone() {
        return pref.getString("setLoginPhone", "");
    }
}