package com.billin.www.graduationproject_ocr.base;

import android.util.Size;

/**
 * 记录各种卡片的尺寸大小
 * <p>
 * Created by billin on 2018/4/28.
 */
public enum CardSizeRatio {

    ID_CARD(new Size(86, 54));

    private Size ratio;

    CardSizeRatio(Size ratio) {
        this.ratio = ratio;
    }

    /**
     * 根据宽度获取高度
     */
    public int getHeight(int width) {
        return (int) (1f * width / ratio.getWidth() * ratio.getHeight());
    }

    /**
     * 根据宽度获取高度
     */
    public int getWidth(int height) {
        return (int) (1f * height / ratio.getHeight() * ratio.getWidth());
    }
}
