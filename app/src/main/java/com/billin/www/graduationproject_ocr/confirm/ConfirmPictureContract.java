package com.billin.www.graduationproject_ocr.confirm;

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

    }

    abstract class Presenter extends BasePresenter<View> {

        abstract void compressAndShow(int quantity);

        abstract void confirm();

        abstract void cancel();
    }

}
