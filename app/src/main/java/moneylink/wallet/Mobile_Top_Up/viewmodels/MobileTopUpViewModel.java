package moneylink.wallet.Mobile_Top_Up.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Response;
import moneylink.wallet.R;
import moneylink.wallet.di.JSONdi.AppExecutors;
import moneylink.wallet.di.JSONdi.NetworkResource;

import moneylink.wallet.di.JSONdi.restRequest.AERequest;
import moneylink.wallet.di.JSONdi.restResponse.AEResponse;
import moneylink.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import moneylink.wallet.di.JSONdi.restResponse.paykiiResponse.GetPaymentDetailsResponse;

import moneylink.wallet.di.RequestHelper.WRPayBillRequest;
import moneylink.wallet.di.retrofit.RestApi;
import moneylink.wallet.di.retrofit.RestClient;

public class MobileTopUpViewModel extends ViewModel {

    RestApi restApi = RestClient.getBase();
    AppExecutors appExecutors = new AppExecutors();

    public WRPayBillRequest payBillRequest = new WRPayBillRequest();
    public String countryCode = "AE";
    public String billerID = "";

    public String callingCode = "+971";

    public GetPaymentDetailsResponse getPaymentDetailsResponse = new GetPaymentDetailsResponse();
    public PrepaidPlanResponse prepaidPlanResponses = new PrepaidPlanResponse();

    public LiveData<NetworkResource<AEResponse>> getBillerNamesOnNotFound(AERequest request,
                                                                          String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getPrepaidOperatorsCountryWise(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

    public LiveData<NetworkResource<AEResponse>> getPrepaidCountry(AERequest request,
                                                                   String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getPrepaidCountry(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }


    public LiveData<NetworkResource<AEResponse>> getPrepaidBillerNamesRetry(AERequest request,
                                                                            String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getPrepaidBillerNamesRetry(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

    public LiveData<NetworkResource<AEResponse>> getPrepaidBillerNames(AERequest request,
                                                                       String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getPrepaidBillerNames(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

    public LiveData<NetworkResource<AEResponse>> getPaymentDetails(AERequest request,
                                                                   String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getWAPaymentDetail(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

//    public LiveData<NetworkResource<AEResponse>> getPrepaidPlan(AERequest request, String key, String sKey) {//old one
//        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
//        Call<AEResponse> call = restApi.getPrepaidPlan(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
//            @Override
//            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        AEResponse model = new AEResponse();
//                        model.responseCode = 500;
//                        model.description = message;
//                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (response.isSuccessful()) {
//                    data.postValue(NetworkResource.success(response.body()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AEResponse> call1, Throwable t) {
//                AEResponse temp = new AEResponse();
//                if (t instanceof ConnectException) {
//                    // COMPLETED handle no internet access case
//
//                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
//                } else if (t instanceof IOException) {
//                    // handle server error case
//                    data.postValue(NetworkResource.error(R.string.error_server, temp));
//                } else {
//                    // serialization case, throw abort message (maybe save crash log)
//                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
//                }
//            }
//        }));
//        return data;
//    }
//
//    public LiveData<NetworkResource<AEResponse>> mobileRecharge(AERequest request
//            , String key, String sKey) {
//        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
//        Call<AEResponse> call = restApi.prepaidRecharge(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
//            @Override
//            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        AEResponse model = new AEResponse();
//                        model.responseCode = 500;
//                        model.description = message;
//                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (response.isSuccessful()) {
//                    data.postValue(NetworkResource.success(response.body()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AEResponse> call1, Throwable t) {
//                AEResponse temp = new AEResponse();
//                if (t instanceof ConnectException) {
//                    // COMPLETED handle no internet access case
//
//                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
//                } else if (t instanceof IOException) {
//                    // handle server error case
//                    data.postValue(NetworkResource.error(R.string.error_server, temp));
//                } else {
//                    // serialization case, throw abort message (maybe save crash log)
//                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
//                }
//            }
//        }));
//        return data;
//    }//old one
//
//
//    public LiveData<NetworkResource<AEResponse>> getPlanName(AERequest request, String key
//            , String sKey) {
//        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
//        Call<AEResponse> call = restApi.getPlanName(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
//            @Override
//            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        AEResponse model = new AEResponse();
//                        model.responseCode = 500;
//                        model.description = message;
//                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (response.isSuccessful()) {
//                    data.postValue(NetworkResource.success(response.body()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AEResponse> call1, Throwable t) {
//                AEResponse temp = new AEResponse();
//                if (t instanceof ConnectException) {
//                    // COMPLETED handle no internet access case
//
//                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
//                } else if (t instanceof IOException) {
//                    // handle server error case
//                    data.postValue(NetworkResource.error(R.string.error_server, temp));
//                } else {
//                    // serialization case, throw abort message (maybe save crash log)
//                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
//                }
//            }
//        }));
//        return data;
//    }
//
//
//    public LiveData<NetworkResource<AEResponse>> getRechargePlanName(AERequest request, String key
//            , String sKey) {
//        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
//        Call<AEResponse> call = restApi.getRechargePlanName(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
//            @Override
//            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        AEResponse model = new AEResponse();
//                        model.responseCode = 500;
//                        model.description = message;
//                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (response.isSuccessful()) {
//                    data.postValue(NetworkResource.success(response.body()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AEResponse> call1, Throwable t) {
//                AEResponse temp = new AEResponse();
//                if (t instanceof ConnectException) {
//                    // COMPLETED handle no internet access case
//
//                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
//                } else if (t instanceof IOException) {
//                    // handle server error case
//                    data.postValue(NetworkResource.error(R.string.error_server, temp));
//                } else {
//                    // serialization case, throw abort message (maybe save crash log)
//                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
//                }
//            }
//        }));
//        return data;
//    }
//
//    public LiveData<NetworkResource<AEResponse>> getBillDetails(AERequest request, String key
//            , String sKey) {
//        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
//        Call<AEResponse> call = restApi.getBillDetails(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
//            @Override
//            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        AEResponse model = new AEResponse();
//                        model.responseCode = 500;
//                        model.description = message;
//                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (response.isSuccessful()) {
//                    data.postValue(NetworkResource.success(response.body()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AEResponse> call1, Throwable t) {
//                AEResponse temp = new AEResponse();
//                if (t instanceof ConnectException) {
//                    // COMPLETED handle no internet access case
//
//                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
//                } else if (t instanceof IOException) {
//                    // handle server error case
//                    data.postValue(NetworkResource.error(R.string.error_server, temp));
//                } else {
//                    // serialization case, throw abort message (maybe save crash log)
//                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
//                }
//            }
//        }));
//        return data;
//    }

    public LiveData<NetworkResource<AEResponse>> getPayBill(AERequest request, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getPayBill(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

}
