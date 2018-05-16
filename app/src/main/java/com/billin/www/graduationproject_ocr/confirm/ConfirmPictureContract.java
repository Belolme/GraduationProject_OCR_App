package com.billin.www.graduationproject_ocr.confirm;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.billin.www.graduationproject_ocr.base.BaseMVPView;
import com.billin.www.graduationproject_ocr.base.BasePresenter;

/**
 * 拍完照后的图片预览操作界面
 * <p>
 * Created by Billin on 2018/4/14.
 */
public interface ConfirmPictureContract {

    /**
     * 文档边缘识别修正模块视图接口
     */
    abstract class View extends BaseMVPView<View, Presenter> {

        /**
         * 在界面中展示图片
         *
         * @param filePath 图片的本地储存路径
         */
        abstract void showImage(String filePath);

        /**
         * 切换 loading 状态，期间禁止用户对界面做出任何操作
         *
         * @param show true 表示显示 loading 状态，false 表示不显示 loading 状态
         */
        abstract void showLoading(boolean show);

        /**
         * 获取四边形区域的坐标点，该坐标点的坐标系为图片坐标系
         */
        abstract PointF[] getQuadrilateralInImage();

        /**
         * 设置四边形区域的坐标点，该坐标点参考系为图片坐标系
         */
        abstract void setQuadrilateralInImage(PointF[] points);

        /**
         * 获取当前界面正在显示的图片
         *
         * @return 该图片数据以 Bitmap 数据格式返回
         */
        abstract Bitmap getCurrentImage();

    }

    /**
     * 文档边缘识别修正模块业务逻辑接口
     */
    abstract class Presenter extends BasePresenter<View> {

        /**
         * 请求对从拍照模块获取的图片进行压缩并在界面展示压缩后的图片
         *
         * @param quantity 图片质量压缩参数，取值范围为 1 - 100
         */
        abstract void compressAndShow(int quantity);

        /**
         * 用户确定调整完四边形区域后，调用此方法。调用此方法后，
         * 会对图片进行四边形区域的透视变换并跳转到文字识别后处理模块中
         */
        abstract void confirm();

        /**
         * 用户取消对该图像的文字识别操作时调用此方法。此方法不会再进行任何对
         * 图像的后续处理并返回拍照模块界面
         */
        abstract void cancel();
    }

}
