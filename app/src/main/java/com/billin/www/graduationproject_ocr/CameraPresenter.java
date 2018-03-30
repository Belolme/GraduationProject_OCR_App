package com.billin.www.graduationproject_ocr;

import android.util.Log;

import java.io.File;

public class CameraPresenter implements CameraContract.Presenter<CameraContract.View> {

    private static final String TAG = "CameraPresenter";

    private CameraContract.View mView;

    public void setView(CameraContract.View view) {
        this.mView = view;
    }

    @Override
    public void capturePhoto(File file) {
        Log.d(TAG, "capturePhoto: " + file);
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
