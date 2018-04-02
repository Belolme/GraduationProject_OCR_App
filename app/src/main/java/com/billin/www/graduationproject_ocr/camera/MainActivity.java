package com.billin.www.graduationproject_ocr.camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.billin.www.graduationproject_ocr.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new CameraFragment())
                    .commit();
        }
    }
}
