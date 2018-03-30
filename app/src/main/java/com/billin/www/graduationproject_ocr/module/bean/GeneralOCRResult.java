package com.billin.www.graduationproject_ocr.module.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 最最顶层的返回结果 bean 类，仅仅有 logId
 * <p>
 * Created by billin on 2018/3/30.
 */
public class GeneralOCRResult {

    @SerializedName("log_id")
    private long logId;

    public GeneralOCRResult(long logId) {
        this.logId = logId;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralOCRResult that = (GeneralOCRResult) o;

        return logId == that.logId;
    }

    @Override
    public int hashCode() {
        return (int) (logId ^ (logId >>> 32));
    }

    @Override
    public String toString() {
        return "GeneralOCRResult{" +
                "logId=" + logId +
                '}';
    }
}
