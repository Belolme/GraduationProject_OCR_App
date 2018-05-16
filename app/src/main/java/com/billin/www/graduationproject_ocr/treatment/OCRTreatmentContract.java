package com.billin.www.graduationproject_ocr.treatment;


import com.billin.www.graduationproject_ocr.base.BaseMVPView;
import com.billin.www.graduationproject_ocr.base.BasePresenter;

/**
 * 进行文字识别后的预览，处理
 * <p>
 * Created by Billin on 2018/3/31.
 */
public interface OCRTreatmentContract<T> {

    /**
     * 文字识别后处理模块视图接口
     */
    abstract class View extends BaseMVPView<View, Presenter> {

        /**
         * 显示文字识别文本
         *
         * @param ocr 文字识别引擎识别到的文本
         */
        abstract void showOcrString(String ocr);

        /**
         * 移动预览图片到相应的位置
         *
         * @param dx 图片的 x 坐标
         * @param dy 图片的 y 坐标
         */
        abstract void moveImageToLocation(int dx, int dy);

        /**
         * 获取界面显示的文字
         */
        abstract String getText();
    }

    /**
     * 文字识别后处理模块事务逻辑接口
     */
    abstract class Presenter extends BasePresenter<View> {

        /**
         * 文字识别接口
         *
         * @param imgPath 需要识别的图片文件地址
         * @param style   文字识别的类型，R.id.normal 或者 R.id.id_card
         */
        abstract void processOcrString(String imgPath, int style);

        /**
         * 界面的文本被点击时调用
         *
         * @param pos 文本的点击位置
         */
        abstract void clickCharPos(int pos);
    }

}
