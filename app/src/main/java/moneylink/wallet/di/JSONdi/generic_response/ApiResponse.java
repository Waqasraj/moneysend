package moneylink.wallet.di.JSONdi.generic_response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T>{

    @SerializedName("ResponseCode")
    public Integer responseCode;
    @SerializedName("Description")
    public String description;
    @SerializedName("data")
    public T data;

    public T getData() {
        return data;
    }

}
