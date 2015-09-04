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
import android.widget.AbsListView;
import android.widget.ListView;

import net.soulwolf.widget.parallaxrefresh.ParallaxScrollObserver;
import net.soulwolf.widget.parallaxrefresh.event.ParallaxScrollCallback;

/**
 * author: Soulwolf Created on 2015/9/4 17:03.
 * email : Ching.Soulwolf@gmail.com
 */
public class ParallaxListView extends ListView implements ParallaxScrollObserver, AbsListView.OnScrollListener {

    private ParallaxScrollCallback mParallaxScrollCallback;
    private int mScrollY;
    private OnScrollListener mDelegateOnScrollListener;

    public ParallaxListView(Context context) {
        super(context);
        initialize();
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        super.setOnScrollListener(this);
    }

    @Override
    public void setScrollCallback(@NonNull ParallaxScrollCallback callback) {
        this.mParallaxScrollCallback = callback;
    }

    @Override
    public void setPlaceholder(@NonNull View view) {
        removeHeaderView(view);
        addHeaderView(view);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.mDelegateOnScrollListener = l;
    }

    private int getRealScrollY() {
        View c = getChildCount() > 0 ? getChildAt(0) : null;
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mDelegateOnScrollListener != null){
            mDelegateOnScrollListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int realScrollY = getRealScrollY();
        if(mParallaxScrollCallback != null && realScrollY != mScrollY){
            mParallaxScrollCallback.onParallaxScrollChanged(0,realScrollY,true);
            mScrollY = realScrollY;
        }
        if(mDelegateOnScrollListener != null){
            mDelegateOnScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }
    }
}
