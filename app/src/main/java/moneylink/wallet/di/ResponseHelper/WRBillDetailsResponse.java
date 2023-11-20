package moneylink.wallet.di.ResponseHelper;

import android.os.Parcel;
import android.os.Parcelable;

public class WRBillDetailsResponse implements Parcelable {
    public String billerID;
    public String skuID;
    public String balanceORDue = "";
    public String customerName = "";
    public String billDueDate = "";


    public WRBillDetailsResponse() {
    }

    protected WRBillDetailsResponse(Parcel in) {
        billerID = in.readString();
        skuID = in.readString();
        balanceORDue = in.readString();
        customerName = in.readString();
        billDueDate = in.readString();
    }

    public static final Creator<WRBillDetailsResponse> CREATOR = new Creator<WRBillDetailsResponse>() {
        @Override
        public WRBillDetailsResponse createFromParcel(Parcel in) {
            return new WRBillDetailsResponse(in);
        }

        @Override
        public WRBillDetailsResponse[] newArray(int size) {
            return new WRBillDetailsResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billerID);
        dest.writeString(skuID);
        dest.writeString(balanceORDue);
        dest.writeString(customerName);
        dest.writeString(billDueDate);
    }
}
