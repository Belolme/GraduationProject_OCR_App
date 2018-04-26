package com.billin.www.graduationproject_ocr.confirm;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.billin.www.graduationproject_ocr.OCRState;
import com.billin.www.graduationproject_ocr.module.callback.OCRCallback;
import com.billin.www.graduationproject_ocr.module.ocrservice.OCRInitService;
import com.billin.www.graduationproject_ocr.treatment.OCRTreatmentActivity;
import com.billin.www.graduationproject_ocr.util.BitmapUtil;
import com.billin.www.graduationproject_ocr.util.ImageProcessor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 对图片进行预处理操作
 * <p>
 * Created by Billin on 2018/4/14.
 */
public class ConfirmPresenter extends ConfirmPictureContract.Presenter {

    private static final String TAG = "ConfirmPresenter";

    private String mOriginFilePath;

    private String mCompressedFilePath;

    private Handler mBackgroundHandler;

    private HandlerThread mHandlerThread;

    /**
     * 以显示 view 的坐标系为标准，处理完图片后需要将获得的点从图片坐标系转换为 view 的坐标系
     */
    private PointF[] mQuadrilateral;

    @Override
    void compressAndShow(int quantity) {
        compressAndShowArea(quantity, false);
    }

    private String concatImagePath(String src, String concat) {
        String tmpTargetFilePath = null;
        int dotIndex = mOriginFilePath.length() - 1;
        for (int i = dotIndex; i >= 0; i--) {
            if (mOriginFilePath.charAt(i) == '.') {
                tmpTargetFilePath = mOriginFilePath.substring(0, i)
                        + concat + mOriginFilePath.substring(i);
                break;
            }
        }

        return tmpTargetFilePath;
    }

    private void compressAndShowArea(int quantity, boolean showArea) {
        if (TextUtils.isEmpty(mOriginFilePath)) {
            return;
        }

        getView().showLoading(true);
        String tmpTargetFilePath = concatImagePath(mOriginFilePath, "_tmp_".concat(String.valueOf(quantity)));


        if (tmpTargetFilePath == null)
            throw new RuntimeException("file path is invalidate");

        // TODO: 2018/4/14 使用后台线程压缩图片
        // 压缩图片的时候应当显示 loading
        // 为了用户体验，进度条最多显示十个档次，已经压缩过的图片直接从缓冲中取出即可
        if (!BitmapUtil.compressImage(mOriginFilePath, tmpTargetFilePath, quantity)) {
            Toast.makeText(getView(), "something is wrong, can't compress image",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mCompressedFilePath = tmpTargetFilePath;
        getView().showImage(mCompressedFilePath);

        if (showArea) {
            PointF[] quadrilateral = ImageProcessor.getInstance().getQuadrilateral(mCompressedFilePath);
            getView().setQuadrilateralInImage(quadrilateral);
            mQuadrilateral = getView().getQuadrilateralInImage();
        }

        getView().showLoading(false);
    }

    @Override
    void confirm() {

        getView().showLoading(true);

        mQuadrilateral = getView().getQuadrilateralInImage();

        final String cropImageFilePath = concatImagePath(mOriginFilePath, "_crop_");

        Bitmap perspectiveBitmap = ImageProcessor.getInstance()
                .perspectiveTransform(getView().getCurrentImage(), mQuadrilateral);

        try {
            perspectiveBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(cropImageFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (OCRState.getAccessToken() == null) {
            OCRInitService.getInstance()
                    .addListener(new OCRCallback<AccessToken>() {
                        @Override
                        public void onResult(AccessToken data) {
                            getView().showLoading(false);

                            OCRTreatmentActivity.go(getView(), cropImageFilePath);

                            OCRInitService.getInstance().removeListener(this);
                        }

                        @Override
                        public void onError(OCRError error) {
                            getView().showLoading(false);

                            Toast.makeText(getView(), "Check your network!!", Toast.LENGTH_SHORT)
                                    .show();

                            OCRInitService.getInstance().removeListener(this);
                        }
                    });

            return;
        }

        getView().showLoading(false);

        OCRTreatmentActivity.go(getView(), cropImageFilePath);
    }

    @Override
    void cancel() {
        getView().finish();
    }

    @Override
    public void onStart() {
        // 初始化图片处理的线程
        mHandlerThread = new HandlerThread("image processor");
        mHandlerThread.start();
        mBackgroundHandler = new Handler(mHandlerThread.getLooper());

        // 显示图片在界面上
        String filePath = getView().getIntent().getStringExtra(ConfirmView.KEY_FILE_PATH);
        if (filePath == null) {
            Toast.makeText(getView(), "Something was wrong, can't get image",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mOriginFilePath = filePath;
        compressAndShowArea(50, true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mBackgroundHandler.removeCallbacksAndMessages(null);
        mBackgroundHandler = null;

        mHandlerThread.getLooper().quit();
        mHandlerThread = null;
    }
}
