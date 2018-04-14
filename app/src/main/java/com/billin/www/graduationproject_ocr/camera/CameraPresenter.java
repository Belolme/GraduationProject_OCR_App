package com.billin.www.graduationproject_ocr.camera;


import android.support.v4.app.Fragment;

import com.billin.www.graduationproject_ocr.confirm.ConfirmView;

import java.io.File;

public class CameraPresenter implements CameraContract.Presenter<CameraContract.View> {

    private static final String TAG = "CameraPresenter";

    private CameraContract.View mView;

    public void setView(CameraContract.View view) {
        this.mView = view;
    }

    @Override
    public void capturePhoto(final File file) {

        ConfirmView.go(((Fragment) mView).getActivity(), file.getPath());

     /*   mView.showLoading(true);

        if (!BitmapUtil.compressImage(file.getPath(), file.getPath(), 20)) {
            Toast.makeText(((Fragment) mView).getContext(),
                    "Something is wrong, Check your storage",
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }

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
        OCRTreatmentActivity.go(((Fragment) mView).getActivity(), file.getPath());*/
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
