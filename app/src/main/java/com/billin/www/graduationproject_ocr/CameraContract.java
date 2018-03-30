package com.billin.www.graduationproject_ocr;

import java.io.File;

public interface CameraContract {

    interface View<P> {

    }

    interface Presenter<V> {

        void capturePhoto(File file);

        void start();

        void pause();

        void resume();

        void stop();
    }
}
