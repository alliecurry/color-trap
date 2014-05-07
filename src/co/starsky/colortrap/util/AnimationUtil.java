package co.starsky.colortrap.util;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import co.starsky.colortrap.animator.AnimatorPath;
import co.starsky.colortrap.animator.AnimatorProxy;
import co.starsky.colortrap.animator.PathEvaluator;
import co.starsky.colortrap.animator.PathPoint;

/**
 * @author alliecurry
 */
public final class AnimationUtil {
    public static final int DEFAULT_CURVE_DURATION = 250;
    public static final int DEFAULT_FADE_DURATION = 300;

    public static void fadeOutView(View v) {
        ObjectAnimator animationFadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        animationFadeOut.setDuration(DEFAULT_FADE_DURATION);
        animationFadeOut.start();
    }

    public static void fadeInView(View v) {
        ObjectAnimator animationFadeIn = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        animationFadeIn.setDuration(DEFAULT_FADE_DURATION);
        animationFadeIn.start();
    }

    /** Creates a Bézier curve animation path. */
    public static AnimatorPath getCurvedPath(final float x, final float y,
                                              final float xOffset, final float yOffset,
                                              final float xCurve, final float yCurve) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(x, y);
        path.curveTo(
                x + xCurve, y - yCurve,
                x + xOffset + xCurve, y + yOffset - yCurve,
                x + xOffset, y + yOffset);
        return path;
    }

    /**
     *  Creates and starts an animation along the given {@link co.starsky.colortrap.animator.AnimatorPath}.
     *  @param view {@link android.view.View} to animate.
     *  @param path {@link co.starsky.colortrap.animator.AnimatorPath} containing the coordinates to animate along.
     *  @param duration {@code int} total duration (milliseconds) of the animation.
     *  @param onAnimationComplete {@link java.lang.Runnable} task to be executed once the animation has completed.
     */
    public static void startPathAnimation(final View view, final AnimatorPath path, final int duration,
                                           final Runnable onAnimationComplete) {
        AnimatorProxy.wrap(view);
        final ObjectAnimator anim = ObjectAnimator.ofObject(new ViewLocation(view), ViewLocation.PROPERTY_NAME,
                new PathEvaluator(), path.getPoints().toArray());

//        final int duration = (int) Math.abs((xOffset + yOffset) / 3) + 250;
        anim.setDuration(duration);
        anim.start();
        new Handler().postDelayed(onAnimationComplete, duration);
    }

    /** @return {@code long} duration of a curve animation based on the given offset values.
     *  Ensures that longer curve animations are not wildly faster. */
    public static int getCurveDuration(final float xOffset, final float yOffset) {
        return (int) Math.abs((xOffset + yOffset) / 3) + DEFAULT_CURVE_DURATION;
    }

    /** @return {@code float} curve amount based on the given View size and offset. */
    public static float getCurve(float tileSize, float offset) {
        return offset > -1f && offset < 1f ? (tileSize / 2) + offset : 0;
    }

    /** Class used to set the location of a view. */
    public static class ViewLocation {
        public static final String PROPERTY_NAME = "viewLocation";
        private final View view;

        public ViewLocation(final View v) {
            this.view = v;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setViewLocation(PathPoint newLoc) {
            view.setTranslationX(newLoc.mX);
            view.setTranslationY(newLoc.mY);
        }
    }

}
