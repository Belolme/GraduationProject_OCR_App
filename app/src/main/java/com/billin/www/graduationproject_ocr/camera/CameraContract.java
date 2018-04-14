package com.billin.www.graduationproject_ocr.camera;

import java.io.File;

public interface CameraContract {

    interface View<P> {

        void showLoading(boolean show);

    }

    interface Presenter<V> {

        void capturePhoto(File file);

        void start();

        void pause();

        void resume();

        void stop();
    }
}
