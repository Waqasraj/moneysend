package moneylink.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.R;

import moneylink.wallet.adapters.YemenLocationsAdapter;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;

import moneylink.wallet.di.ResponseHelper.YLocationResponse;

import moneylink.wallet.interfaces.OnGetYLocation;

public class YemenLocationDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnGetYLocation {

    List<YLocationResponse> locationList;
    YemenLocationsAdapter adapter;
    OnGetYLocation onLocations;

    public YemenLocationDialog(List<YLocationResponse> locationList, OnGetYLocation onGetYCities) {
        this.locationList = locationList;
        this.onLocations = onGetYCities;
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setSearchView();
        setupRecyclerView();
        binding.titleOfPage.setText(getString(R.string.select_location));
        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }


    /**
     * method will init the search box
     */
    private void setSearchView() {
        binding.searchView.setMaxWidth(Integer.MAX_VALUE);
        binding.searchView.requestFocus();
        binding.searchView.setFocusable(true);
        binding.searchView.setHint(getString(R.string.search));


        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                YemenLocationsAdapter(getContext(), locationList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setAdapter(adapter);
    }


    @Override
    public void onGetYLocations(List<YLocationResponse> yLocations) {

    }

    @Override
    public void onSelectYLocation(YLocationResponse location) {
        onLocations.onSelectYLocation(location);
        cancelUpload();
    }

    @Override
    public void onResponseMessage(String message) {

    }
}
