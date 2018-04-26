package com.billin.www.graduationproject_ocr.treatment;

import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;

import com.baidu.ocr.sdk.exception.OCRError;
import com.billin.www.graduationproject_ocr.module.bean.NormalWord;
import com.billin.www.graduationproject_ocr.module.bean.WordGeneralOCRResult;
import com.billin.www.graduationproject_ocr.module.callback.OCRCallback;
import com.billin.www.graduationproject_ocr.module.ocrservice.RecognizeService;

import java.util.List;

/**
 * 对 OCR Result 进行处理
 * <p>
 * Created by Billin on 2018/3/31.
 */
public class OCRTreatmentPresenter extends OCRTreatmentContract.Presenter {

    private Class mType;

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    void processOcrString(String imgPath) {

        RecognizeService.recGeneralBasic(imgPath,
                new OCRCallback<WordGeneralOCRResult<NormalWord>>() {
                    @Override
                    public void onResult(WordGeneralOCRResult<NormalWord> data) {
                        List<NormalWord> wordList = data.getWordsResult();

                        StringBuilder stringBuilder = new StringBuilder();
                        for (NormalWord word : wordList) {
                            stringBuilder.append(word.getWords());
                        }

                        getView().showOcrString(stringBuilder.toString());
                    }

                    @Override
                    public void onError(OCRError error) {

                    }
                });
    }

    @Override
    void clickCharPos(int pos) {

    }
}
