package moneylink.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.BankDetailsApadterOnTransfer;
import moneylink.wallet.base.BaseDialogFragment;
import moneylink.wallet.databinding.BankTransferAlertDesignBinding;
import moneylink.wallet.di.RequestHelper.GetTotiPayAccDetails;
import moneylink.wallet.di.apicaller.TotiPayBankAccountDetailsTask;
import moneylink.wallet.interfaces.OnDecisionMade;
import moneylink.wallet.interfaces.OnGetBankNameList;

public class BankDepositDialog extends BaseDialogFragment<BankTransferAlertDesignBinding>
        implements OnGetBankNameList {

    BankDetailsApadterOnTransfer adapter;
    String referenceNo;
    OnDecisionMade onDecisionMade;
    List<String> bankDetails;

    public BankDepositDialog(String referenceNo, OnDecisionMade onDecisionMade) {
        this.referenceNo = referenceNo;
        this.onDecisionMade = onDecisionMade;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                getActivity().finish();
            }
        };
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
        return R.layout.bank_transfer_alert_design;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        bankDetails = new ArrayList<>();
        binding.referenceNo.setText("Transaction No: " + referenceNo);
        setupRecyclerView();
        GetTotiPayAccDetails request = new GetTotiPayAccDetails();
        request.countryShortName = getSessionManager().getResidenceCountry();
        request.languageId = getSessionManager().getlanguageselection();


        TotiPayBankAccountDetailsTask task = new TotiPayBankAccountDetailsTask(getActivity(), this);
        task.execute(request);


        binding.yes.setOnClickListener(v -> {
            onDecisionMade.onProceed();
            cancelUpload();
        });
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                BankDetailsApadterOnTransfer(bankDetails);
        binding.bankDetailRecycler.setLayoutManager(mLayoutManager);
        binding.bankDetailRecycler.setAdapter(adapter);
    }

    @Override
    public void onGetBankList(List<String> lists) {
        bankDetails.clear();
        for (String bankDetail : lists) {
            bankDetail = bankDetail.replace(",", "\n\n");
            bankDetails.add(bankDetail.replace(":", "\n"));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
