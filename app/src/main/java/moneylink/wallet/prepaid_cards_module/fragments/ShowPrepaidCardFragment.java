package moneylink.wallet.prepaid_cards_module.fragments;

import android.os.Bundle;

import android.view.View;

import moneylink.wallet.R;
import moneylink.wallet.databinding.FragmentShowPrepaidCardBinding;
import moneylink.wallet.di.RequestHelper.AddCustomerCardNoRequest;
import moneylink.wallet.di.apicaller.AddCustomerCardTask;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnCustomerCardNo;
import moneylink.wallet.prepaid_cards_module.PrepaidCardsActivity;
import moneylink.wallet.utils.IsNetworkConnection;


public class ShowPrepaidCardFragment extends BaseFragment<FragmentShowPrepaidCardBinding>
        implements OnCustomerCardNo {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {


        if (((PrepaidCardsActivity) getBaseActivity()).virtualCardNo.isEmpty()) {
            createCustomerCard();
        } else {
            onGetCustomerCardNo(((PrepaidCardsActivity) getBaseActivity()).virtualCardNo);
        }

        binding.cardTxt.setText(getSessionManager().getUserName());

        binding.generate.setOnClickListener(v -> {
            generate();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_show_prepaid_card;
    }

    @Override
    public void onCreateCustomerCardNo(String customerCardNo) {
        addDishes(customerCardNo);
        binding.generate.setVisibility(View.GONE);
        binding.cardDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void createCustomerCard() {
        binding.txtCardNo.setText("0000-0000-0000-0000");
        binding.generate.setVisibility(View.VISIBLE);
    }


    void generate() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            AddCustomerCardNoRequest request = new AddCustomerCardNoRequest();
            request.customerNo = getSessionManager().getCustomerNo();
            request.languageID = getSessionManager().getlanguageselection();

            AddCustomerCardTask task = new AddCustomerCardTask(getContext(), this);
            task.execute(request);
        }
    }

    @Override
    public void onGetCustomerCardNo(String customerCardNo) {
        addDishes(customerCardNo);
        binding.generate.setVisibility(View.GONE);
        binding.cardDetails.setVisibility(View.VISIBLE);
       // ((PrepaidCardsActivity) getBaseActivity()).addNewView();
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    public void addDishes(String original) {
        // String original = number;
        int interval = 4;
        char separator = '-';

        StringBuilder sb = new StringBuilder(original);

        for (int i = 0; i < original.length() / interval - 1; i++) {
            sb.insert(((i + 1) * interval) + i, separator);
        }
        binding.cardTxt.setText(getSessionManager().getUserName());
        binding.txtCardNo.setText(sb.toString());
    }
}