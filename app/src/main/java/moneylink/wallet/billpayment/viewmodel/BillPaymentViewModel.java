package moneylink.wallet.billpayment.viewmodel;

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
import moneylink.wallet.di.retrofit.RestApi;
import moneylink.wallet.di.retrofit.RestClient;

public class BillPaymentViewModel extends ViewModel {

    public String inquiry = "";
    RestApi restApi = RestClient.getBase();

    AppExecutors appExecutors = new AppExecutors();

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


    public LiveData<NetworkResource<AEResponse>> getWABillPaymentDetails(AERequest request,
                                                                         String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getWRBillDetail(RestClient.makeGSONRequestBody(request)
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


    public LiveData<NetworkResource<AEResponse>> getWASKUByBillerName(AERequest request,
                                                                      String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.getWASKUByBillerName(RestClient.makeGSONRequestBody(request)
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


//    public LiveData<NetworkResource<AEResponse>> GetWRBillerType(AERequest request,
//                                                                 String key, String sKey) {
//        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
//        Call<AEResponse> call = restApi.GetWRBillerType(RestClient.makeGSONRequestBody(request)
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

    //CountriList
    public LiveData<NetworkResource<AEResponse>> GetCountryList(AERequest request, String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.GetCountryList(RestClient.makeGSONRequestBody(request)
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


    //BillerCategory
    public LiveData<NetworkResource<AEResponse>> GetWRBillerCategory(AERequest request, String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.GetWRBillerCategory(RestClient.makeGSONRequestBody(request)
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

    //GetWRBillerNames
    public LiveData<NetworkResource<AEResponse>> GetWRBillerNames(AERequest request, String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.GetWRBillerNames(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration

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

    //GetWRBillerFields
    public LiveData<NetworkResource<AEResponse>> GetWRBillerFields(AERequest request, String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.GetWRBillerFields(RestClient.makeGSONRequestBody(request)
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

//
//    //BillPay
//    public LiveData<NetworkResource<BillPayResponse>> getBillPay(BillPayRequest request, String key, String sKey) {
//        MutableLiveData<NetworkResource<BillPayResponse>> data = new MutableLiveData<>(); // receiving
//        Call<BillPayResponse> call = restApi.getBillPay(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<BillPayResponse>() {
//            @Override
//            public void onResponse(Call<BillPayResponse> call1, Response<BillPayResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        BillPayResponse model = new BillPayResponse();
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
//            public void onFailure(Call<BillPayResponse> call1, Throwable t) {
//                BillPayResponse temp = new BillPayResponse();
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
//    public LiveData<NetworkResource<WRCountryListResponse>> getCountryList(Credentials request, String key, String sKey) {
//        MutableLiveData<NetworkResource<WRCountryListResponse>> data = new MutableLiveData<>(); // receiving
//        Call<WRCountryListResponse> call = restApi.getCountryList(RestClient.makeGSONRequestBody(request)
//                , key, sKey); // rest api declaration
//        //init
//        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<WRCountryListResponse>() {
//            @Override
//            public void onResponse(Call<WRCountryListResponse> call1, Response<WRCountryListResponse> response) {
//                if (!response.isSuccessful() && response.errorBody() != null) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.getString("Message");
//
//                        WRCountryListResponse model = new WRCountryListResponse();
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
//            public void onFailure(Call<WRCountryListResponse> call1, Throwable t) {
//                WRCountryListResponse temp = new WRCountryListResponse();
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

//    public LiveData<NetworkResource<AEResponse>> getBillDetails(AERequest request, String key, String sKey) {
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


    public LiveData<NetworkResource<AEResponse>> getPayBill(AERequest request, String key, String sKey) {
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
