package moneylink.wallet.di.ResponseHelper;

import android.os.Parcel;
import android.os.Parcelable;

public class GetBeneficiaryListResponse implements Parcelable {
    public boolean isHoldForOfAC;
    public String beneficiaryNumber;
    public String customerNo;
    public String beneficiaryNo;
    public String firstName;
    public String middleName;
    public String lastName;
    public String nickName;
    public String address;
    public String dateOfBirth;
    public String telephone;
    public String nationality;
    public String idNumber;
    public String idType;
    public String idIssueDate;
    public String idExpiryDate;
    public String payOutCurrency;
    public String paymentMode;
    public String paymentBranchCode;
    public String bankName;
    public String bankCountry;
    public String branchName;
    public String bankCode;
    public String accountNumber;
    public String isActive;
    public String purposeCode;
    public String purposeOfTransfer;
    public String payoutAgent;
    public String payoutBranchName;
    public String payOutCountryCode;
    public String payoutCityCode;
    public String landMark;
    public String zipCode;
    public String emailId;
    public String customerRelation;
    public String idtype_Description;
    public String imageURL;

    //extra variables
    public String beneficiaryRelationName;

    public GetBeneficiaryListResponse() {
    }

    protected GetBeneficiaryListResponse(Parcel in) {
        isHoldForOfAC = in.readByte() != 0;
        beneficiaryNumber = in.readString();
        customerNo = in.readString();
        beneficiaryNo = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        nickName = in.readString();
        address = in.readString();
        dateOfBirth = in.readString();
        telephone = in.readString();
        nationality = in.readString();
        idNumber = in.readString();
        idType = in.readString();
        idIssueDate = in.readString();
        idExpiryDate = in.readString();
        payOutCurrency = in.readString();
        paymentMode = in.readString();
        paymentBranchCode = in.readString();
        bankName = in.readString();
        bankCountry = in.readString();
        branchName = in.readString();
        bankCode = in.readString();
        accountNumber = in.readString();
        isActive = in.readString();
        purposeCode = in.readString();
        purposeOfTransfer = in.readString();
        payoutAgent = in.readString();
        payoutBranchName = in.readString();
        payOutCountryCode = in.readString();
        payoutCityCode = in.readString();
        landMark = in.readString();
        zipCode = in.readString();
        emailId = in.readString();
        customerRelation = in.readString();
        idtype_Description = in.readString();
        imageURL = in.readString();
        beneficiaryRelationName = in.readString();
    }

    public static final Creator<GetBeneficiaryListResponse> CREATOR = new Creator<GetBeneficiaryListResponse>() {
        @Override
        public GetBeneficiaryListResponse createFromParcel(Parcel in) {
            return new GetBeneficiaryListResponse(in);
        }

        @Override
        public GetBeneficiaryListResponse[] newArray(int size) {
            return new GetBeneficiaryListResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isHoldForOfAC ? 1 : 0));
        dest.writeString(beneficiaryNumber);
        dest.writeString(customerNo);
        dest.writeString(beneficiaryNo);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(nickName);
        dest.writeString(address);
        dest.writeString(dateOfBirth);
        dest.writeString(telephone);
        dest.writeString(nationality);
        dest.writeString(idNumber);
        dest.writeString(idType);
        dest.writeString(idIssueDate);
        dest.writeString(idExpiryDate);
        dest.writeString(payOutCurrency);
        dest.writeString(paymentMode);
        dest.writeString(paymentBranchCode);
        dest.writeString(bankName);
        dest.writeString(bankCountry);
        dest.writeString(branchName);
        dest.writeString(bankCode);
        dest.writeString(accountNumber);
        dest.writeString(isActive);
        dest.writeString(purposeCode);
        dest.writeString(purposeOfTransfer);
        dest.writeString(payoutAgent);
        dest.writeString(payoutBranchName);
        dest.writeString(payOutCountryCode);
        dest.writeString(payoutCityCode);
        dest.writeString(landMark);
        dest.writeString(zipCode);
        dest.writeString(emailId);
        dest.writeString(customerRelation);
        dest.writeString(idtype_Description);
        dest.writeString(imageURL);
        dest.writeString(beneficiaryRelationName);
    }
}
