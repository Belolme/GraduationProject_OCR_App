package com.billin.www.graduationproject_ocr.module.callback;

import com.baidu.ocr.sdk.exception.OCRError;

/**
 * 通用的 OCRCallback
 * <p>
 * Created by billin on 2018/3/30.
 */
public interface OCRCallback<T> extends GeneralCallback<T, OCRError> {
}
