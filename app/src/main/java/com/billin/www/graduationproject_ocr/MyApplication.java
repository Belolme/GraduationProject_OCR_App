package com.billin.www.graduationproject_ocr;

import android.app.Application;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

import org.opencv.android.OpenCVLoader;

/**
 * 用于初始化一些东西
 * <p>
 * Created by Billin on 2018/3/31.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.e(TAG, "onError: ", error);
            }
        }, getApplicationContext(), "0CXU3SvpPXo6VRVuy0130NCV", "0HBq2p8wfFbDUFcs5QFRbrveKn8P6yBj");
    }

    private static final String TAG = "MyApplication";
}
