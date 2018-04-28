package com.billin.www.graduationproject_ocr.camera;


import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.billin.www.graduationproject_ocr.confirm.ConfirmView;

import java.io.File;

public class CameraPresenter implements CameraContract.Presenter<CameraContract.View> {

    private static final String TAG = "CameraPresenter";

    private CameraContract.View mView;

    private int mStyle;

    public void setView(CameraContract.View view) {
        this.mView = view;
    }

    @Override
    public void capturePhoto(final File file, @Nullable RectF rectPointInImage) {
        ConfirmView.go(((Fragment) mView).getActivity(), file.getPath(), rectPointInImage, mStyle);
    }

    @Override
    public void switchRecognizeType(int typeId) {
        mStyle = typeId;
        mView.showStyle(mStyle);
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
