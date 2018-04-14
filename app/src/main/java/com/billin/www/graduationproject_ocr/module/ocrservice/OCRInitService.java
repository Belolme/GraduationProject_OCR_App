package com.billin.www.graduationproject_ocr.module.ocrservice;

import android.content.Context;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.billin.www.graduationproject_ocr.OCRState;
import com.billin.www.graduationproject_ocr.module.callback.OCRCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * 用于初始化百度 ocr 的 Service
 * <p>
 * Created by Billin on 2018/4/14.
 */
public class OCRInitService {

    private static final String TAG = "OCRInitService";

    private static OCRInitService mInitService = new OCRInitService();

    private List<OCRCallback<AccessToken>> mListener;

    private OCRInitService() {
        mListener = new LinkedList<>();
    }

    public static OCRInitService getInstance() {
        return mInitService;
    }

    public void initService(Context context) {
        if (OCRState.getAccessToken() != null) {
            return;
        }

        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                Log.d(TAG, "OCR token is init");

                OCRState.setAccessToken(result);

                for (OCRCallback<AccessToken> callback : mListener) {
                    callback.onResult(result);
                }

                mListener.clear();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                for (OCRCallback<AccessToken> callback : mListener) {
                    callback.onError(error);
                }
            }
        }, context.getApplicationContext(), OCRState.AK, OCRState.SK);
    }

    public void addListener(OCRCallback<AccessToken> listener) {
        mListener.add(listener);
    }

    public void removeListener(OCRCallback<AccessToken> listener) {
        mListener.remove(listener);
    }
}
