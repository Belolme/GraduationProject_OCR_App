package com.billin.www.graduationproject_ocr.confirm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.billin.www.graduationproject_ocr.R;
import com.billin.www.graduationproject_ocr.view.CropImageView;

/**
 * 图片边距裁剪与显示，图片质量调整
 * <p>
 * Created by Billin on 2018/4/14.
 */
public class ConfirmView extends ConfirmPictureContract.View {

    public static final String KEY_FILE_PATH = "KEY FILE PATH";

    private CropImageView mImageView;

    private ImageView mCheckView;

    private ProgressBar mProgressBar;

    private Toolbar mToolbar;

    private ProgressDialog mLoadingDialog;

    public static void go(Context context, String filePath) {
        Intent intent = new Intent(context, ConfirmView.class);
        intent.putExtra(KEY_FILE_PATH, filePath);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_confirm);

        mToolbar = findViewById(R.id.confirm_toolbar);
        mCheckView = findViewById(R.id.confirm_check);
        mImageView = findViewById(R.id.confirm_image);
        mProgressBar = findViewById(R.id.confirm_progress);

        // 设置 toolbar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().cancel();
            }
        });
    }

    @Override
    protected ConfirmPictureContract.Presenter setupPresenter() {
        return new ConfirmPresenter();
    }

    @Override
    void showImage(final String filePath) {
        // 这里一定要用 setImageBitmap 方法，因为 setDrawable 方法生成的 Drawable 和
        // 图片的尺寸不一致
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    void showLoading(boolean show) {
        if (mLoadingDialog != null && show) {
            return;
        } else if (!show && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            return;
        }

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage("Loading...");
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.show();
    }

    @Override
    PointF[] getQuadrilateral() {
        return mImageView.getPointsInImage();
    }

    @Override
    void setQuadrilateralInImage(PointF[] points) {
        mImageView.setPointInImage(points);
    }
}