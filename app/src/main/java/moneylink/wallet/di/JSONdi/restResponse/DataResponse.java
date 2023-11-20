package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class DataResponse {
    @SerializedName("Key")
    public String Key;
    @SerializedName("Version")
    public String version;
    @SerializedName("ResponseCode")
    public Integer responseCode;
    @SerializedName("Description")
    public String description;
}
