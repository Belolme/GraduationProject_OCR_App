package com.billin.www.graduationproject_ocr;

import android.app.Application;
import android.util.Log;

import com.billin.www.graduationproject_ocr.module.ocrservice.OCRInitService;

import org.opencv.android.OpenCVLoader;

/**
 * 用于初始化一些东西
 * <p>
 * Created by Billin on 2018/3/31.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");

        initOpenCV();
        initBaiduOCR();
    }

    private void initOpenCV() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }
    }

    private void initBaiduOCR() {
        OCRInitService.getInstance().initService(this);
    }
}
