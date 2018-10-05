package www.vaiyee.com.travelschool.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Administrator on 2018/10/5.
 */

public class ScaleAnimation extends BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f,0.5f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 0.5f,1f)
        };
    }
}
