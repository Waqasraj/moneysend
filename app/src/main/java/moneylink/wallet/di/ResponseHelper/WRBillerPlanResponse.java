package moneylink.wallet.di.ResponseHelper;

import android.os.Parcel;
import android.os.Parcelable;

public class WRBillerPlanResponse implements Parcelable {
    public String billerName;
    public String billerType;
    public String description;
    public Double minAmount;
    public Double maxAmount;
    public Double payableAmount;
    public Integer billerId;
    public Integer billerSKUId;
    public Integer billerTypeId;
    public Integer billerCategoryID;
    public String countryCode;


    public WRBillerPlanResponse() {
    }

    protected WRBillerPlanResponse(Parcel in) {
        billerName = in.readString();
        billerType = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            minAmount = null;
        } else {
            minAmount = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxAmount = null;
        } else {
            maxAmount = in.readDouble();
        }
        if (in.readByte() == 0) {
            payableAmount = null;
        } else {
            payableAmount = in.readDouble();
        }
        if (in.readByte() == 0) {
            billerId = null;
        } else {
            billerId = in.readInt();
        }
        if (in.readByte() == 0) {
            billerSKUId = null;
        } else {
            billerSKUId = in.readInt();
        }
        if (in.readByte() == 0) {
            billerTypeId = null;
        } else {
            billerTypeId = in.readInt();
        }
        if (in.readByte() == 0) {
            billerCategoryID = null;
        } else {
            billerCategoryID = in.readInt();
        }
        countryCode = in.readString();
    }

    public static final Creator<WRBillerPlanResponse> CREATOR = new Creator<WRBillerPlanResponse>() {
        @Override
        public WRBillerPlanResponse createFromParcel(Parcel in) {
            return new WRBillerPlanResponse(in);
        }

        @Override
        public WRBillerPlanResponse[] newArray(int size) {
            return new WRBillerPlanResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billerName);
        dest.writeString(billerType);
        dest.writeString(description);
        if (minAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minAmount);
        }
        if (maxAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(maxAmount);
        }
        if (payableAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(payableAmount);
        }
        if (billerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(billerId);
        }
        if (billerSKUId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(billerSKUId);
        }
        if (billerTypeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(billerTypeId);
        }
        if (billerCategoryID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(billerCategoryID);
        }
        dest.writeString(countryCode);
    }
}
