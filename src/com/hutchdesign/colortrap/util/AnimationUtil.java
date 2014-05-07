package com.hutchdesign.colortrap.util;

/**
 * @author alliecurry
 */
public final class AnimationUtil {
    private static final int CURVE_DURATION = 200;


//    private void jump(final View view) {
//        // Set up the path we're animating along
//        AnimatorPath path = new AnimatorPath();
//        float x = view.getX();
//        float y = view.getY() + (view.getHeight() / 4);
//        int width = view.getWidth();
//        path.moveTo(x, y);
//        path.curveTo(x+80, y-80, x+width, y, x+width, y);
//
//        AnimatorProxy.wrap(animateView);
//
//        // Set up the animation
//        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLoc",
//                new PathEvaluator(), path.getPoints().toArray());
//
//        anim.setDuration(250);
//        animateView.setVisibility(View.VISIBLE);
//
//        anim.start();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                animateView.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged();
//            }
//        }, 250);
//    }

}
