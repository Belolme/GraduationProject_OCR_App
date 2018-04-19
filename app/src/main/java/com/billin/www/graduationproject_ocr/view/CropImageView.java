package com.billin.www.graduationproject_ocr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mPath = new Path();
    }

    private void initPoint(int width, int height) {
        mPoints[0] = new PointF(POINT_RADIUS, POINT_RADIUS);
        mPoints[1] = new PointF(width - POINT_RADIUS, POINT_RADIUS);
        mPoints[2] = new PointF(POINT_RADIUS, height - POINT_RADIUS);
        mPoints[3] = new PointF(width - POINT_RADIUS, height - POINT_RADIUS);
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

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (PointF point : mPoints) {
            canvas.drawOval(point.x - POINT_RADIUS,
                    point.y - POINT_RADIUS,
                    point.x + POINT_RADIUS,
                    point.y + POINT_RADIUS, mPaint);
        }

        mPaint.setStyle(Paint.Style.STROKE);

        sortPoint();
        mPath.reset();
        mPath.moveTo(mPoints[0].x, mPoints[0].y);
        for (int i = 1; i < 4; i++) {
            mPath.lineTo(mPoints[i].x, mPoints[i].y);
        }
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 获取相对于图片坐标系的四个标注点坐标
     */
    public PointF[] getPointsInImage() {
        Matrix matrix = getImageMatrix();

        Matrix reverse = new Matrix();
        if (!matrix.invert(reverse)) {
            throw new RuntimeException("cannot reverse matrix");
        }

        sortPoint();

        float[] srcPoint = mapToFloatArray(mPoints);
        float[] dstPoint = new float[8];
        reverse.mapPoints(dstPoint, srcPoint);

        return mapToPointArray(dstPoint);
    }

    /**
     * 在相对于图片的坐标系中设置四个绘制点
     *
     * @throws IllegalArgumentException 当 points 的数量不是四个的时候抛出此异常
     */
    public void setPointInImage(PointF[] points) {
        if (points.length != 4) {
            throw new IllegalArgumentException("invalidate points argument with length "
                    + points.length);
        }

        Matrix matrix = getImageMatrix();

        float[] dst = new float[8];
        matrix.mapPoints(dst, mapToFloatArray(points));

        mPoints = mapToPointArray(dst);
        postInvalidate();
    }

    private float[] mapToFloatArray(PointF[] point) {
        float[] floats = new float[point.length];
        for (int i = 0; i < point.length; i++) {
            floats[2 * i] = mPoints[i].x;
            floats[2 * i + 1] = mPoints[i].y;
        }

        return floats;
    }

    private PointF[] mapToPointArray(float[] point) {
        PointF[] dst = new PointF[point.length / 2];
        for (int i = 0; i < point.length / 2; i++) {
            dst[i] = new PointF(point[i * 2], point[i * 2 + 1]);
        }

        return dst;
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
                        / (Math.sqrt(Math.pow(o.x - finalVertical.x, 2)
                        + Math.pow(o.y - finalVertical.y, 2)));
            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
