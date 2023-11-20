package moneylink.wallet.di;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class HTTPHelper {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String baseUrlFromJNII();

    public static URLConnection getURL() {

        URL url = null;
        try {
            url = new URL(baseUrlFromJNII());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * @param xml
     * @param soapAction
     * @param method
     * @return
     */
    public static String getResponse(String xml, String soapAction
            , String method) {
        String responseString = "";
        String outputString = "";
        HttpURLConnection httpConn = (HttpURLConnection) HTTPHelper.getURL();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        byte[] buffer = new byte[xml.length()];
        buffer = xml.getBytes();
        try {
            bout.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = bout.toByteArray();
        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", soapAction);

        try {
            httpConn.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(30000);
        httpConn.setReadTimeout(30000);
        BufferedReader in;
        try {
            OutputStream out = httpConn.getOutputStream();
            out.write(b);
            out.close();

            InputStreamReader isr =
                    new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);
            //Write the SOAP message response to a String.
            while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputString;
    }
}
