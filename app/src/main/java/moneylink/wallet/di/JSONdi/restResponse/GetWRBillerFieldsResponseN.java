package moneylink.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import moneylink.wallet.di.JSONdi.generic_response.ApiResponse;


public class GetWRBillerFieldsResponseN extends ApiResponse<GetWRBillerFieldsResponseN> {


    @SerializedName("Inquiry_Available_")
    public String inquiryAvailable;
    @SerializedName("PayOut_Currency_")
    public String payOutCurrency;

    @SerializedName("Biller_Fields_")
    public List<Fields> billerFields;


    public static class Fields {
        @SerializedName("id")
        public String id;
        @SerializedName("fieldName")
        public String fieldName;
        @SerializedName("labelName")
        public String labelName;
        @SerializedName("description")
        public String description;
        @SerializedName("type")
        public String type;
        @SerializedName("maxLength")
        public Integer maxLength;
        @SerializedName("minLength")
        public Integer minLength;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
        }

        public Integer getMinLength() {
            return minLength;
        }

        public void setMinLength(Integer minLength) {
            this.minLength = minLength;
        }
    }
}
