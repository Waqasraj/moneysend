package moneylink.wallet;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Timer;
import java.util.TimerTask;

import moneylink.wallet.appLocker.ApplockManager;
import moneylink.wallet.interfaces.SessionOutListener;

public class TotiWallet extends Application implements LifecycleObserver {
    public static TotiWallet myAutoLogoutApp;
    protected SessionOutListener listener;
    private Timer timer;
    private  final long INACTIVE_TIMEOUT = 60000 * 5; // 5 min


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        Log.e("onMoveToForeground: ","foreground" );

    }

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        Log.e("onMoveToBackground: ", "bcakground");
    }


    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onMoveToDestroy() {
        Log.e("onMoveToDestroy: ", "onMoveToDestroy");
    }



    public void touch() {
        ApplockManager.getInstance().updateTouch();
    }

    public void setStopTrue() {
        ApplockManager.getInstance().setStopTrue();
    }

    public void setStopFalse() {
        ApplockManager.getInstance().setStopFalse();
        ApplockManager.getInstance().startWaitThread(TotiWallet.myAutoLogoutApp);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }





    /**
     * method will start timer again when user interact the screen
     */
    public void startUserSession () {
        cancelTimer ();

        timer = new Timer();
        /**
         * if user do not interact the application screen
         * then user will auto logout after time complete
         */
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.doLogout ();
            }
        }, INACTIVE_TIMEOUT);

    }

    /**
     * method will cancel timer
     */
    private void cancelTimer () {
        if (timer !=null) timer.cancel();
    }

    /**
     * method to init listener
     * @param listener
     */
    public void registerSessionListener(SessionOutListener listener){
        this.listener = listener;
    }

    /**
     * method will start session/timer on every interaction
     */
    public void onUserInteracted () {
        startUserSession();
    }
}
