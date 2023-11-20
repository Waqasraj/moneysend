package moneylink.wallet.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import moneylink.wallet.R;
import moneylink.wallet.utils.SessionManager;

import com.google.android.material.snackbar.Snackbar;

/**
 * program is used in every dialog with base
 * @param <T>
 */
public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment {

    private static final String TAG = "BaseDialog";

    protected T binding;
    private TootiBaseActivity mActivity;

    public T getViewDataBinding() {
        return binding;
    }

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public void showProgress(){}
    public void hideProgress(){}

    protected void injectView(){}
    protected abstract void setUp(Bundle savedInstanceState);


    public SessionManager getSessionManager() {
        return ((TootiBaseActivity) getBaseActivity()).sessionManager;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TootiBaseActivity) {
            TootiBaseActivity activity = (TootiBaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectView();
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.executePendingBindings();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUp(savedInstanceState);
    }

    public void onMessage(String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(getString(R.string.cancel), v -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }



    public void cancelUpload() {
        Dialog dialog = getDialog();
        dialog.dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.setCanceledOnTouchOutside(true);
        }
    }


    public TootiBaseActivity getBaseActivity() {
        return mActivity;
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}

