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
import moneylink.wallet.adapters.CurrencyListAdapter;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.interfaces.OnSelectCurrency;

public class WalletCurrencyListDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectCurrency {


    OnSelectCurrency onSelectCurrency;
    List<GetSendRecCurrencyResponse> currencyResponseList;
    CurrencyListAdapter adapter;

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public WalletCurrencyListDialog(List<GetSendRecCurrencyResponse> currencyResponseList, OnSelectCurrency onSelectCurrency) {
        this.onSelectCurrency = onSelectCurrency;
        this.currencyResponseList = currencyResponseList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_currecny));
        setSearchView();
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {

    }


    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        onSelectCurrency.onCurrencySelect(response);
        cancelUpload();
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
        // purposeList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                CurrencyListAdapter(getContext() , currencyResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
