package moneylink.wallet.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SnappyRecyclerView extends RecyclerView {


    public SnappyRecyclerView(Context context) {
        super(context);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        //these four variables identify the views you see on screen.
        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition();
        View firstView = linearLayoutManager.findViewByPosition(firstVisibleView);
        View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);

        //these variables get the distance you need to scroll in order to center your views.
        //my views have variable sizes, so I need to calculate side margins separately.
        //note the subtle difference in how right and left margins are calculated, as well as
        //the resulting scroll distances.
        int leftMargin = (screenWidth - lastView.getWidth()) / 2;
        int rightMargin = (screenWidth - firstView.getWidth()) / 2 + firstView.getWidth();
        int leftEdge = lastView.getLeft();
        int rightEdge = firstView.getRight();
        int scrollDistanceLeft = leftEdge - leftMargin;
        int scrollDistanceRight = rightMargin - rightEdge;

        //if(user swipes to the left)
        if (velocityX > 0) smoothScrollBy(scrollDistanceLeft, 0);
        else smoothScrollBy(-scrollDistanceRight, 0);

        return true;

    }

    @Override
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

//        // If you tap on the phone while the RecyclerView is scrolling it will stop in the middle.
//        // This code fixes this. This code is not strictly necessary but it improves the behaviour.
//
//        if (state == SCROLL_STATE_IDLE) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
//
//            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//
//            // views on the screen
//            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//            View lastView = linearLayoutManager.findViewByPosition(lastVisibleItemPosition);
//            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//            View firstView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition);
//
//            // distance we need to scroll
//            int leftMargin = (screenWidth - lastView.getWidth()) / 2;
//            int rightMargin = (screenWidth - firstView.getWidth()) / 2 + firstView.getWidth();
//            int leftEdge = lastView.getLeft();
//            int rightEdge = firstView.getRight();
//            int scrollDistanceLeft = leftEdge - leftMargin;
//            int scrollDistanceRight = rightMargin - rightEdge;
//
//            if (leftEdge > screenWidth / 2) {
//                smoothScrollBy(-scrollDistanceRight, 0);
//            } else if (rightEdge < screenWidth / 2) {
//                smoothScrollBy(scrollDistanceLeft, 0);
//            }
//        }
    }

}
