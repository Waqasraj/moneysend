package moneylink.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;

import moneylink.wallet.R;
import moneylink.wallet.di.RequestHelper.GetWRPrepaidOperatorRequest;
import moneylink.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import moneylink.wallet.interfaces.OnGetPrepaidOperator;
import moneylink.wallet.utils.ProgressBar;

public class PrepaidOperatorTask extends
        AsyncTask<GetWRPrepaidOperatorRequest, Void, PrepaidOperatorResponse> {


    ProgressBar progressBar;
    Context context;
    OnGetPrepaidOperator onGetPrepaidOperator;


    public PrepaidOperatorTask(Context context, OnGetPrepaidOperator onGetPrepaidOperator) {
        this.context = context;
        this.onGetPrepaidOperator = onGetPrepaidOperator;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(PrepaidOperatorResponse prepaidOperatorResponse) {
        super.onPostExecute(prepaidOperatorResponse);
        progressBar.hideProgressDialogWithTitle();
        if (prepaidOperatorResponse != null) {
            onGetPrepaidOperator.onGetPrepaidOperator(prepaidOperatorResponse);
        }
    }

    @Override
    protected PrepaidOperatorResponse doInBackground(GetWRPrepaidOperatorRequest... getWRPrepaidOperatorRequests) {

        return null;
    }

}
