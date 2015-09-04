/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for ParallaxScrollView
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 */
package net.soulwolf.widget.parallaxrefresh;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * author: Soulwolf Created on 2015/9/4 18:46.
 * email : Ching.Soulwolf@gmail.com
 */
class ObjectAnimator extends Animation {

    private View mTarget;
    private float  mTargetValue;
    private float mTranslationY;

    ObjectAnimator(View target,float value){
        this.mTarget = target;
        this.mTargetValue = value;
        mTranslationY = ViewCompat.getTranslationY(target);
    }

    public static Animation ofTranslationY(View target,float value){
        return new ObjectAnimator(target,value);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        ViewCompat.setTranslationY(mTarget,mTargetValue - (mTargetValue - mTranslationY) * (1.0f - interpolatedTime));
    }

    @Override
    public void start() {
        mTarget.startAnimation(this);
    }
}
