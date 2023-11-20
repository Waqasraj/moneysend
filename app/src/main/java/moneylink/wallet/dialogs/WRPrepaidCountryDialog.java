package moneylink.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.PrepaidCountryAdapter;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import moneylink.wallet.di.ResponseHelper.WRCountryListResponse;
import moneylink.wallet.interfaces.OnPrepaidCountry;

public class WRPrepaidCountryDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnPrepaidCountry {


    List<WRCountryListResponse> countryListResponseList;
    OnPrepaidCountry onPrepaidCountry;
    PrepaidCountryAdapter adapter;


    public WRPrepaidCountryDialog(List<WRCountryListResponse> countryListResponseList,
                                  OnPrepaidCountry onPrepaidCountry) {
        this.countryListResponseList = countryListResponseList;
        this.onPrepaidCountry = onPrepaidCountry;
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
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setSearchView();
        setupRecyclerView();

        binding.titleOfPage.setText(getString(R.string.select_country));

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });
        binding.searchView.setVisibility(View.VISIBLE);
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
        countryListResponseList.sort((o1, o2) ->
                o1.countryName.compareToIgnoreCase(o2.countryName));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                PrepaidCountryAdapter(getContext(), countryListResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }


    @Override
    public void onSelectPrepaidCountry(WRCountryListResponse wrCountryListResponse) {
        onPrepaidCountry.onSelectPrepaidCountry(wrCountryListResponse);
        cancelUpload();
    }

    @Override
    public void onPrepaidOperator(PrepaidOperatorResponse prepaidOperatorResponse) {

    }
}
