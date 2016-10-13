package it.polito.mad.polijob.company;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.ViewSwitcher;

/**
 * Created by giuseppe on 18/05/15.
 */
public class MyAnimatorListener extends AnimatorListenerAdapter {
    private static final long DURATION = 400;
    private ViewSwitcher vs;

    public MyAnimatorListener(ViewSwitcher vs) {
        this.vs = vs;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        vs.getNextView().setAlpha(0f);
        vs.showNext();
        vs.getCurrentView().animate().alpha(1f).setDuration(DURATION).setListener(null).start();
    }
}