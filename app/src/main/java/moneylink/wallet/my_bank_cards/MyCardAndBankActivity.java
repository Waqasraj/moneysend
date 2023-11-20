package moneylink.wallet.my_bank_cards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import moneylink.wallet.R;
import moneylink.wallet.adapters.CardDetailsAdapter;
import moneylink.wallet.databinding.ActivityMyCardAndBankBinding;
import moneylink.wallet.di.ResponseHelper.GetCardDetailsResponse;
import moneylink.wallet.dialogs.ShowCardDetailsDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.ClassChangerHelper;
import moneylink.wallet.home_module.NewDashboardActivity;
import moneylink.wallet.home_module.fragments.HomeFragment;
import moneylink.wallet.interfaces.OnCardDetailsSubmit;
import moneylink.wallet.interfaces.OnGetCardDetails;

public class MyCardAndBankActivity extends BaseFragment<ActivityMyCardAndBankBinding>
        implements OnCardDetailsSubmit, OnGetCardDetails {


    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;


    @Override
    public void onResume() {
        super.onResume();
        ((NewDashboardActivity) getBaseActivity()).hideHumBurger(ClassChangerHelper.MY_BANK);

    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        binding.addNewCardsTv.setOnClickListener(view -> {
//            SaveCardDialog dialog = new SaveCardDialog(this);
//            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//            dialog.show(transaction, "");
            showCardDialog();
        });

        binding.inculdeLayout.backBtn.setOnClickListener(v -> {
            ((NewDashboardActivity) getBaseActivity())
                    .moveToFragment(new HomeFragment());
        });


        binding.loadCards.setOnClickListener(v -> {
            showCardDialog();
//            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
//                GetCardDetailsRequest request = new GetCardDetailsRequest();
//                request.customerNo = getSessionManager().getCustomerNo();
//                request.languageID = getSessionManager().getlanguageselection();
//
//                GetCardDetailsTask task = new GetCardDetailsTask(getContext(),
//                        this);
//                task.execute(request);
//            } else {
//                onMessage(getString(R.string.no_internet));
//            }
        });
    }


    public void showCardDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Alert")
                .setMessage("Credit Card is not allowed for testing.")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_card_and_bank;
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {

    }


    @Override
    public void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses) {
        this.responseList.clear();
        this.responseList.addAll(cardDetailsResponses);
        detailsAdapter.notifyDataSetChanged();

        if (cardDetailsResponses.size() > 0) {
            binding.loadCards.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectCard(GetCardDetailsResponse cardDetail) {
        ShowCardDetailsDialog dialog = new ShowCardDetailsDialog(cardDetail);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        responseList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        detailsAdapter = new
                CardDetailsAdapter(responseList, this);
        binding.cardDetailsRecyclerView.setLayoutManager(mLayoutManager);
        binding.cardDetailsRecyclerView.setHasFixedSize(true);
        binding.cardDetailsRecyclerView.setAdapter(detailsAdapter);
    }
}