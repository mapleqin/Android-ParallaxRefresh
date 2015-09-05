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

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * author: Soulwolf Created on 2015/9/4 13:27.
 * email : Ching.Soulwolf@gmail.com
 */
public abstract class BaseParallaxHolder implements IParallaxHolder {

    static final float PARALLAX_RATIO = 5.0f;
    static final Interpolator ROLLBACK_INTERPOLATOR = new DecelerateInterpolator();
    static final long         ROLLBACK_DURATION     = 300;

    protected Context mContext;
    protected ParallaxMode mParallaxMode;
    private View mContentView;

    private int mRealWidth;
    private int mRealHeight;

    @Override
    public void onCreate(Context context) {
        this.mContext = context;
    }

    @Override
    public void onMeasured(int width, int height) {
        this.mRealWidth = width;
        this.mRealHeight = height;
    }

    @Override
    public int getRealWidth() {
        return mRealWidth;
    }

    @Override
    public int getRealHeight() {
        return mRealHeight;
    }

    @Override
    public final void setParallaxMode(@NonNull ParallaxMode mode) {
        this.mParallaxMode = mode;
    }

    @Override
    public void onScrollChanged(int scrollX, int scrollY, boolean isTouchEvent) {
        ViewCompat.setTranslationY(mContentView,Utils.minMax(-scrollY / PARALLAX_RATIO, 0, getParallaxTop()));
        if(mParallaxMode == ParallaxMode.PARALLAX_MODE_SCROLL
                && scrollY >= 0){
            ViewCompat.setTranslationY(mContentView,-scrollY);
        }
    }

    @Override
    public void onRollback() {
        if(ViewCompat.getTranslationY(mContentView) > 0){
            Animation animation = ObjectAnimator.ofTranslationY(mContentView,0);
            animation.setDuration(ROLLBACK_DURATION);
            animation.setInterpolator(ROLLBACK_INTERPOLATOR);
            animation.start();
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mContentView = null;
    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container) {
        mContentView = onCreateView(inflater,container,false);
        return mContentView;
    }

    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container,boolean attachRoot);

    protected final View findViewById(View container,@IdRes int id){
        return container == null ? null : container.findViewById(id);
    }

    protected final View findViewById(@IdRes int id){
        return findViewById(getContentView(),id);
    }

    @Override
    public View getContentView(){
        return mContentView;
    }
}
