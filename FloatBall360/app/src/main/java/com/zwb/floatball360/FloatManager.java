package com.zwb.floatball360;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by zwb
 * Description
 * Date 2017/5/27.
 */

public class FloatManager {
    private static FloatManager floatManager;
    private FloatBall floatBall;
    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private float downX;
    private float downY;
    private int mTouchSlop;//是否移动的边界值

    private float firstDownX;
    private float firstDownY;

    private FloatManager(final Context context) {
        this.context = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        floatBall = new FloatBall(context);
        floatBall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        firstDownX = event.getRawX();
                        firstDownY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();
                        float dx = moveX - downX;
                        float dy = moveY - downY;
                        if (isDrag(dx, dy)) {
                            params.x += dx;
                            params.y += dy;
                            windowManager.updateViewLayout(floatBall, params);
                            downX = moveX;
                            downY = moveY;
                            floatBall.setDragState(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        int upX = (int) event.getRawX();
                        int upY = (int) event.getRawY();
                        transitionAnim(upX);
                        floatBall.setDragState(false);
                        return isDrag(upX - firstDownX, upY - firstDownY);
                }
                return false;
            }
        });
        floatBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "悬浮小球", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 小球的中心点是否靠左
     *
     * @param rawX
     * @return
     */
    private boolean isOffsetLeft(float rawX) {
        return rawX < getScreenWidth() / 2.0f;
    }

    /**
     * 是否是移动
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isDrag(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    /**
     * 开始平移动画
     *
     * @param rawX 当前的rawX的数值
     */
    private void transitionAnim(int rawX) {
        int to;
        if (isOffsetLeft(rawX)) {
            if (rawX == 0) {//已经到了最左端
                return;
            } else {
                to = 0;
            }
        } else {
            //已经到了最右端
            if (rawX == getScreenWidth() - floatBall.getMeasuredWidth()) {
                return;
            } else {
                to = getScreenWidth() - floatBall.getMeasuredWidth();
            }
        }
        ValueAnimator animator = ValueAnimator.ofInt(rawX, to);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dx = (int) animation.getAnimatedValue();
                params.x = dx;
                windowManager.updateViewLayout(floatBall, params);
            }
        });
        animator.start();
    }

    /**
     * 获取屏幕的宽
     *
     * @return
     */
    private int getScreenWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static FloatManager getInstance(Context context) {
        if (floatManager == null) {
            synchronized (FloatManager.class) {
                if (floatManager == null) {
                    floatManager = new FloatManager(context);
                }
            }
        }
        return floatManager;
    }

    /**
     * 显示悬浮球
     */
    public void showFloatBall() {
        params = new WindowManager.LayoutParams();
        params.width = floatBall.getMeasuredWidth();
        params.height = floatBall.getMeasuredHeight();
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置不获取焦点且不影响其他控件的点击事件
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;//设置背景透明
        windowManager.addView(floatBall, params);
    }
}
