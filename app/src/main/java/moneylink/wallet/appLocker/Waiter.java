package moneylink.wallet.appLocker;

import android.content.Context;
import android.util.Log;

import moneylink.wallet.utils.SessionManager;

public class Waiter extends Thread
{
    private static final String TAG=Waiter.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop = false;
    private Context mContext;
    SessionManager session;

    public Waiter(Context context,long period) {
        this.period=period;
        stop=false;
        mContext = context;
        session = new SessionManager(context.getApplicationContext());
    }

    public void run() {
        long idle=0;
        this.touch();
        do
        {
            idle = System.currentTimeMillis() - lastUsed;
            if(idle > period)
            {
                idle=0;
                // Perform Your desired Function like Logout or expire the session for the app.
                stopThread();
            }
        }
        while(!stop);
        Log.d(TAG, "Finishing Waiter thread");
    }

    public synchronized void touch() {
        lastUsed = System.currentTimeMillis();
    }

    public synchronized void setStopTrue() {
        stop = true;
    }

    public synchronized void setStopFalse() {
        stop = false;
    }

    public synchronized void forceInterrupt() {
        this.interrupt();
    }

    public synchronized void setPeriod(long period)
    {
        this.period=period;
    }

    public synchronized void stopThread() {
        stop = true;
        session.logoutUserInBackgroundOrForeground(mContext);
    }

    public synchronized void startThread() {
        stop = false;
    }

}
