package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class ResponseApi {

    @SerializedName("ResponseCode")
    public Integer status;

    @SerializedName("Description")
    public String description;

    //
    @SerializedName("Version")
    public String version = "";

}
