package com.billin.www.graduationproject_ocr.treatment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.billin.www.graduationproject_ocr.R;

/**
 * 对 ocr 文本进行处理
 * <p>
 * Created by Billin on 2018/3/31.
 */
public class OCRTreatmentActivity extends OCRTreatmentContract.View {

    private static final String KEY_IMG = "OCR RESULT";

    private static final String OCR_RESULT_DATA_TYPE = "OCR_RESULT_DATA_TYPE";

    private EditText mTextView;

    private ImageView mPreviewView;

    private Toolbar mToolbar;

    private ShareActionProvider mShareActionProvider;

    public static void go(Context context, String imagePath) {
        Intent intent = new Intent(context, OCRTreatmentActivity.class);
        intent.putExtra(KEY_IMG, imagePath);
        context.startActivity(intent);
    }

    private void initPresenter() {
        Intent intent = getIntent();
        String imgPath = intent.getStringExtra(KEY_IMG);

        mPreviewView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        getPresenter().processOcrString(imgPath);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_ocr_result_process);
        mTextView = findViewById(R.id.ocr_process_edit_text);
        mPreviewView = findViewById(R.id.ocr_process_img_preview);
        mToolbar = findViewById(R.id.ocr_process_toolbar);

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
                finish();
            }
        });

        // 初始化 EditText
        mTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setShareIntent();
            }
        });

        initPresenter();
    }

    @Override
    protected OCRTreatmentPresenter setupPresenter() {
        return new OCRTreatmentPresenter();
    }

    @Override
    void showOcrString(String ocr) {
        mTextView.setText(ocr);
    }

    @Override
    void moveImageToLocation(int dx, int dy) {

    }

    @Override
    String getText() {
        return mTextView.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_ocr_treatment, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Get the ViewPager's current item position and set its ShareIntent.
        setShareIntent();

        // Return true to display menu
        return true;
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mTextView.getText().toString());
            mShareActionProvider.setShareIntent(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
