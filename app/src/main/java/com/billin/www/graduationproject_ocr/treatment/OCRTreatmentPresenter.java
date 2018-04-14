package com.billin.www.graduationproject_ocr.treatment;

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


    /*
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
                });*/
    }

    @Override
    void confirm(String res) {

    }

    @Override
    void cancel() {

    }

    @Override
    void clickCharPos(int pos) {

    }
}
