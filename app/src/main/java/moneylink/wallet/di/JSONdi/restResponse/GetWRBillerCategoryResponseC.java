package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class GetWRBillerCategoryResponseC extends ApiResponse<List<GetWRBillerCategoryResponseC>> {

    @SerializedName("ID")
    public Integer ID;
    @SerializedName("Name")
    public String Name;

    @SerializedName("IconName")
    public String IconName;

    @SerializedName("IconURL")
    public String IconURL;

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
