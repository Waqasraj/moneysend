package moneylink.wallet.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import moneylink.wallet.R;
import moneylink.wallet.base.TootiBaseActivity;
import moneylink.wallet.dialogs.PinVerificationDialog;
import moneylink.wallet.interfaces.OnUserPin;
import moneylink.wallet.utils.Constants;
import moneylink.wallet.utils.SessionManager;

import com.google.android.material.snackbar.Snackbar;

/**
 * program is using in every fragment to implement this functionality
 *
 * @param <T>
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment
 implements OnUserPin  {

    protected T binding;
    private Activity mActivity;
    public Fragment baseFragment;

    public SessionManager getSessionManager() {
        return ((TootiBaseActivity) getBaseActivity()).sessionManager;
    }


    public boolean isYemenClient() {
        return getSessionManager().getCustomerPhone().substring(0 , 3).equalsIgnoreCase("967");
    }

    public void showProgress() {
    }

    public void hideProgress() {
    }

    public boolean isValidate() {
        return true;
    }

    /**
     * method to use inject view and will be called onCreate
     */
    protected abstract void injectView();

    /**
     * method will call it self onActivateCreate
     *
     * @param savedInstanceState
     */
    protected abstract void setUp(Bundle savedInstanceState);

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.mActivity = activity;
            //  activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectView();
        setHasOptionsMenu(false);
        Constants.hideKeyboard(getBaseActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        //getBaseActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        return binding.getRoot();
    }


    public void onMessage(String message) {
        if(message != null) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
            snackbar.setAction(getString(R.string.cancel), v -> snackbar.dismiss());
            snackbar.show();
        }

    }


    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.executePendingBindings();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUp(savedInstanceState);
    }

    public Activity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return binding;
    }


    public interface Callback {
        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }


   public void getPin() {
        PinVerificationDialog dialog = new PinVerificationDialog(this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    @Override
    public void onVerifiedPin() {

    }

    @Override
    public void onResponseMessage(String message) {

    }
}

