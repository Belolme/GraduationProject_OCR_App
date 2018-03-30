package com.billin.www.graduationproject_ocr;

import com.billin.www.graduationproject_ocr.module.bean.NormalWord;
import com.billin.www.graduationproject_ocr.module.bean.WordGeneralOCRResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void gsonTest() throws Exception {
        String json = "{" +
                "\"log_id\": 2471272194, " +
                "\"words_result_num\": 2," +
                "\"words_result\": " +
                "    [" +
                "        {\"words\": \" TSINGTAO\"}, " +
                "        {\"words\": \"青島睥酒\"}" +
                "    ]" +
                "}";

        Gson gson = new Gson();
        WordGeneralOCRResult<NormalWord> res = gson.fromJson(
                json, new TypeToken<WordGeneralOCRResult<NormalWord>>() {
                }.getType());

        System.out.println(res);
    }
}