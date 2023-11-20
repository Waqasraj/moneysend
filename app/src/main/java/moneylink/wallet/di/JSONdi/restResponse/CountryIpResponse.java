package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class CountryIpResponse {
    @SerializedName("ip")
    public String ip;
    @SerializedName("version")
    public String version;
    @SerializedName("city")
    public String city;
    @SerializedName("region")
    public String region;
    @SerializedName("region_code")
    public String region_code;
    @SerializedName("country_name")
    public String country_name;
    @SerializedName("country_code")
    public String country_code;
    @SerializedName("country_code_iso3")
    public String country_code_iso;
    @SerializedName("country_capital")
    public String capital;
    @SerializedName("country_tld")
    public String country_tld;
    @SerializedName("continent_code")
    public String continent_code;
}
