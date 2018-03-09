package com.billin.www.graduationproject_ocr.view;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.billin.www.graduationproject_ocr.R;

/**
 * Created by chris on 6/15/17.
 * <p/>
 * touch focus feedback view
 */
public class TouchFocusFeedbackView extends FrameLayout {

    private View viewCircle;

    private View viewCircleBorder;

    public TouchFocusFeedbackView(@NonNull Context context) {
        this(context, null);
    }

    public TouchFocusFeedbackView(@NonNull Context context,
                                  @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchFocusFeedbackView(@NonNull Context context,
                                  @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void animateTouchTarget(float x, float y) {
        setX(x - (getMeasuredWidth() / 2));
        setY(y - (getMeasuredHeight() / 2));

        viewCircle.setScaleX(0.5f);
        viewCircle.setScaleY(0.5f);
        viewCircle.setAlpha(1f);
        viewCircle.animate().cancel();

        viewCircleBorder.setScaleX(1.5f);
        viewCircleBorder.setScaleY(1.5f);
        viewCircleBorder.setAlpha(1f);
        viewCircleBorder.animate().cancel();

        viewCircle.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setListener(new SimpleAnimator() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewCircle.animate()
                                .alpha(0f)
                                .setListener(null)
                                .start();
                    }
                })
                .start();

        viewCircleBorder.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(0) // must reset delay with 0, because the time is replace 2000 below
                .setListener(new SimpleAnimator() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewCircleBorder.animate()
                                .alpha(0f)
                                .setStartDelay(2000)
                                .setListener(null)
                                .start();
                    }
                })
                .start();

    }

    private void init(Context context) {
        inflate(context, R.layout.touch_focus_feedback_view, this);
        viewCircle = findViewById(R.id.viewCircle);
        viewCircle.setAlpha(0f);
        viewCircleBorder = findViewById(R.id.viewCircleBorder);
        viewCircleBorder.setAlpha(0f);
    }

    public static abstract class SimpleAnimator implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}