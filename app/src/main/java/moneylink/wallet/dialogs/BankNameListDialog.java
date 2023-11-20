package moneylink.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.BankNamesAdapter;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.TransferDialogPurposeBinding;
import moneylink.wallet.di.RequestHelper.BankNameListRequest;
import moneylink.wallet.di.ResponseHelper.GetBankNetworkListResponse;
import moneylink.wallet.di.apicaller.BankNameListTask;
import moneylink.wallet.interfaces.OnGetBankNameList;
import moneylink.wallet.interfaces.OnSelectBank;
import moneylink.wallet.utils.IsNetworkConnection;

public class BankNameListDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnGetBankNameList, OnSelectBank {

    BankNamesAdapter adapter;
    List<String> bankList;
    OnSelectBank onSelectBank;
    String countryCode = "";

    public BankNameListDialog(String countryCode, OnSelectBank onSelectBank) {
        this.countryCode = countryCode;
        this.onSelectBank = onSelectBank;
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
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            BankNameListRequest request = new BankNameListRequest();
            request.shortCountryName = countryCode;
            request.languageId = getSessionManager().getlanguageselection();
            BankNameListTask task = new BankNameListTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
            cancelUpload();
        }


        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });
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
        Collections.sort(bankList, (o1, o2) ->
                o1.compareToIgnoreCase(o2));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                BankNamesAdapter(bankList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onGetBankList(List<String> lists) {
        bankList = new ArrayList<>();
        bankList.addAll(lists);
        setupRecyclerView();
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectBank(GetBankNetworkListResponse bankDetails) {

    }

    @Override
    public void onSelectBankName(String bankName) {
        onSelectBank.onSelectBankName(bankName);
        cancelUpload();
    }

    @Override
    public void onSelectBranch(GetBankNetworkListResponse branchName) {

    }
}
