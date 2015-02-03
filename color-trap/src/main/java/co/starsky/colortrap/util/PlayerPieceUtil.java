package co.starsky.colortrap.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import co.starsky.colortrap.R;
import co.starsky.colortrap.model.Mode;

import java.io.IOException;
import java.io.InputStream;

/**
 * Used to update the player piece images.
 */
public final class PlayerPieceUtil {

    public static void setupImages(ImageView player1, ImageView player2, Mode mode) {
        player1.setImageResource(R.drawable.piece1);
        switch (mode) {
            case HOTSEAT:
                player2.setImageResource(R.drawable.piece2);
                break;
            case COMPUTER:
                player2.setImageResource(R.drawable.piece3);
                break;
        }
    }

    public static void showWinner(ImageView player1, ImageView player2, Mode mode, boolean isPlayer1Winner) {
        player1.setImageResource(isPlayer1Winner ? R.drawable.piece1_win : R.drawable.piece1_lose);
        switch (mode) {
            case HOTSEAT:
                player2.setImageResource(isPlayer1Winner ? R.drawable.piece2_lose : R.drawable.piece2_win);
                break;
            case COMPUTER:
                player2.setImageResource(isPlayer1Winner ? R.drawable.piece3_lose : R.drawable.piece3);
                break;
        }
    }

    public static Bitmap openRawResource(Context context, int resource) {
        InputStream is = context.getResources().openRawResource(resource);
        final Bitmap imageBitmap = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
        }
        return imageBitmap;
    }
}
