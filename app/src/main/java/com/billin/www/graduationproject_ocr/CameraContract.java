package com.billin.www.graduationproject_ocr;

public interface CameraContract {

    interface View<P> {

    }

    interface Presenter<V> {

        void capturePhoto();

        void start();

        void pause();

        void resume();

        void stop();
    }
}
