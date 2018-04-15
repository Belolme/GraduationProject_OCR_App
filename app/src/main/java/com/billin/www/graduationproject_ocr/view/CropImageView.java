package com.billin.www.graduationproject_ocr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.billin.www.graduationproject_ocr.R;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 可以显示并设置裁剪框的 ImageView
 * <p>
 * Created by Billin on 2018/4/15.
 */
public class CropImageView extends android.support.v7.widget.AppCompatImageView {

    private static final float POINT_RADIUS
            = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            12, Resources.getSystem().getDisplayMetrics());

    private Paint mPaint;

    private Path mPath;

    private PointF[] mPoints;

    private boolean mIsDragPoint;

    private PointF mDragPoint;

    public CropImageView(Context context) {
        super(context);
        init();
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPoints = new PointF[4];

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mPath = new Path();
    }

    private void initPoint(int width, int height) {
        mPoints[0] = new PointF(0, 0);
        mPoints[1] = new PointF(width, 0);
        mPoints[2] = new PointF(0, height);
        mPoints[3] = new PointF(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPoint(w, h);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean superRes = super.onTouchEvent(event);
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                mDragPoint = getPointWithArea(x, y);
                if (mDragPoint != null) {
                    mIsDragPoint = true;
                } else {
                    mIsDragPoint = false;
                    return superRes;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (!mIsDragPoint)
                    return superRes;

                mDragPoint.x = x;
                mDragPoint.y = y;
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (!mIsDragPoint)
                    return superRes;

                mIsDragPoint = false;
                postInvalidate();
                break;
        }

        return true;
    }

    private PointF getPointWithArea(float x, float y) {
        for (PointF pointF : mPoints) {
            if (isWithinPointArea(pointF, x, y)) {
                return pointF;
            }
        }

        return null;
    }

    private boolean isWithinPointArea(PointF pointF, float x, float y) {
        return x > pointF.x - POINT_RADIUS && y > pointF.y - POINT_RADIUS
                && x < pointF.x + POINT_RADIUS && y < pointF.y + POINT_RADIUS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (PointF point : mPoints) {
            canvas.drawOval(point.x - POINT_RADIUS,
                    point.y - POINT_RADIUS,
                    point.x + POINT_RADIUS,
                    point.y + POINT_RADIUS, mPaint);
        }

        sortPoint();
        mPath.reset();
        mPath.moveTo(mPoints[0].x, mPoints[0].y);
        for (int i = 1; i < 4; i++) {
            mPath.lineTo(mPoints[i].x, mPoints[i].y);
        }
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 按逆时针排序四边形的点
     */
    private void sortPoint() {

        // 找到最底部的点并设置在第一位
        for (int i = 1; i < mPoints.length; i++) {
            if (mPoints[i].y > mPoints[0].y) {
                PointF tmp = mPoints[i];
                mPoints[i] = mPoints[0];
                mPoints[0] = tmp;
            }
        }

        // 逆时针扫描所有的点进行排序
        final PointF finalVertical = mPoints[0];
        Arrays.sort(mPoints, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Double.compare(getR(o1), getR(o2));
            }

            private double getR(PointF o) {
                return o.equals(finalVertical) ? 2.0
                        : (o.x - finalVertical.x)
                        / (Math.sqrt(Math.pow(o.x - finalVertical.x, 2))
                        + Math.pow(o.y - finalVertical.y, 2));
            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
