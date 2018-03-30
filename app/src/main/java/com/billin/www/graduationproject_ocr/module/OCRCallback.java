package com.billin.www.graduationproject_ocr.module;

import com.baidu.ocr.sdk.exception.OCRError;

/**
 * 通用的 OCRCallback
 * <p>
 * Created by billin on 2018/3/30.
 */
public interface OCRCallback<T> {

    void onResult(T data);

    void onError(OCRError error);
}
