package com.billin.www.graduationproject_ocr.treatment;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.billin.www.graduationproject_ocr.R;
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
    void processOcrString(String imgPath, int style) {

        if (style == R.id.normal) {
            RecognizeService.recGeneralBasic(imgPath,
                    new OCRCallback<WordGeneralOCRResult<NormalWord>>() {
                        @Override
                        public void onResult(WordGeneralOCRResult<NormalWord> data) {
                            List<NormalWord> wordList = data.getWordsResult();

                            StringBuilder stringBuilder = new StringBuilder();
                            for (NormalWord word : wordList) {
                                stringBuilder.append(word.getWords()).append('\n');
                            }

                            getView().showOcrString(stringBuilder.toString());
                        }

                        @Override
                        public void onError(OCRError error) {

                        }
                    });
        } else if (style == R.id.id_card) {
            RecognizeService.recIDCard(imgPath, new OCRCallback<IDCardResult>() {
                @Override
                public void onResult(IDCardResult data) {
                    String s = "姓名：" + data.getName() + '\n' +
                            "性别：" + data.getGender() + '\n' +
                            "民族：" + data.getEthnic() + '\n' +
                            "出生日期：" + data.getBirthday() + '\n' +
                            "住址：" + data.getAddress() + '\n' +
                            "身份证号码：" + data.getIdNumber() + '\n';

                    getView().showOcrString(s);
                }

                @Override
                public void onError(OCRError error) {

                }
            });
        }
    }

    @Override
    void clickCharPos(int pos) {

    }
}
