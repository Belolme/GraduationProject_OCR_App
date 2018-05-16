package com.billin.www.graduationproject_ocr.camera;

import android.graphics.RectF;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * 拍照模块接口
 */
public interface CameraContract {

    /**
     * 拍照模块视图接口
     *
     * @param <P> 拍照模块业务逻辑处理实现类
     */
    interface View<P> {

        /**
         * 切换 loading 状态，期间禁止用户对界面做出任何操作
         *
         * @param show true 表示显示 loading 状态，false 表示不显示 loading 状态
         */
        void showLoading(boolean show);

        /**
         * 根据文字识别类型做相应的界面变化
         *
         * @param styleId 对应的值应当为 R.id.id_card 或者 R.id.normal
         */
        void showStyle(int styleId);
    }

    /**
     * 拍照模块业务处理层接口
     *
     * @param <V> 拍照模块视图实现类
     */
    interface Presenter<V> {

        /**
         * 用户点击拍摄按钮拍摄照片时，这个方法应当被调用
         *
         * @param file             照片保留的文件位置
         * @param rectPointInImage 如果通过 {@link View#showStyle(int)} 设置界面的 style
         *                         为 R.id.id_card，这个参数为蒙版映射到照片的四个点的坐标
         */
        void capturePhoto(File file, @Nullable RectF rectPointInImage);

        /**
         * 切换识别的类型
         *
         * @param typeId R.id.normal, R.id.id_card
         */
        void switchRecognizeType(int typeId);

        void start();

        void pause();

        void resume();

        void stop();
    }
}
