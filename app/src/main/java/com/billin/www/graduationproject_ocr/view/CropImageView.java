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

    private static final String TAG = "CropImageView";

    private static final float POINT_RADIUS
            = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            12, Resources.getSystem().getDisplayMetrics());

    private Paint mPaint;

    private Path mPath;

    private PointF[] mImageCoordinatePoints;

    private PointF[] mViewCoordinatePoints;

    private float[] mTmpPointCache;

    private Matrix mTmpMatrix;

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
        mImageCoordinatePoints = new PointF[4];
        mViewCoordinatePoints = new PointF[4];
        mTmpPointCache = new float[8];
        mTmpMatrix = new Matrix();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mPath = new Path();
    }

    private void initPoint(int width, int height) {
        // 当这个值不为空的时候，表示已经在外部设置了这个值, 需要做同步处理
        if (mImageCoordinatePoints[0] != null) {
            syncImageAndViewCoordinate(true);
            return;
        }

        mImageCoordinatePoints[0] = new PointF(POINT_RADIUS, POINT_RADIUS);
        mImageCoordinatePoints[1] = new PointF(width - POINT_RADIUS, POINT_RADIUS);
        mImageCoordinatePoints[2] = new PointF(POINT_RADIUS, height - POINT_RADIUS);
        mImageCoordinatePoints[3] = new PointF(width - POINT_RADIUS, height - POINT_RADIUS);

        mViewCoordinatePoints[0] = new PointF(POINT_RADIUS, POINT_RADIUS);
        mViewCoordinatePoints[1] = new PointF(width - POINT_RADIUS, POINT_RADIUS);
        mViewCoordinatePoints[2] = new PointF(POINT_RADIUS, height - POINT_RADIUS);
        mViewCoordinatePoints[3] = new PointF(width - POINT_RADIUS, height - POINT_RADIUS);
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
                syncImageAndViewCoordinate(true);
                mDragPoint = getPointWithArea(x, y);
                if (mDragPoint != null) {
                    mIsDragPoint = true;
                } else {
                    mIsDragPoint = false;
                    return superRes;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (!mIsDragPoint
                        || x < POINT_RADIUS
                        || y < POINT_RADIUS
                        || x > getWidth() - POINT_RADIUS
                        || y > getHeight() - POINT_RADIUS)
                    return superRes;

                mDragPoint.x = x;
                mDragPoint.y = y;

                syncImageAndViewCoordinate(false);
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (!mIsDragPoint)
                    return superRes;

                syncImageAndViewCoordinate(false);
                mIsDragPoint = false;
                postInvalidate();
                break;
        }

        return true;
    }

    private PointF getPointWithArea(float x, float y) {
        for (PointF pointF : mViewCoordinatePoints) {
            if (isWithinPointArea(pointF, x, y)) {
                return pointF;
            }
        }

        return null;
    }

    /**
     * 映射以图片为坐标系的点到以 View 为坐标系的点
     *
     * @param imageToView true 图片映射到 View, false View 映射到图片
     */
    private void syncImageAndViewCoordinate(boolean imageToView) {
        if (imageToView) {
            mapToFloatArray(mImageCoordinatePoints, mTmpPointCache);
            getImageMatrix().mapPoints(mTmpPointCache);
            mapToPointArray(mTmpPointCache, mViewCoordinatePoints);
        } else {
            mapToFloatArray(mViewCoordinatePoints, mTmpPointCache);
            getImageMatrix().invert(mTmpMatrix);
            mTmpMatrix.mapPoints(mTmpPointCache);
            mapToPointArray(mTmpPointCache, mImageCoordinatePoints);
        }
    }

    private boolean isWithinPointArea(PointF pointF, float x, float y) {
        return x > pointF.x - POINT_RADIUS && y > pointF.y - POINT_RADIUS
                && x < pointF.x + POINT_RADIUS && y < pointF.y + POINT_RADIUS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int c = canvas.save();
        if (getImageMatrix() != null)
            canvas.concat(getImageMatrix());

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (PointF point : mImageCoordinatePoints) {
            canvas.drawOval(point.x - POINT_RADIUS,
                    point.y - POINT_RADIUS,
                    point.x + POINT_RADIUS,
                    point.y + POINT_RADIUS, mPaint);
        }

        mPaint.setStyle(Paint.Style.STROKE);

        sortPoint(mImageCoordinatePoints);
        mPath.reset();
        mPath.moveTo(mImageCoordinatePoints[0].x, mImageCoordinatePoints[0].y);
        for (int i = 1; i < 4; i++) {
            mPath.lineTo(mImageCoordinatePoints[i].x, mImageCoordinatePoints[i].y);
        }
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        canvas.restoreToCount(c);
    }

    /**
     * 获取相对于 View 坐标系的四个标注点坐标
     */
    public PointF[] getPointInView() {
        sortPoint(mViewCoordinatePoints);
        return mImageCoordinatePoints;
    }

    /**
     * 获取相对于图片坐标系的四个标注点坐标
     */
    public PointF[] getPointsInImage() {
        sortPoint(mImageCoordinatePoints);
        return mImageCoordinatePoints;
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

        mImageCoordinatePoints = points;
        syncImageAndViewCoordinate(true);
    }

    private float[] mapToFloatArray(PointF[] point) {
        float[] floats = new float[point.length * 2];
        mapToFloatArray(point, floats);
        return floats;
    }

    private void mapToFloatArray(PointF[] src, float[] dst) {
        for (int i = 0; i < src.length; i++) {
            dst[2 * i] = src[i].x;
            dst[2 * i + 1] = src[i].y;
        }
    }

    /**
     * 这一个方法把 float 表示的 point 数据转换为 PointF 表示的 point 数组
     *
     * @return 返回值是一个全新分配的内存空间的 PointF 数组
     */
    private PointF[] mapToPointArray(float[] point) {
        PointF[] dst = new PointF[point.length / 2];
        mapToPointArray(point, dst);
        return dst;
    }

    /**
     * 功能和 {@link #mapToFloatArray(PointF[])} 相同，不过这个函数没有返回值，
     * 反之需要传入一个 PointF 数组用以获取转换后的结果
     */
    private void mapToPointArray(float[] src, PointF[] dst) {
        for (int i = 0; i < src.length / 2; i++) {
            dst[i] = new PointF(src[i * 2], src[i * 2 + 1]);
        }
    }

    /**
     * 按逆时针排序四边形的点
     */
    private void sortPoint(PointF[] points) {

        // 找到最底部的点并设置在第一位
        for (int i = 1; i < points.length; i++) {
            if (points[i].y > points[0].y) {
                PointF tmp = points[i];
                points[i] = points[0];
                points[0] = tmp;
            }
        }

        // 逆时针扫描所有的点进行排序
        final PointF finalVertical = points[0];
        Arrays.sort(points, new Comparator<PointF>() {
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
