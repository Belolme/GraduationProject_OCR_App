package com.billin.www.graduationproject_ocr;

        import com.baidu.ocr.sdk.model.AccessToken;

/**
 * 保存 ocr 初始化的一些变量
 * <p>
 * Created by Billin on 2018/4/14.
 */
public class OCRState {

    public static final String AK = "0CXU3SvpPXo6VRVuy0130NCV";

    public static final String SK = "0HBq2p8wfFbDUFcs5QFRbrveKn8P6yBj";

    private static AccessToken mAccessToken;

    public static AccessToken getAccessToken() {
        return mAccessToken;
    }

    public static void setAccessToken(AccessToken accessToken) {
        mAccessToken = accessToken;
    }
}
