package moneylink.wallet.login_signup_module.fragments_sign_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import moneylink.wallet.R;
import moneylink.wallet.databinding.CompleteProfileNonb2Binding;
import moneylink.wallet.di.ApiHelper;
import moneylink.wallet.di.HTTPHelper;
import moneylink.wallet.di.RequestHelper.GetCustomerProfileRequest;
import moneylink.wallet.di.ResponseHelper.CustomerProfile;
import moneylink.wallet.di.ResponseHelper.GetCountryListResponse;
import moneylink.wallet.di.SoapActionHelper;
import moneylink.wallet.di.apicaller.GetCustomerProfileTask;
import moneylink.wallet.dialogs.DialogCountry;
import moneylink.wallet.fragments.BaseFragment;
import moneylink.wallet.home_module.NewDashboardActivity;
import moneylink.wallet.interfaces.OnGetCustomerProfile;
import moneylink.wallet.interfaces.OnSelectCountry;
import moneylink.wallet.login_signup_module.MainActivityLoginSignUp;
import moneylink.wallet.login_signup_module.helper.RegisterUserRequest;
import moneylink.wallet.utils.IsNetworkConnection;
import moneylink.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class FragmentSignupCompleteProfile extends BaseFragment<CompleteProfileNonb2Binding>
        implements OnSelectCountry, OnGetCustomerProfile {

    boolean isNationality = false;
    ProgressBar progressBar;

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.countrysignup.getText().toString())) {
            onMessage(getString(R.string.select_country));
            return false;
        } else if (TextUtils.isEmpty(binding.citysignup.getText().toString())) {
            onMessage(getString(R.string.enter_city_name));
            return false;
        } else if (TextUtils.isEmpty(binding.addresssignup.getText().toString())) {
            onMessage(getString(R.string.address_enter));
            return false;
        } else if (TextUtils.isEmpty(binding.nationalitysignup.getText().toString())) {
            onMessage(getString(R.string.select_nationlity));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        progressBar = new ProgressBar();
        binding.createnext2.setOnClickListener(v -> {
            if (isValidate()) {
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.city = binding.citysignup.getText().toString();

                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.address = binding.addresssignup.getText().toString();

                RegisterUserRequest registerUserRequest = ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest;
                registerUserRequest.languageID = getSessionManager().getlanguageselection();
                registerUserRequest.ipAddress = getSessionManager().getIpAddress();
                registerUserRequest.ipCountry = getSessionManager().getIpCountryName();

                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    progressBar.showProgressDialogWithTitle(getContext()
                            , getString(R.string.loading_txt));
                    RegisterUserTask task = new RegisterUserTask();
                    task.execute(registerUserRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }

        });


        binding.countrysignup.setOnClickListener(v -> {
            isNationality = false;
            showCountryDialog();
        });

        binding.nationalitysignup.setOnClickListener(v -> {
            isNationality = true;
            showCountryDialog();
        });
    }


    void showCountryDialog() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            DialogCountry dialogCountry = new DialogCountry(this, isNationality, false);
            FragmentTransaction fm = getParentFragmentManager().beginTransaction();
            dialogCountry.show(fm, "");
        } else {
            onMessage(getString(R.string.no_internet));
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.complete_profile_nonb2;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        if (isNationality) {
            ((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.nationality = country.countryShortName;
            binding.nationalitysignup.setText(country.countryName);
        } else {
            //TODO sending GBR
            ((MainActivityLoginSignUp) getBaseActivity())
                    .viewModel.registerUserRequest.country = country.countryShortName;
            binding.countrysignup.setText(country.countryName);
        }
    }


    public void goToDashBoard(String customerNo) {
        getSessionManager().setCustomerNo(customerNo);
        getSessionManager().setCustomer(((MainActivityLoginSignUp) getBaseActivity())
                .viewModel.registerUserRequest);
        getSessionManager().setDocumentsUploaded(false);


        getSessionManager().putCustomerPhone(
                ((MainActivityLoginSignUp) getBaseActivity()).viewModel.registerUserRequest.phoneNumber);
        getSessionManager().putEmailAddress(((MainActivityLoginSignUp) getBaseActivity()).viewModel.registerUserRequest.email);

        getCustomerProfile();

    }

    @Override
    public void onGetCustomerProfile(CustomerProfile customerProfile) {
        getSessionManager().setCustomerGet(customerProfile);
        getSessionManager().customerImage("");
        ((MainActivityLoginSignUp) getBaseActivity())
                .sessionManager.isKYCApproved(customerProfile.isApprovedKYC);
        progressBar.hideProgressDialogWithTitle();
        Intent intent = new Intent(getActivity(), NewDashboardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onResponseMessage(String message) {
        progressBar.hideProgressDialogWithTitle();
        onMessage(message);
    }


    private void getCustomerProfile() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetCustomerProfileRequest request = new GetCustomerProfileRequest();
            request.mobileNo = ((MainActivityLoginSignUp) getBaseActivity())
                    .sessionManager.getCustomerPhone();
            request.customerNo = ((MainActivityLoginSignUp) getBaseActivity())
                    .sessionManager.getCustomerNo();
            request.emailAddress = ((MainActivityLoginSignUp) getBaseActivity())
                    .sessionManager.getEmail();
            request.languageId = getSessionManager().getlanguageselection();
            GetCustomerProfileTask task = new GetCustomerProfileTask(getContext(), true, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    public class RegisterUserTask extends AsyncTask<RegisterUserRequest, Void, String> {

        //   ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressBar = new ProgressBar();
            //  progressBar.showProgressDialogWithTitle(getContext(), getString(R.string.loading_txt));
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  progressBar.hideProgressDialogWithTitle();
            if (!s.isEmpty()) {
                goToDashBoard(s);
            }
        }

        @Override
        protected String doInBackground(RegisterUserRequest... registerUserRequests) {
            String xml = registerUserRequests[0].getXML();
            Log.e("doInBackground: ", xml);
            String responseString = HTTPHelper.getResponse(registerUserRequests[0]
                            .getXML(),
                    SoapActionHelper.ACTION_CUSTOMER_REGISTRATION
                    , ApiHelper.METHOD_POST);
            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
            // convert to a JSONObject
            JSONObject jsonObject = xmlToJson.toJson();
            String response = "";
            try {
                jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("CustomerRegistrationResponse").getJSONObject("CustomerRegistrationResult");
                String responseCode = jsonObject.getString("ResponseCode");
                String message = jsonObject.getString("Description");
                Log.e("doInBackground: ", jsonObject.toString());
                if (responseCode.equals("101")) {
                    response = jsonObject.getString("CustomerNumber");
                    Log.e("customerno: ", response);
                    onResponseMessage(message);
                } else {
                    onResponseMessage(message);
                    response = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
