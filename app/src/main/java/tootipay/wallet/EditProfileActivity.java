package tootipay.wallet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityEditProfileBinding;
import tootipay.wallet.di.RequestHelper.EditCustomerProfileRequest;
import tootipay.wallet.di.RequestHelper.GetCustomerProfileRequest;
import tootipay.wallet.di.ResponseHelper.CustomerProfile;
import tootipay.wallet.di.apicaller.EditCustomerProfileTask;
import tootipay.wallet.di.apicaller.GetCustomerProfileTask;
import tootipay.wallet.di.restRequest.UploadUserImageRequest;
import tootipay.wallet.di.restResponse.ResponseApi;
import tootipay.wallet.di.retrofit.APIError;
import tootipay.wallet.di.retrofit.RestApi;
import tootipay.wallet.di.retrofit.RestClient;
import tootipay.wallet.dialogs.CustomPhotoDialog;
import tootipay.wallet.interfaces.OnChooseImageType;
import tootipay.wallet.interfaces.OnGetCustomerProfile;
import tootipay.wallet.interfaces.OnResponse;
import tootipay.wallet.utils.BitmapHelper;
import tootipay.wallet.utils.CheckPermission;
import tootipay.wallet.utils.FileCompressor;
import tootipay.wallet.utils.IsNetworkConnection;
import tootipay.wallet.utils.ProgressBar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditProfileActivity extends TootiBaseActivity<ActivityEditProfileBinding>
        implements OnGetCustomerProfile, OnResponse, OnChooseImageType {

    CustomerProfile customerProfile;
    EditCustomerProfileRequest editCustomerProfileRequest;
    Bitmap thumbnail;
    FileCompressor mCompressor;
    File mPhotoFile;
    private final int PERMISSION_REQUEST_CODE = 200;
    private final int CAMERA_CODE = 100;
    private final int GALLERY_CODE = 101;

    private boolean isGallerySelected = false;
    public boolean isUploaded = false;
    UploadUserImageRequest uploadImage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        disableFields();
        uploadImage = new UploadUserImageRequest();
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
        getCustomerProfile();
        binding.toolBar.titleTxt.setText(getString(R.string.edit_profile_txt));
        binding.toolBar.titleTxt.setTextColor(getResources().getColor(R.color.black));


        binding.updateProfile.setOnClickListener(v -> {
//            editCustomerProfileRequest.customer = ((KYCMainActivity) getBaseActivity())
//                    .sessionManager.getCustomerDetails();
            editCustomerProfileRequest = new EditCustomerProfileRequest();
            editCustomerProfileRequest.customer.firstName = customerProfile.firstName;
            editCustomerProfileRequest.customer.lastName = customerProfile.lastName;
            editCustomerProfileRequest.customer.middleName = customerProfile.middleName;
            editCustomerProfileRequest.customer.nationality = customerProfile.nationality;
            editCustomerProfileRequest.customer.email = customerProfile.email;
            editCustomerProfileRequest.customer.phoneNumber = customerProfile.mobileNo;
            editCustomerProfileRequest.customer.gender = customerProfile.gender;
            editCustomerProfileRequest.idType = Integer.parseInt(customerProfile.idType);
            editCustomerProfileRequest.customer.dob = customerProfile.dateOfBirth;
            editCustomerProfileRequest.idExpireDate = customerProfile.idExpireDate;
            editCustomerProfileRequest.idIssueDate = customerProfile.idIssueDate;
            editCustomerProfileRequest.idNumber = customerProfile.idNumber;
            editCustomerProfileRequest.customerNo = sessionManager.getCustomerNo();
            editCustomerProfileRequest.residenceCountry = customerProfile.residenceCountry;
            editCustomerProfileRequest.customer.address = binding.addressEditTextProfile.getText().toString();
            editCustomerProfileRequest.languageId = sessionManager.getlanguageselection();
            if (IsNetworkConnection.checkNetworkConnection(this)) {
                EditCustomerProfileTask task = new EditCustomerProfileTask(this, this);
                task.execute(editCustomerProfileRequest);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.profilePictureImageView.setOnClickListener(v -> {
            openPhotoSelectDialog();
        });


        Glide.with(this)
                .load(BitmapHelper.decodeImage(sessionManager.getCustomerImage()))
                .placeholder(getResources().getDrawable(R.drawable.user_profile_home))
                .into(binding.profilePictureImageView);
    }

    private void getCustomerProfile() {
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            GetCustomerProfileRequest request = new GetCustomerProfileRequest();
            request.mobileNo = sessionManager.getCustomerPhone();
            request.customerNo = sessionManager.getCustomerNo();
            request.emailAddress = sessionManager.getEmail();
            GetCustomerProfileTask task = new GetCustomerProfileTask(this, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    public void disableFields() {
        binding.firstNameEditTextProfile.setEnabled(false);
        binding.lastNameEditTextProfile.setEnabled(false);
        binding.mobileNumberEditTextProfile.setEnabled(false);
        binding.emailEditTextProfile.setEnabled(false);
        binding.maleRadioButtonProfile.setEnabled(false);
        binding.femaleRadioButtonProfile.setEnabled(false);
        binding.dateOfBirthEditTextProfile.setEnabled(false);
        binding.countryOfResidenceEditTextProfile.setEnabled(false);
    }

    @Override
    public void onGetCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
        binding.firstNameEditTextProfile.setText(customerProfile.firstName);
        binding.lastNameEditTextProfile.setText(customerProfile.lastName);
        binding.mobileNumberEditTextProfile.setText(customerProfile.mobileNo);
        binding.emailEditTextProfile.setText(customerProfile.email);
        if (customerProfile.gender.equalsIgnoreCase("m")) {
            binding.maleRadioButtonProfile.setChecked(true);
            binding.femaleRadioButtonProfile.setChecked(false);
        } else {
            binding.femaleRadioButtonProfile.setChecked(true);
            binding.maleRadioButtonProfile.setChecked(false);
        }
        binding.dateOfBirthEditTextProfile.setText(customerProfile.dateOfBirth);
        binding.addressEditTextProfile.setText(customerProfile.address);
        binding.countryOfResidenceEditTextProfile.setText(customerProfile.residenceCountry);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccess() {
        onMessage(getString(R.string.profile_updated));
        finish();
    }


    //gallery and camera manage code
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        mCompressor = new FileCompressor(this);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.select_image_text)),
                    GALLERY_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.install_manager),
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void openPhotoSelectDialog() {
        //Do the stuff that requires permission...
        CustomPhotoDialog dialog = new CustomPhotoDialog(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (storageAccepted && cameraAccepted) {
                    if (isGallerySelected) {
                        //gallery
                    } else {
                        dispatchTakePictureIntent();
                    }
                } else {

                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            isUploaded = true;
                            uploadImage.Image = BitmapHelper.encodeImage(thumbnail);
                            uploadImage.Image_Name = "User_IMAGE.jpg";
                            uploadPicture();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }


                    }

                }
            } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    // uploadPicture(mPhotoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    thumbnail = mCompressor.compressToBitmap(mPhotoFile);
                    isUploaded = true;
                    uploadImage.Image = BitmapHelper.encodeImage(thumbnail);
                    uploadImage.Image_Name = "User_IMAGE.jpg";
                    uploadPicture();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, getString(R.string.error_getting_image), Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    ProgressBar progressBar;

    public void uploadPicture() {
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(this, getString(R.string.loading_txt));


        uploadImage.Customer_No = sessionManager.getCustomerNo();
        uploadImage.credentials.LanguageID = Integer.parseInt(sessionManager.getlanguageselection());
        RestApi restApi = RestClient.getClient().create(RestApi.class);
        Call<ResponseApi> call = restApi.uploadCustomerImage(uploadImage);

        call.enqueue(new retrofit2.Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                progressBar.hideProgressDialogWithTitle();
                if (response.isSuccessful()) {
                    //    assert response.body() != null;
                    if (response.body().status == 101) {
                        binding.profilePictureImageView.setImageBitmap(thumbnail);
                        sessionManager.customerImage(uploadImage.Image);
                    } else {
                        onResponseMessage(response.body().description);
                    }
                } else if (!response.isSuccessful() && response.errorBody() != null){

                    APIError message = new Gson().fromJson(response.errorBody()
                            .charStream(), APIError.class);
                    if(message != null) {
                        Toast.makeText(EditProfileActivity.this, "" +
                                message.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        onMessage("Something went wrong");
                    }

                } else {
                    onMessage("Something went wrong");
                }

            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                progressBar.hideProgressDialogWithTitle();
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });


    }

    @Override
    public void onClickCamera() {
        isGallerySelected = false;
        if (CheckPermission.checkCameraPermission(this)) {
            //permission granted already
            dispatchTakePictureIntent();
        } else {
            requestPermission();
        }
    }

    @Override
    public void onClickGallery() {
        isGallerySelected = true;
        showFileChooser();
    }
}