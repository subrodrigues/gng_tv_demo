package pt.gngtv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by filiperodrigues on 07/08/2015.
 */
public interface Animations {

   /* class BounceSlightyDownAnimator extends BaseViewAnimator {
        private Context context;

        public BounceSlightyDownAnimator(Context context) {
            this.context = context;
        }

        public void prepare(View target) {
            this.getAnimatorAgent().playTogether(
                    new Animator[]{
                            ObjectAnimator.ofFloat(target, "translationY", new float[]{(float)100, 30.0F, -10.0F, 0.0F})});
        }
    }

    class BounceSlightyUpAnimator extends BaseViewAnimator {
        private Context context;

        public BounceSlightyUpAnimator(Context context) {
            this.context = context;
        }

        public void prepare(View target) {
            this.getAnimatorAgent().playTogether(
                    new Animator[]{
                            ObjectAnimator.ofFloat(target, "translationY", new float[]{(float)(100, -30.0F, 10.0F, 0.0F})});
        }
    }
*/
    class SlideOutLeftNoTransparencyAnimator extends BaseViewAnimator {
        public SlideOutLeftNoTransparencyAnimator() {
        }

        public void prepare(View target) {
            this.getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "translationX", new float[]{0.0F, (float)(-target.getRight())})});
        }
    }

    class SlideInLeftNoTransparencyAnimator extends BaseViewAnimator {
        public SlideInLeftNoTransparencyAnimator() {
        }

        public void prepare(View target) {
            ViewGroup parent = (ViewGroup)target.getParent();
            int distance = parent.getWidth() - target.getLeft();
            this.getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "translationX", new float[]{(float)(-distance), 0.0F})});
        }
    }

    class SmoothPulseAnimator extends BaseViewAnimator {
        public SmoothPulseAnimator() {
        }

        public void prepare(View target) {
            this.getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "scaleY", new float[]{1.0F, 1.01F, 1.0F}), ObjectAnimator.ofFloat(target, "scaleX", new float[]{1.0F, 1.01F, 1.0F})});
        }
    }

    class SmoothBounceAnimator extends BaseViewAnimator {
        public SmoothBounceAnimator() {
        }

        public void prepare(View target) {
            this.getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "translationY", new float[]{0.0F, 0.0F, -10.0F, 0.0F, -5.0F, 0.0F, 0.0F})});
        }
    }
}
