package com.billin.www.graduationproject_ocr.treatment;


import android.support.v7.widget.ShareActionProvider;

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

        /**
         * 获取界面显示的文字
         */
        abstract String getText();
    }

    abstract class Presenter extends BasePresenter<View> {

        abstract void processOcrString(String imgPath);

        abstract void clickCharPos(int pos);
    }

}
