package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class GetProfileImage {

    @SerializedName("ImageData")
    public String imageData;

    @SerializedName("ResponseCode")
    public Integer status;

    @SerializedName("Description")
    public String description;
}
