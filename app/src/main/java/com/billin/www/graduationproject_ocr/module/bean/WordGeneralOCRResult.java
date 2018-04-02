package com.billin.www.graduationproject_ocr.module.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 包含文字图片的结果返回类型
 * <p>
 * Created by billin on 2018/3/30.
 */
public class WordGeneralOCRResult<T> extends GeneralOCRResult implements Serializable {

    @SerializedName("direction")
    private int direction;

    @SerializedName("words_result_num")
    private long wordsResultNum;

    @SerializedName("words_result")
    private List<T> wordsResult;

    public WordGeneralOCRResult(long logId) {
        super(logId);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public long getWordsResultNum() {
        return wordsResultNum;
    }

    public void setWordsResultNum(long wordsResultNum) {
        this.wordsResultNum = wordsResultNum;
    }

    public List<T> getWordsResult() {
        return wordsResult;
    }

    public void setWordsResult(List<T> wordsResult) {
        this.wordsResult = wordsResult;
    }

    @Override
    public String toString() {
        return "WordGeneralOCRResult{" +
                "direction=" + direction +
                ", wordsResultNum=" + wordsResultNum +
                ", wordsResult=" + wordsResult +
                '}';
    }
}
