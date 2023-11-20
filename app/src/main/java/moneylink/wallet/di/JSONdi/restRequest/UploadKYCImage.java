package moneylink.wallet.di.JSONdi.restRequest;

public class UploadKYCImage {
    public Credentials credentials = new Credentials();
    public String Customer_No;
    public String Image;
    public String Image_Name;


    @Override
    public String toString() {
        return "{" +
                "Credentials=" + credentials +
                ", Customer_No='" + Customer_No + '\'' +
                ", Image='" + Image + '\'' +
                ", Image_Name='" + Image_Name + '\'' +
                '}';
    }
}
