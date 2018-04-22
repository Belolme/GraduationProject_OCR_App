package com.billin.www.graduationproject_ocr.confirm;

import android.graphics.PointF;

import com.billin.www.graduationproject_ocr.base.BaseMVPView;
import com.billin.www.graduationproject_ocr.base.BasePresenter;

/**
 * 拍完照后的图片预览操作界面
 * <p>
 * Created by Billin on 2018/4/14.
 */
public interface ConfirmPictureContract {

    abstract class View extends BaseMVPView<View, Presenter> {

        abstract void showImage(String filePath);

        abstract void showLoading(boolean show);

        /**
         * 获取相对于 view 坐标系的坐标点
         */
        abstract PointF[] getQuadrilateral();

        /**
         * 设置相对于图片坐标系的坐标点
         */
        abstract void setQuadrilateralInImage(PointF[] points);

    }

    abstract class Presenter extends BasePresenter<View> {

        abstract void compressAndShow(int quantity);

        abstract void confirm();

        abstract void cancel();
    }

}
