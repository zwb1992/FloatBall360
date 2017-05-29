package com.zwb.floatball360;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zwb
 * Description 悬浮加速球
 * Date 2017/5/27.
 */

public class SpeedBall extends View {
    private int width;
    private int height;
    private Paint circlePaint;
    private Paint textPaint;
    private String text = "50%";
    private float textSize = 16;
    private Rect textRect;

    private Path path;
    private Paint pathPaint;
    private int progress = 50;
    private int max = 100;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private float density;
    private GestureDetector gestureDetector;
    private int mOffsetY = 5;//y轴上的振幅

    public SpeedBall(Context context) {
        this(context, null);
    }

    public SpeedBall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        width = (int) (100 * density);
        height = (int) (100 * density);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.ballColor));

        textSize = 16 * density;
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textRect);

        path = new Path();
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
        pathPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary1));
        pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));


        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(getContext(), "-----onDoubleTap------", Toast.LENGTH_SHORT).show();
                singleAnim();
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Toast.makeText(getContext(), "-----onSingleTapConfirmed-----", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        float x = width / 2.0f - textRect.width() / 2.0f;
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float y = height / 2 - (metrics.ascent + metrics.descent) / 2;
        bitmapCanvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
        float progressY = (1 - progress * 1.0f / max) * height;
        path.reset();
        path.moveTo(width, progressY);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(0, progressY);
        for (int i = 0; i < 5; i++) {
            path.rQuadTo(5 * density, -mOffsetY * density, 10 * density, 0);
            path.rQuadTo(5 * density, mOffsetY * density, 10 * density, 0);
        }
        path.close();
        bitmapCanvas.drawPath(path, pathPaint);
        bitmapCanvas.drawText(text, x, y, textPaint);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 单击动画
     */
    private void singleAnim() {
        mOffsetY = 10;
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SpeedBall.this.progress = (int) animation.getAnimatedValue();
                invalidate();

            }
        });
        animator.start();

        ValueAnimator animatorOffset = ValueAnimator.ofInt(mOffsetY, 0);
        animatorOffset.setDuration(1000);
        animatorOffset.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SpeedBall.this.mOffsetY = (int) animation.getAnimatedValue();
                invalidate();

            }
        });
        animatorOffset.start();
    }
}
