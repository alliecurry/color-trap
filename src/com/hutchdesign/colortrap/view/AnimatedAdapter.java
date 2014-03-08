package com.hutchdesign.colortrap.view;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.BaseAdapter;

/**
 * An adapter which will animate each of its Views on draw.
 * @author alliecurry
 */
public abstract class AnimatedAdapter extends BaseAdapter {

    protected ObjectAnimator animator;
    protected boolean animate = false;
    protected int delay = 0;
    private int delayAmount = 0;

    public AnimatedAdapter(Context context, int animatorRes, int duration) {
        animator = (ObjectAnimator) AnimatorInflater.loadAnimator(context, animatorRes);
        animator.setDuration(duration);
    }

    public void animate() {
        animate = true;
        delay = 0;
        notifyDataSetChanged();
    }

    protected void setDelay(int delay) {
        delayAmount = delay;
    }

    /** To animate all child Views, call this function on getView(). */
    protected void doAnimation(final View view, int position) {
        doAnimation(view, position, false);
    }

    /** To animate all child Views, call this function on getView().
     *  @param skip - true when to not animate the particular View. Useful if only animating a portion of the Views. */
    protected void doAnimation(final View view, int position, boolean skip) {
        if (!animate) {
            return;
        }

        if (!skip) {
            final ObjectAnimator anim = animator.clone();
            anim.setTarget(view);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    anim.start();
                    onAnimate(view);
                    view.setVisibility(View.VISIBLE);
                }
            }, delay += delayAmount);
        }

        // Reset animation
        if (position == getCount() - 1) {
            animate = false;
            delay = 0;
        }
    }

    /** Called during animation. Perform any additional transformations not in the animator here. */
    public abstract void onAnimate(View view);

}
