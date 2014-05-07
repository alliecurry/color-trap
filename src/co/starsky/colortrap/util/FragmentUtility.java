package co.starsky.colortrap.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import co.starsky.colortrap.R;

/**
 * @author alliecurry
 */
public final class FragmentUtility {

    private FragmentUtility() {
        throw new AssertionError();
    }

    public static void addFragment(Activity fragmentActivity, Fragment fragment, int containerId) {
        addFragment(fragmentActivity, fragment, containerId, null);
    }

    public static void addFragment(Activity fragmentActivity, Fragment fragment, int containerId, String tag) {
        FragmentManager fragmentManager = fragmentActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public static void replaceFragment(Activity fragmentActivity, Fragment fragment, int containerId, String tag) {
        FragmentManager fragmentManager = fragmentActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);
        fragmentTransaction.replace(containerId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public static void removeFragment(Activity fragmentActivity, Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void popFragmentStack(Activity fragmentActivity) {
        fragmentActivity.getFragmentManager().popBackStack();
    }

    public static String getCurrentFragmentTag(Activity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getFragmentManager();
        FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        return entry.getName();
    }

    public static int getBackStackCount(Activity fragmentActivity) {
        return fragmentActivity.getFragmentManager().getBackStackEntryCount();
    }

}

