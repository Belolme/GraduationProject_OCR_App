package com.billin.www.graduationproject_ocr.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 把这个 Activity 的声明周期绑定到 presenter 中，此时 BaseMVPView 相当于对 BaseView 的进一步封装
 * <p>
 * Created by Billin on 2018/3/31.
 */
public abstract class BaseMVPView<V extends BaseMVPView<V, P>, P extends BasePresenter<V>>
        extends BaseActivity implements BaseView<P> {

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        mPresenter = setupPresenter();
        mPresenter.setView((V) this);

        mPresenter.onStart();
    }

    /**
     * 必须在这个方法实现 {@link #setContentView} 方法
     */
    protected abstract void initView();

    protected abstract P setupPresenter();

    protected P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPresenter() != null)
            getPresenter().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null)
            getPresenter().onDestroy();

        recycle();
    }

    protected void recycle() {
        mPresenter = null;
    }
}
