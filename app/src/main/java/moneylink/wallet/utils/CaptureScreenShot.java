package moneylink.wallet.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * @author muhammad waqas
 * @version 1.0
 *
 * program will handle screen shot of views
 */
public class CaptureScreenShot {
    /**
     * method will take view and capture screen shot
     * @param printView
     * @return
     */
    public static Bitmap getScreenShot(View printView) {

        printView.setBackgroundColor(Color.parseColor("#ffffff"));
        Bitmap bitmap = Bitmap.createBitmap(printView.getWidth(),
                printView.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        printView.draw(canvas);

        // compress bitmap
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size(), options);
        return bitmap;
    }
}
