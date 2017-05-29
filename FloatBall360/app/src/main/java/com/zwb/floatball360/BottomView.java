package com.zwb.floatball360;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by zwb
 * Description
 * Date 17/5/29.
 */

public class BottomView extends LinearLayout {
    private TranslateAnimation animation;
    private RelativeLayout rl_bottom;
    public BottomView(final Context context) {
        super(context);
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_layout, this, true);
        rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        rl_bottom.setAnimation(getAnim());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    public void startAnim() {
        animation.start();
    }


    private TranslateAnimation getAnim() {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        return animation;
    }

    public void remove(){
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FloatManager manager = FloatManager.getInstance(getContext());
                manager.dissMissMenuView();
                manager.showFloatBall();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rl_bottom.clearAnimation();
        rl_bottom.setAnimation(animation);
        animation.start();
    }
}
