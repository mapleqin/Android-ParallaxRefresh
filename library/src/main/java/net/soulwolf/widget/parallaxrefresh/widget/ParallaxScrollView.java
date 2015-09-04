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
package net.soulwolf.widget.parallaxrefresh.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import net.soulwolf.widget.parallaxrefresh.ParallaxScrollObserver;
import net.soulwolf.widget.parallaxrefresh.event.ParallaxScrollCallback;

/**
 * author: Soulwolf Created on 2015/9/4 16:06.
 * email : Ching.Soulwolf@gmail.com
 */
public class ParallaxScrollView extends ScrollView implements ParallaxScrollObserver{

    private ParallaxScrollCallback mParallaxScrollCallback;
    private ViewGroup mOriginalContainer;
    private View mPlaceholderView;
    private RelativeLayout mRootContainer;

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureRootContainer();
    }

    private void ensureRootContainer(){
        if(mOriginalContainer == null && getChildCount() > 0){
            mOriginalContainer = (ViewGroup) getChildAt(0);
        }
        if(mPlaceholderView != null){
            ensurePlaceholder(mPlaceholderView);
        }
    }

    @Override
    public void setScrollCallback(@NonNull ParallaxScrollCallback callback) {
        this.mParallaxScrollCallback = callback;
    }

    @Override
    public void setPlaceholder(@NonNull View view) {
        if(mOriginalContainer != null){
            ensurePlaceholder(view);
        }else {
            mPlaceholderView = view;
        }
    }

    private void ensurePlaceholder(@NonNull View view){
        if(mOriginalContainer != null){
            if(mRootContainer != null){
                mRootContainer.removeAllViews();
                removeView(mRootContainer);
            }
            ViewGroup.LayoutParams params = mOriginalContainer.getLayoutParams();
            removeView(mOriginalContainer);
            mRootContainer = new RelativeLayout(getContext());
            addView(mRootContainer, params);
            // Add placeholder view to root layout;
            mRootContainer.addView(view);

            // Create a RelativeLayout layout params!
            RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(params);
            containerParams.addRule(RelativeLayout.BELOW,view.getId());

            mRootContainer.addView(mOriginalContainer, containerParams);
            mPlaceholderView = null;
        }
    }

    @Override
    public void addView(@NonNull View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ParallaxScrollView can host only one direct child");
        }
        super.addView(child);
        ensureRootContainer();
    }

    @Override
    public void addView(@NonNull View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ParallaxScrollView can host only one direct child");
        }
        super.addView(child, index);
        ensureRootContainer();
    }

    @Override
    public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ParallaxScrollView can host only one direct child");
        }
        super.addView(child, params);
        ensureRootContainer();
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ParallaxScrollView can host only one direct child");
        }
        super.addView(child, index, params);
        ensureRootContainer();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mParallaxScrollCallback != null){
            mParallaxScrollCallback.onParallaxScrollChanged(l,t,false);
        }
    }
}
