package com.billin.www.graduationproject_ocr.camera;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.billin.www.graduationproject_ocr.OCRState;
import com.billin.www.graduationproject_ocr.module.callback.OCRCallback;
import com.billin.www.graduationproject_ocr.module.ocrservice.OCRInitService;
import com.billin.www.graduationproject_ocr.treatment.OCRTreatmentActivity;
import com.billin.www.graduationproject_ocr.util.BitmapUtil;

import java.io.File;

public class CameraPresenter implements CameraContract.Presenter<CameraContract.View> {

    private static final String TAG = "CameraPresenter";

    private CameraContract.View mView;

    public void setView(CameraContract.View view) {
        this.mView = view;
    }

    @Override
    public void capturePhoto(final File file) {

        mView.showLoading(true);

        BitmapUtil.compressImage(file.getPath(), file.getPath(), 20);

        if (OCRState.getAccessToken() == null) {
            OCRInitService.getInstance()
                    .addListener(new OCRCallback<AccessToken>() {
                        @Override
                        public void onResult(AccessToken data) {
                            mView.showLoading(false);

                            OCRTreatmentActivity.go(((Fragment) mView).getActivity(),
                                    file.getPath());

                            OCRInitService.getInstance().removeListener(this);
                        }

                        @Override
                        public void onError(OCRError error) {
                            mView.showLoading(false);

                            Toast.makeText(((Fragment) mView).getContext(),
                                    "Something is wrong, Check your network",
                                    Toast.LENGTH_SHORT)
                                    .show();

                            OCRInitService.getInstance().removeListener(this);
                        }
                    });

            return;
        }

        mView.showLoading(false);
        OCRTreatmentActivity.go(((Fragment) mView).getActivity(), file.getPath());
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }
}
