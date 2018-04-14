package com.billin.www.graduationproject_ocr.treatment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public static void go(Context context, String imagePath) {
        Intent intent = new Intent(context, OCRTreatmentActivity.class);
        intent.putExtra(KEY_IMG, imagePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result_process);

        initView();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String imgPath = intent.getStringExtra(KEY_IMG);

        mPreviewView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        getPresenter().processOcrString(imgPath);
    }

    private void initView() {
        mTextView = findViewById(R.id.ocr_process_edit_text);
        mPreviewView = findViewById(R.id.ocr_process_img_preview);
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
}
