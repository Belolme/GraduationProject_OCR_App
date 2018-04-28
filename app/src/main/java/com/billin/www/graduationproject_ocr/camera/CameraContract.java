package com.billin.www.graduationproject_ocr.camera;

import android.graphics.RectF;
import android.support.annotation.Nullable;

import java.io.File;

public interface CameraContract {

    interface View<P> {

        void showLoading(boolean show);

        /**
         * 显示相应的类型
         */
        void showStyle(int styleId);
    }

    interface Presenter<V> {

        /**
         * 获取到照片
         *
         * @param file             照片保留的文件位置
         * @param rectPointInImage 如果 style 为 R.id.id_card 时，会传入蒙版映射到照片四个点的坐标
         */
        void capturePhoto(File file,@Nullable RectF rectPointInImage);

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
