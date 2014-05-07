package co.starsky.colortrap.view;

import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author alliecurry
 */
public class RotateTouchListener implements View.OnTouchListener {
    private static final int YOFFSET = 50;

    private ObjectAnimator animatorLeft;
    private ObjectAnimator animatorRight;
    private ObjectAnimator animatorLeftReset;
    private ObjectAnimator animatorRightReset;
    private boolean isAnimatedLeft = false;
    private boolean isAnimatedRight = false;

    public RotateTouchListener(ObjectAnimator animationLeft, ObjectAnimator animationRight, int duration) {
        this.animatorLeft = animationLeft;
        this.animatorRight = animationRight;
        animatorLeft.setDuration(duration);
        animatorRight.setDuration(duration);
    }

    public void setResetAnimators(ObjectAnimator resetLeft, ObjectAnimator resetRight) {
        this.animatorLeftReset = resetLeft;
        this.animatorRightReset = resetRight;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int difference = (int) (view.getPivotY() - motionEvent.getX());

                if (!isAnimatedRight && difference < -YOFFSET) {
                    startAnimation(animatorRight, view, false);

                } else if (!isAnimatedLeft && difference > YOFFSET) {
                    startAnimation(animatorLeft, view, true);
                } else if (difference < YOFFSET && difference > -YOFFSET) {
                    resetView(view);
                }
                break;
            case MotionEvent.ACTION_UP:
                resetView(view);
                break;
            default: break;
        }

        return false;
    }

    private void startAnimation(ObjectAnimator animator, View v, boolean isLeft) {
        isAnimatedLeft = isLeft;
        isAnimatedRight = !isLeft;
        animator.setTarget(v);
        animator.start();
    }

    private void resetView(View v) {
        if (isAnimatedLeft) {
            resetView(animatorLeftReset, v);
        } else if (isAnimatedRight) {
            resetView(animatorRightReset, v);
        }
    }

    private void resetView(ObjectAnimator animator, View v) {
        isAnimatedLeft = false;
        isAnimatedRight = false;
        animator.setTarget(v);
        animator.start();
    }

}
