package moneylink.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import moneylink.wallet.MoneyTransferModuleV.Helpers.CountryTypesHelper;
import moneylink.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import moneylink.wallet.R;
import moneylink.wallet.databinding.ActivityCashPickupSecondBinding;
import moneylink.wallet.di.RequestHelper.BeneficiaryAddRequest;
import moneylink.wallet.di.RequestHelper.GetCashNetworkListRequest;
import moneylink.wallet.di.RequestHelper.GetYBranchRequest;
import moneylink.wallet.di.RequestHelper.GetYCityRequest;
import moneylink.wallet.di.RequestHelper.GetYLocationRequest;
import moneylink.wallet.di.ResponseHelper.AddBeneficiaryResponse;
import moneylink.wallet.di.ResponseHelper.GetCashNetworkListResponse;
import moneylink.wallet.di.ResponseHelper.YBranchResponse;
import moneylink.wallet.di.ResponseHelper.YCityResponse;
import moneylink.wallet.di.ResponseHelper.YLocationResponse;
import moneylink.wallet.di.apicaller.AddBeneficiaryTask;
import moneylink.wallet.di.apicaller.GetCashNetworkListTask;
import moneylink.wallet.di.apicaller.GetYBranchTask;
import moneylink.wallet.di.apicaller.GetYCityTask;
import moneylink.wallet.di.apicaller.GetYLocationTask;
import moneylink.wallet.dialogs.PayOutAgentDialog;
import moneylink.wallet.dialogs.YemenBranchDialog;
import moneylink.wallet.dialogs.YemenCityDialog;
import moneylink.wallet.dialogs.YemenLocationDialog;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.interfaces.OnApiResponse;
import moneylink.wallet.interfaces.OnGetCashNetworkList;
import moneylink.wallet.interfaces.OnGetYBranch;
import moneylink.wallet.interfaces.OnGetYCities;
import moneylink.wallet.interfaces.OnGetYLocation;
import moneylink.wallet.interfaces.OnSelectPayoutAgent;
import moneylink.wallet.utils.IsNetworkConnection;

import java.util.ArrayList;
import java.util.List;


public class CashPickUpBeneficiaryAddressDetailsFragment
        extends BaseFragment<ActivityCashPickupSecondBinding>
        implements OnGetCashNetworkList, OnApiResponse, OnGetYCities
        , OnGetYLocation, OnGetYBranch , OnSelectPayoutAgent {

    BeneficiaryAddRequest addBeneficiaryRequest;
    List<GetCashNetworkListResponse> networkLists;

    List<YCityResponse> cityList;

    @Override

    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.cash_beneficiary));
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        this.cityList = new ArrayList<>();
        addBeneficiaryRequest = ((MoneyTransferMainLayout) getBaseActivity())
                .bankTransferViewModel.beneficiaryAddRequest;
        addBeneficiaryRequest.languageId = getSessionManager().getlanguageselection();

        if (addBeneficiaryRequest.countryID == CountryTypesHelper.YEMEN) {
            binding.yemenLayout.setVisibility(View.VISIBLE);
            binding.localCityId.setVisibility(View.GONE);
            binding.cityName.setVisibility(View.GONE);
            binding.agentsForCashId.setVisibility(View.GONE);
            binding.agentsForCash.setVisibility(View.GONE);
        }

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetCashNetworkListRequest receiptRequest = new GetCashNetworkListRequest();
            receiptRequest.countryCode = addBeneficiaryRequest.PayoutCountryCode;
            receiptRequest.languageId = getSessionManager().getlanguageselection();

            GetCashNetworkListTask task = new GetCashNetworkListTask(getContext(), this);
            task.execute(receiptRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        binding.agentsForCash.setOnClickListener(v -> {
            if (networkLists != null) {
                if (networkLists.size() > 1) {
                    PayOutAgentDialog dialog = new PayOutAgentDialog(networkLists, this);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    dialog.show(transaction  , "");
                }
            }
        });

        binding.nextLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                addBeneficiaryRequest.ipAddress = getSessionManager().getIpAddress();
                addBeneficiaryRequest.ipCountry = getSessionManager().getIpCountryName();
                AddBeneficiaryTask task = new AddBeneficiaryTask(getActivity(), this);
                task.execute(addBeneficiaryRequest);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });

        binding.yemenCity.setOnClickListener(v -> {
            if (cityList.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetYCityRequest request = new GetYCityRequest();
                    request.languageId = getSessionManager().getlanguageselection();

                    GetYCityTask task = new GetYCityTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showYemenCountryDialog(cityList);
            }

        });

        binding.yemenLocation.setOnClickListener(v -> {

            if (addBeneficiaryRequest.cityID != -1) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetYLocationRequest request = new GetYLocationRequest();
                    request.cityID = addBeneficiaryRequest.cityID;
                    request.languageID = getSessionManager().getlanguageselection();

                    GetYLocationTask task = new GetYLocationTask(getContext(),
                            this);
                    task.execute(request);

                } else {
                    onMessage(getString(R.string.loading_txt));
                }
            } else {
                onMessage(getString(R.string.select_city_error));
            }

        });

        binding.yemenBranch.setOnClickListener(v -> {
            if(addBeneficiaryRequest.locationID != -1) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetYBranchRequest request = new GetYBranchRequest();
                    request.locationID = addBeneficiaryRequest.locationID;
                    request.cityID = addBeneficiaryRequest.cityID;
                    request.languageID = getSessionManager().getlanguageselection();

                    GetYBranchTask task = new GetYBranchTask(getContext(), this);
                    task.execute(request);

                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage(getString(R.string.select_location));
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_pickup_second;
    }

    @Override
    public void onGetNetworkList(List<GetCashNetworkListResponse> networkLists) {
        this.networkLists = new ArrayList<>();
        if (networkLists.size() == 1) {
            addBeneficiaryRequest.PaymentMode = networkLists.get(0).paymentMode;
            addBeneficiaryRequest.PayOutBranchCode = networkLists.get(0).payOutBranchCode;
            binding.agentsForCash.setText(networkLists.get(0).payOutAgent);
        }
        this.networkLists.addAll(networkLists);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccessResponse(Object response) {
        if (response instanceof AddBeneficiaryResponse) {
            onMessage(getString(R.string.bene_added_successfully));
            addBeneficiaryRequest.beneficiaryNo = ((AddBeneficiaryResponse) response).beneficiaryNo;
            ((MoneyTransferMainLayout) getBaseActivity())
                    .bankTransferViewModel.fillBeneficiaryDetails(addBeneficiaryRequest);



            ((MoneyTransferMainLayout) getBaseActivity()).navController.popBackStack(R.id.selectedBeneficiaryForBankTransferFragment
             , false);

//
//            ((MoneyTransferMainLayout) getBaseActivity()).navController.
//
//            Bundle bundle = new Bundle();
//            bundle.putInt("transfer_type" , BeneficiarySelector.CASH_TRANSFER);
//            Navigation.findNavController(getView())
//                    .navigate(R.id.action_cashPickupSecondActivity_to_selectedBeneficiaryForBankTransferFragment
//                     , bundle);
        }
    }

    @Override
    public void onError(String message) {
        onMessage(message);
    }

    @Override
    public void onGetCities(List<YCityResponse> citiesList) {
        this.cityList.addAll(citiesList);
        showYemenCountryDialog(this.cityList);
    }

    @Override
    public void onSelectYCity(YCityResponse city) {
        addBeneficiaryRequest.cityID = city.cityId;
        binding.yemenCity.setText(city.cityName);
        hideBranch();
    }

    @Override
    public void onGetYLocations(List<YLocationResponse> yLocations) {
        showYemenLocationDialog(yLocations);
    }

    @Override
    public void onSelectYLocation(YLocationResponse location) {
        addBeneficiaryRequest.locationID = location.locationID;
        binding.yemenLocation.setText(location.locationName);
        showBranch();
    }

    public void hideBranch() {
        binding.yemenBranch.setText("");
        binding.yemenBranchLayout.setVisibility(View.GONE);
    }

    public void showBranch() {
        binding.yemenBranch.setText("");
        binding.yemenBranchLayout.setVisibility(View.VISIBLE);
    }

    public void showYemenCountryDialog(List<YCityResponse> list) {
        YemenCityDialog dialog = new YemenCityDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    public void showYemenLocationDialog(List<YLocationResponse> list) {
        YemenLocationDialog dialog = new YemenLocationDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    public void showYemenBranchDialog(List<YBranchResponse> list) {
        YemenBranchDialog dialog = new YemenBranchDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onGetYBranch(List<YBranchResponse> branchList) {

        if (branchList.size() == 1) {
            Log.e("onGetYBranch: ", String.valueOf(branchList.get(0).branchID) );
            binding.yemenBranch.setText(branchList.get(0).branchName);
            addBeneficiaryRequest.PaymentMode = "cash";
            addBeneficiaryRequest.PayOutBranchCode = String.valueOf(branchList.get(0).branchID);
        } else {
            showYemenBranchDialog(branchList);
        }
    }

    @Override
    public void onSelectBranch(YBranchResponse branch) {
        addBeneficiaryRequest.PaymentMode = "cash";
        addBeneficiaryRequest.PayOutBranchCode = String.valueOf(branch.branchID);
        binding.yemenBranch.setText(branch.branchName);
        Log.e("onGetYBranch: ", String.valueOf(branch.branchID) );
    }

    @Override
    public void onSelectPayoutAgent(GetCashNetworkListResponse response) {
        addBeneficiaryRequest.PaymentMode = response.paymentMode;
        addBeneficiaryRequest.PayOutBranchCode =response.payOutBranchCode;
        binding.agentsForCash.setText(response.payOutAgent);
    }
}
