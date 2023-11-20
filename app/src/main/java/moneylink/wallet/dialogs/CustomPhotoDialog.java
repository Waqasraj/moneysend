package moneylink.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import moneylink.wallet.R;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.LayoutDialogImageChooseBinding;
import moneylink.wallet.interfaces.OnChooseImageType;


public class CustomPhotoDialog extends BaseDialogFragment<LayoutDialogImageChooseBinding> {

    OnChooseImageType onChooseImageType;

    public CustomPhotoDialog(OnChooseImageType onChooseImageType) {
        this.onChooseImageType = onChooseImageType;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_dialog_image_choose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.tvCamera.setOnClickListener(v -> {
            onChooseImageType.onClickCamera();
            cancelUpload();
        });

        binding.tvGallery.setOnClickListener(v -> {
            onChooseImageType.onClickGallery();
            cancelUpload();
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            dialog.getWindow().setLayout((int) (width * .8), WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

}
