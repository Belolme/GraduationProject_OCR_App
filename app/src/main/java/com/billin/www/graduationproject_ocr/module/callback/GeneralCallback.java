package com.billin.www.graduationproject_ocr.module.callback;

/**
 * 一个通用的 Callback, 所有的 Callback 都应当继承这一个接口
 * <p>
 * Created by Billin on 2018/4/14.
 */
public interface GeneralCallback<T, E> {

    void onResult(T data);

    void onError(E error);
}
