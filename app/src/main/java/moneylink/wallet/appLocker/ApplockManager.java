package moneylink.wallet.appLocker;

import android.app.Application;
import android.content.Context;

public class ApplockManager {
    private static ApplockManager instance;
    private DefaultApplock currentAppLocker;

    public static ApplockManager getInstance() {
        if (instance == null) {
            instance = new ApplockManager();
        }
        return instance;
    }

    public void enableDefaultAppLockIfAvailable(Application currentApp) {
        currentAppLocker = new DefaultApplock(currentApp);
    }

    public void startWaitThread(Context context){
        currentAppLocker.startWaitThread(context);
    }

    public void updateTouch(){
        currentAppLocker.updateTouch();
    }

    public void setStopTrue(){
        currentAppLocker.setStopTrue();
    }

    public void setStopFalse(){
        currentAppLocker.setStopFalse();
    }
}
