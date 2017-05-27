package com.zwb.floatball360;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zwb
 * Description 悬浮加速球
 * Date 2017/5/27.
 */

public class FloatBall extends View {
    private int width;
    private int height;
    private Paint circlePaint;
    private Paint textPaint;
    private String text = "50%";
    private float textSize = 16;
    private Rect textRect;
    private boolean isDrag = false;
    private Bitmap bitmap;

    public FloatBall(Context context) {
        this(context, null);
    }

    public FloatBall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        float density = getResources().getDisplayMetrics().density;
        width = (int) (50 * density);
        height = (int) (50 * density);

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

        Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        bitmap = Bitmap.createScaledBitmap(tempBitmap, width, height, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrag) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        } else {
            canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
            float x = width / 2.0f - textRect.width() / 2.0f;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float y = height / 2 - (metrics.ascent + metrics.descent) / 2;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    /**
     * 是否正在拖拽
     *
     * @param flag
     */
    public void setDragState(boolean flag) {
        isDrag = flag;
        invalidate();
    }
}
