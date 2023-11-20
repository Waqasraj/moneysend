package moneylink.wallet.scanqrcodemodule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import moneylink.wallet.ImagePickerActivity;
import moneylink.wallet.databinding.FragmentBarcodeReaderBinding;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.WalletViaQRCodeActivity;
import moneylink.wallet.scanqrcodemodule.camera.CameraSource;
import moneylink.wallet.scanqrcodemodule.camera.CameraSourcePreview;
import moneylink.wallet.scanqrcodemodule.camera.GraphicOverlay;
import moneylink.wallet.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.List;

public class BarcodeReaderFragment extends BaseFragment<FragmentBarcodeReaderBinding> implements View.OnTouchListener,
        BarcodeGraphicTracker.BarcodeGraphicTrackerListener {

    private static final String TAG = BarcodeReaderFragment.class.getSimpleName();
    private static final String KEY_AUTO_FOCUS = "key_auto_focus";
    private static final String KEY_USE_FLASH = "key_use_flash";
    private static final String KEY_SCAN_OVERLAY_VISIBILITY = "key_scan_overlay_visibility";
    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;
    private static final String BarcodeObject = "Barcode";
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private final int SELECT_PHOTO = 100;
    CameraSource mCameraSource;
    TextView galleryLayout;
    TextView flashText;
    // constants used to pass extra data in the intent
    private boolean autoFocus = false;
    private boolean useFlash = false;
    private boolean isPaused = false;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private BarcodeReaderListener mListener;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int scanOverlayVisibility;
    private int moduleCheck;
    private boolean isFlashOn;

    public BarcodeReaderFragment() {
    }

    public static BarcodeReaderFragment newInstance(boolean autoFocus, boolean useFlash) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_AUTO_FOCUS, autoFocus);
        args.putBoolean(KEY_USE_FLASH, useFlash);
        BarcodeReaderFragment fragment = new BarcodeReaderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static BarcodeReaderFragment newInstance(boolean autoFocus, boolean useFlash, int scanOverlayVisibleStatus) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_AUTO_FOCUS, autoFocus);
        args.putBoolean(KEY_USE_FLASH, useFlash);
        args.putInt(KEY_SCAN_OVERLAY_VISIBILITY, scanOverlayVisibleStatus);
        BarcodeReaderFragment fragment = new BarcodeReaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this listener if you want more callbacks
     *
     * @param barcodeReaderListener listener
     */
    public void setListener(BarcodeReaderListener barcodeReaderListener) {
        mListener = barcodeReaderListener;
    }


    public void pauseScanning() {
        isPaused = true;
    }

    public void resumeScanning() {
        isPaused = false;
    }


    @Override
    protected void injectView() {
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.autoFocus = arguments.getBoolean(KEY_AUTO_FOCUS, true);
            this.useFlash = arguments.getBoolean(KEY_USE_FLASH, false);
            this.scanOverlayVisibility = arguments.getInt(KEY_SCAN_OVERLAY_VISIBILITY, View.VISIBLE);
        }
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", getActivity().MODE_PRIVATE);

        mPreview = binding.preview;
        //view.findViewById(R.id.preview);
        mGraphicOverlay = binding.graphicOverlay;
        //   view.findViewById(R.id.graphicOverlay);

        galleryLayout = binding.galleryLayout;
        //   view.findViewById(R.id.gallery_layout);
        flashText = binding.flashText;
        //   view.findViewById(R.id.flash_text);
        ScannerOverlay mScanOverlay = binding.scanOverlay;
        //  view.findViewById(R.id.scan_overlay);
        mScanOverlay.setVisibility(scanOverlayVisibility);

        gestureDetector = new GestureDetector(getActivity(), new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());
        //  view.setOnTouchListener(this);

        isFlashOn = false;

        flashText.setOnClickListener(v -> onFlashClick());
        galleryLayout.setOnClickListener(v -> onClickGallery());

        permissionStatus = getActivity().getSharedPreferences("permissionStatus", getActivity().MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, (dialog, which) -> {
                    dialog.cancel();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                    mListener.onCameraPermissionDenied();
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.CAMERA, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, (dialog, which) -> {
                    dialog.cancel();
                    sentToSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                    mListener.onCameraPermissionDenied();
                });
                builder.show();
            } else {
                //just request the permission
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CAMERA, true);
            editor.apply();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_barcode_reader;
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarcodeReaderFragment);
        autoFocus = a.getBoolean(R.styleable.BarcodeReaderFragment_auto_focus, true);
        useFlash = a.getBoolean(R.styleable.BarcodeReaderFragment_use_flash, false);
        a.recycle();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BarcodeReaderListener) {
            mListener = (BarcodeReaderListener) context;
        }
    }

    private void proceedAfterPermission() {
        createCameraSource(autoFocus, useFlash);
    }


    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     * <p>
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(final boolean autoFocus, final boolean useFlash) {
        Log.e(TAG, "createCameraSource:");
        Context context = getActivity();

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = getActivity().registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(getActivity(), R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small bar codes
        // at long distances.

        CameraSource.Builder builder = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(1.0f);

        // make sure that auto focus is an available option
        builder = builder.setFocusMode(
                autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();

        setAutoFocus(true);
    }

    /**
     * Trigger flash torch on and off, perhaps using a compound button.
     */
    public void setUseFlash(boolean use) {
        useFlash = use;
        mCameraSource.setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
    }

    /**
     * Trigger auto focus mode, perhaps using a compound button.
     */
    public void setAutoFocus(boolean continuous) {
        autoFocus = continuous;
        mCameraSource.setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : Camera.Parameters.FOCUS_MODE_AUTO);
    }

    /**
     * Returns true if device supports flash.
     */
    public boolean deviceSupportsFlash() {
        if (getActivity().getPackageManager() == null)
            return false;
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        ((WalletViaQRCodeActivity) getActivity())
                .changeTitle(getString(R.string.scan_code));
        startCameraSource();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            } else {
                mListener.onCameraPermissionDenied();
            }
        }

        setAutoFocus(true);
    }

    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPreview != null) {
            mPreview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, (dialog, which) -> {
                    dialog.cancel();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                    mListener.onCameraPermissionDenied();
                });
                builder.show();
            } else {
                mListener.onCameraPermissionDenied();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PERMISSION_SETTING) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    proceedAfterPermission();
                }
            } else if (requestCode == 1) {
                Uri selectedImage = data.getData();
                mListener.onBitmapGallery(selectedImage);
            } else if (requestCode == SELECT_PHOTO) {

                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseActivity().getContentResolver(), uri);
                    getCode(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
              //  Uri selectedImage = data.getData();
               // performCrop(selectedImage);
            } else if (requestCode == 2) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    getCode(selectedBitmap);
                }
            }
        }


    }


    private void getCode(Bitmap bMap) {
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        // ChecksumException
        try {
            Result result = reader.decode(bitmap);
            //for easy manipulation of the result
            String barcode = result.getText();
            if (barcode != null) {
                parseBarCode(barcode);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.scan_result));
                //  builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(getString(R.string.nothing_found_try));
                AlertDialog alert1 = builder.create();
                alert1.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.done), (dialog, which) -> {
                    //     Intent i = new Intent(getContext(), MainActivity.class);
                    //   startActivity(i);
                });

                alert1.setCanceledOnTouchOutside(false);
                alert1.show();
            }
            //the end of do something with the button statement.
        } catch (NotFoundException | ChecksumException | NullPointerException e) {
            Toast.makeText(getContext(), getString(R.string.nothing_found_text), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigateUp();
        } catch (FormatException e) {
            Toast.makeText(getContext(), getString(R.string.wrong_qr_code), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigateUp();
        }
    }




    private void launchGalleryIntent() {
        Intent intent = new Intent(getBaseActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 100);
    }


    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = getString(R.string.device_not_support_crop);
            Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getActivity());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }

        setAutoFocus(true);
    }

    /**
     * onTap returns the tapped barcode result to the calling Activity.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    boolean onTap(float rawX, float rawY) {
        // Find tap point in preview frame coordinates.
        int[] location = new int[2];
        mGraphicOverlay.getLocationOnScreen(location);
        float x = (rawX - location[0]) / mGraphicOverlay.getWidthScaleFactor();
        float y = (rawY - location[1]) / mGraphicOverlay.getHeightScaleFactor();

        // Find the barcode whose center is closest to the tapped point.
        Barcode best = null;
        float bestDistance = Float.MAX_VALUE;
        for (BarcodeGraphic graphic : mGraphicOverlay.getGraphics()) {
            Barcode barcode = graphic.getBarcode();
            if (barcode.getBoundingBox().contains((int) x, (int) y)) {
                // Exact hit, no need to keep looking.
                best = barcode;
                break;
            }
            float dx = x - barcode.getBoundingBox().centerX();
            float dy = y - barcode.getBoundingBox().centerY();
            float distance = (dx * dx) + (dy * dy);  // actually squared distance
            if (distance < bestDistance) {
                best = barcode;
                bestDistance = distance;
            }
        }

        if (best != null) {
            Intent data = new Intent();
            data.putExtra(BarcodeObject, best);

            getActivity().setResult(CommonStatusCodes.SUCCESS, data);
            getActivity().finish();
            return true;
        }
        return false;
    }


    public void onClickGallery() {
//        Intent photoPic = new Intent(Intent.ACTION_PICK);
//        photoPic.setType("image/*");
//        startActivityForResult(photoPic, SELECT_PHOTO);


        launchGalleryIntent();

    }


    public void onFlashClick() {
        if (!isFlashOn) {
            if (deviceSupportsFlash()) {
                isFlashOn = true;
                setUseFlash(isFlashOn);
                flashText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flash_off, 0, 0);
                flashText.setText(getResources().getString(R.string.off_text));
            } else {
                Toast.makeText(getContext(), getString(R.string.flash_not), Toast.LENGTH_SHORT).show();
            }
        } else {
            isFlashOn = false;
            setUseFlash(isFlashOn);
            flashText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flash_on, 0, 0);
            flashText.setText(getResources().getString(R.string.on_text));
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean b = scaleGestureDetector.onTouchEvent(motionEvent);

        boolean c = gestureDetector.onTouchEvent(motionEvent);

        return b || c || view.onTouchEvent(motionEvent);
    }

    @Override
    public void onScanned(final Barcode barcode) {
        parseBarCode(barcode.displayValue);
//        if (mListener != null && !isPaused) {
//            if (getActivity() == null) {
//                return;
//            }
//            getActivity().runOnUiThread(() -> {
//              //  Toast.makeText(getContext(), barcode.rawValue, Toast.LENGTH_SHORT).show();
//                mListener.onScanned(barcode);
//            });
//        }
    }

    @Override
    public void onScannedMultiple(final List<Barcode> barcodes) {
        Log.e(TAG, "fragment scanned multiple: ");
        if (mListener != null && !isPaused) {
            if (getActivity() == null) {
                return;
            }
            //   getActivity().runOnUiThread(() -> mListener.onScannedMultiple(barcodes));
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), getString(R.string.scan_correct_qr), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onBitmapScanned(final SparseArray<Barcode> sparseArray) {
        if (mListener != null) {
            if (getActivity() == null) {
                return;
            }
            //  getActivity().runOnUiThread(() -> mListener.onBitmapScanned(sparseArray));
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), getString(R.string.scan_correct_qr), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onScanError(final String errorMessage) {
        if (mListener != null) {
            if (getActivity() == null) {
                return;
            }
            //   getActivity().runOnUiThread(() -> mListener.onScanError(errorMessage));
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), getString(R.string.scan_correct_qr), Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * method will parse the bar code and change the screen
     * after check which type of module called the barcode fragment
     *
     * @param barcode
     */
    private void parseBarCode(String barcode) {
        Bundle bundle = new Bundle();
        bundle.putString("customer_no", barcode);
        Navigation.findNavController(getView())
                .navigate(R.id.action_barcodeReaderFragment_to_walletToWalletViaQrCode, bundle);
    }


    void showMessage() {
        getActivity().runOnUiThread(() ->
                Toast.makeText(getContext(), getString(R.string.scan_correct_qr), Toast.LENGTH_SHORT).show());
    }

    public String[] getSplit(String barCode) {
        String[] details = null;
        try {
            details = barCode.split(",");
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.e("error", ex.getLocalizedMessage());
            Log.e(TAG, "getSplit: " + ex.getMessage());
            return details;
        }
        return details;
    }


    public interface BarcodeReaderListener {
        void onScanned(Barcode barcode);

        void onScannedMultiple(List<Barcode> barcodes);

        void onBitmapScanned(SparseArray<Barcode> sparseArray);

        void onBitmapGallery(Uri results);

        void onScanError(String errorMessage);

        void onCameraPermissionDenied();
    }

    class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }

}
