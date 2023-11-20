package moneylink.wallet.utils;

import android.util.Log;
import android.util.Patterns;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * program for check validation for fields
 *
 * @author waqas
 * @since 05-05-2019
 */

public class CheckValidation {

    public static boolean isValidName(String name) {
        if(!name.matches("^[a-zA-Z.\\s]+")) {
            return true;
        }
        return false;
    }

    /**
     * this method will accept any country number
     * and give result accordingly that number in boolean
     * result will be based on country phone number if format
     * will match any country format then true will return
     * otherwise false
     *
     * @param mobNumber
     * @param countryCode
     * @return
     */
    public static boolean isPhoneNumberValidate(String mobNumber, String countryCode) {

        if (!isValidMobile(mobNumber)) {
            return false;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        boolean isValid = false;
        boolean isNumberValid = false;
        try {
            String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
            phoneNumber = phoneNumberUtil.parse(mobNumber, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);
            isNumberValid = false;
        } catch (NumberParseException | NullPointerException | NumberFormatException e) {
            e.printStackTrace();
        }
        if (isValid && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile ||
                PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile)) {
            isNumberValid = true;
        }

        return isNumberValid;
    }

    public static boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 10 || phone.length() > 13)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }


    public static boolean isValidPhoneNumber(String phone) {
        if (!phone.trim().equals("") && phone.length() > 10) {
            return Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }


    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 11;
        }
        return false;
    }


    /**
     * this method will accept any country number
     * and give result accordingly that number in boolean
     * result will be based on country phone number if format
     * will match any country format then true will return
     * otherwise false
     *
     * @param phoneNumber user phone number
     * @return boolean
     */

    public static boolean checkValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumber, null);
            return phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid

        } catch (NumberParseException e) {
            Log.e("NumberParseException", e.toString());
        }
        return false;
    }


    /**
     * method will get input as cahravet and check
     * that this email pattern or not
     * and return result in boolean
     *
     * @param target email
     * @return @boolean
     */

    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * check the password validation
     * user must have to enter one capital letter
     * we can apply more security on password
     * below we have patterns
     *
     * @param password password
     * @return boolean
     */
    public static boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    /**
     * method will check if enter password length is 6 or greater than 6
     *
     * @param password
     * @return
     */
    public static boolean isSixDigit(String password) {
        if (password.length() >= 6) {
            return false;
        }
        return true;
    }


    /**
     * method will check if enter password length is 6 or greater than 6
     *
     * @param password
     * @return
     */
    public static boolean isValidPinLength(String password) {
        if (password.length() != 4) {
            return true;
        }
        return false;
    }


    /**
     * method will check pin code length
     * @param pinCode
     * @return
     */
    public static boolean isValidPin(String pinCode) {
        return pinCode.length() == 4;
    }


   /* ^ # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
        (?=\\S+$)          # no groupespace allowed in the entire string
        .{4,}             # anything, at least six places though
        $*/
}
