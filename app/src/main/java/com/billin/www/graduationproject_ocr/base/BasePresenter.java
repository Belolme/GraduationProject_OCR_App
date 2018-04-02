package com.billin.www.graduationproject_ocr.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by billin on 16-9-3.
 * <p>
 * presenter layout interface
 * 每一个presenter都需要继承这一个接口
 */
public abstract class BasePresenter<V extends BaseView<?>> {

    private V mView;

    /**
     * 获取 MVP 架构与 P 层对应的 V 层。
     *
     * @return 与 P 层相对应的 V 层视图
     */
    public V getView() {
        return mView;
    }

    void setView(V mView) {
        this.mView = mView;
    }

    /**
     * 这个方法和 V 层的生命周期相绑定，
     * 当 V 层执行 {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} 方法时被调用。
     */
    abstract public void onStart();

    /**
     * 这一个方法和 V 层的生命周期绑定，
     * 当 V 层执行 {@link Fragment#onResume()} 方法时被调用。
     */
    abstract public void onResume();

    /**
     * 这个方法和 V 层的生命周期相绑定，
     * 当 V 层执行 {@link Fragment#onPause()} 方法时被调用。
     */
    abstract public void onPause();

    /**
     * 这个方法和 V 层的生命周期相绑定，
     * 当 V 层执行 {@link Fragment#onDestroy()} 方法时被调用。
     */
    abstract public void onDestroy();
}