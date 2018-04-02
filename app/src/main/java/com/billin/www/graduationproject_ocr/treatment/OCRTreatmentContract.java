package com.billin.www.graduationproject_ocr.treatment;

import com.billin.www.graduationproject_ocr.base.BaseMVPView;
import com.billin.www.graduationproject_ocr.base.BasePresenter;

/**
 * 进行文字识别后的预览，处理
 * <p>
 * Created by Billin on 2018/3/31.
 */
public interface OCRTreatmentContract<T> {

    abstract class View extends BaseMVPView<View, Presenter> {

        abstract void showOcrString(String ocr);

        abstract void moveImageToLocation(int dx, int dy);

    }

    abstract class Presenter extends BasePresenter<View> {

        abstract void processOcrString(String imgPath);

        /**
         * 用户处理完文字后回调这个接口
         */
        abstract void confirm(String res);

        /**
         * 用户不对这个结果进行处理
         */
        abstract void cancel();

        abstract void clickCharPos(int pos);

    }

}
