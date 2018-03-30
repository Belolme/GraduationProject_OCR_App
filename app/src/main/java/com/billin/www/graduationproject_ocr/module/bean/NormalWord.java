package com.billin.www.graduationproject_ocr.module.bean;

/**
 * 通用文字普通版的 data 字段
 * <p>
 * Created by billin on 2018/3/30.
 */
public class NormalWord {

    private String words;

    public NormalWord(String words) {
        this.words = words;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NormalWord that = (NormalWord) o;

        return words != null ? words.equals(that.words) : that.words == null;
    }

    @Override
    public int hashCode() {
        return words != null ? words.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NormalWord{" +
                "words='" + words + '\'' +
                '}';
    }
}
