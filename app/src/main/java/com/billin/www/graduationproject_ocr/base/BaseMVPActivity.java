package com.billin.www.graduationproject_ocr.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 专用于 mvp 模式的 Activity, presenter 的声明周期需要显式在 view 中回调
 * <p>
 * Created by Billin on 2018/3/31.
 */
public abstract class BaseMVPActivity<V extends BaseView<P>, P extends BasePresenter<V>>
        extends AppCompatActivity {

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = setupPresenter();
        mPresenter.setView(setupView());

        mPresenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        recycle();
    }

    protected void recycle() {
        mPresenter = null;
    }

    protected abstract P setupPresenter();

    protected abstract V setupView();

    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 在界面中显示一个 {@link Toast}
     *
     * @param msg 需要 {@link Toast} 显示的信息
     */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
