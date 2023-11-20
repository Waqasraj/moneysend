package moneylink.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import moneylink.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivityCashPickupFirstBinding;
import moneylink.wallet.di.RequestHelper.BeneficiaryAddRequest;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.ResponseHelper.GetRelationListResponse;
import moneylink.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.dialogs.DialogCurrency;
import moneylink.wallet.dialogs.DialogRelationList;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.interfaces.OnSelectCurrency;
import moneylink.wallet.interfaces.OnSelectRelation;
import moneylink.wallet.utils.CheckValidation;
import moneylink.wallet.utils.IsNetworkConnection;

import java.util.List;

public class CashPickUpBeneficiaryPersonalDetailsFragment
        extends BaseFragment<ActivityCashPickupFirstBinding>
        implements OnSelectCountry, OnSelectRelation, OnSelectCurrency {


    boolean isCashTransfer = false;
    BeneficiaryAddRequest request;
    boolean isCountryCode = false;

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.cash_beneficiary));


        if (((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryAddRequest != null) {

            setReceivingCurrencyImage(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryAddRequest.countryFlag);
            binding.countryCodeTextView.setText(((MoneyTransferMainLayout) getBaseActivity())
                    .bankTransferViewModel.beneficiaryAddRequest.countryCode);
            binding.relation.setText(((MoneyTransferMainLayout) getBaseActivity())
                    .bankTransferViewModel.beneficiaryAddRequest.CustomerRelation);
            binding.country.setText(((MoneyTransferMainLayout) getBaseActivity())
                    .bankTransferViewModel.beneficiaryAddRequest.countryName);
        }
    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.firstName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__first_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.lastName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__last_name_error));
            return false;
        }
        if (TextUtils.isEmpty(binding.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_code));
            return false;
        } else if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_number_error));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.mobilesignupb.getText().toString()
                , binding.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (TextUtils.isEmpty(binding.country.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_error));
            return false;
        } else if (TextUtils.isEmpty(binding.address.getText().toString())) {
            onMessage(getString(R.string.address_enter));
            return false;
        } else if (TextUtils.isEmpty(binding.relation.getText().toString())) {
            onMessage(getString(R.string.plz_select_relation));
            return false;
        }
//        else if (CheckValidation.isValidName(
//                binding.firstName.getText().toString())) {
//            onMessage(getString(R.string.first_name_special_character_not_allowed));
//            return false;
//        } else if (CheckValidation.isValidName(
//                binding.middleName.getText().toString())) {
//            onMessage(getString(R.string.middle_name_spe_char));
//            return false;
//        } else if ((CheckValidation.isValidName(binding.lastName.getText().toString()))) {
//            onMessage(getString(R.string.last_name_spe_char));
//            return false;
//        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        request = new BeneficiaryAddRequest();
        if (getArguments() != null) {
            isCashTransfer = getArguments().getBoolean("is_cash_transfer", false);
        }

        binding.country.setOnClickListener(v -> {
            isCountryCode = false;
            DialogCountry dialogCountry = new DialogCountry(this, false, false);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCountry.show(transaction, "");
        });

        binding.relation.setOnClickListener(v -> {
            DialogRelationList dialogRelationList = new DialogRelationList(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogRelationList.show(transaction, "");
        });


        binding.countrySpinnerSignIn.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                isCountryCode = true;
                DialogCountry country = new DialogCountry(this::onSelectCountry);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                country.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });

        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                request.FirstName = binding.firstName.getText().toString();
                request.LastName = binding.lastName.getText().toString();
                request.MiddleName = "--";
                request.customerNo = getSessionManager().getCustomerNo();
                request.Telephone = binding.countryCodeTextView.getText().toString() + binding.mobilesignupb.getText().toString();


                request.Address = binding.address.getText().toString();
                request.languageId = getSessionManager().getlanguageselection();
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest = request;
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_cashPickupFirstActivity_to_cashPickupSecondActivity);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_pickup_first;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {

        if (!isCountryCode) {
            binding.country.setText(country.countryName);
            request.countryName = country.countryName;
            request.PayoutCountryCode = country.countryShortName;
            request.BankCountry = country.countryShortName;
            request.languageId = getSessionManager().getlanguageselection();
            request.countryID = country.id;
            request.PayOutCurrency = country.currencyShortName;
        } else {
            setReceivingCurrencyImage(country.imageURL);
            binding.countryCodeTextView.setText(country.countryCode);
            request.countryCode = country.countryCode;
            request.countryFlag = country.imageURL;
        }


    }


    public void setReceivingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.imageIcon.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onSelectRelation(GetRelationListResponse relation) {
        binding.relation.setText(relation.relationName);
        request.CustomerRelation = relation.relationName;
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            request.PayOutCurrency = response.get(0).currencyShortName;
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }
    }

    /**
     * if currencies are more than 1
     *
     * @param response
     */
    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        request.PayOutCurrency = response.currencyShortName;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

}
