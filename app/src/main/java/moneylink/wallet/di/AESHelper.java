package moneylink.wallet.di;


import android.util.Base64;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {
    /**
     * Method for Encrypt Plain String Data
     * @param plainText
     * @return encryptedText
     */

    //cipher
    //byte[]
    //secretkeyspec
    //ivparamterspec

    public static String encrypt(String plainText, String encryptionKey) {
        String encryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] key = encryptionKey.trim().getBytes("UTF-8");

            byte[] keyBytes = new byte[16];
            int length = key.length;
            if (length > keyBytes.length) {
                length = keyBytes.length;
            }

            keyBytes = Arrays.copyOf(key , length);


            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES"); // for init the cipher
            IvParameterSpec ivparameterspec = new IvParameterSpec(keyBytes); // for init the cipher
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            return Base64.encodeToString(cipherText, Base64.NO_WRAP);
        } catch (Exception E) {
            System.err.println("Encrypt Exception : " + E.getMessage());
        }
        return encryptedText;
    }




    public static String decrypt(String plainText, String enc) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] key = (enc).getBytes("UTF-8");

            byte[] keyBytes = new byte[16];
            int length = key.length;
            if (length > keyBytes.length) {
                length = keyBytes.length;

            }

            keyBytes = Arrays.copyOf(key , length);


            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);

            byte[] cipherText = Base64.decode(plainText.getBytes("UTF8"), Base64.DEFAULT);

            return new String(cipher.doFinal(cipherText), "UTF-8");


        } catch (Exception E) {

            System.err.println("Decrypt Exception : " + E.getMessage());

        }
        return decryptedText;
    }


}
