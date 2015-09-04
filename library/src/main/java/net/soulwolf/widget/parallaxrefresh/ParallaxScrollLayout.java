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
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import net.soulwolf.widget.parallaxrefresh.event.OnRefreshListener;
import net.soulwolf.widget.parallaxrefresh.event.ParallaxScrollCallback;

/**
 * author: Soulwolf Created on 2015/9/4 12:53.
 * email : Ching.Soulwolf@gmail.com
 */
public class ParallaxScrollLayout extends FrameLayout implements ParallaxScrollCallback {

    static DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(10);
    static final Interpolator ROLLBACK_INTERPOLATOR = new DecelerateInterpolator();
    static final long         ROLLBACK_DURATION     = 300;
    static final long         AUTO_REFRESH_DURATION = 400;

    private IParallaxHolder mParallaxHolder;
    private ParallaxMode mParallaxMode = ParallaxMode.PARALLAX_MODE_SCROLL;
    private LayoutInflater mLayoutInflater;
    private View mParallaxTarget;
    private View mPlaceholderView;
    private OnRefreshListener mOnRefreshListener;
    private float mRefreshRatio = .6f;

    private float mTouchStartY;
    private float mCurrentY;
    private int mTouchSlop;

    public ParallaxScrollLayout(Context context) {
        this(context, null);
    }

    public ParallaxScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mLayoutInflater = LayoutInflater.from(context);
        applyAttributeSet(context, attrs, defStyleAttr);
    }

    private void applyAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxScrollLayout, defStyleAttr, 0);
            mParallaxMode = ParallaxMode.ensure(a.getInt(R.styleable.ParallaxScrollLayout_psvParallaxMode,0));
            a.recycle();
        }
    }

    /** Set up the head the View scroll parallax processor */
    public void setParallaxHolder(@NonNull IParallaxHolder holder){
        if(mParallaxHolder != null){
            mParallaxHolder.onDestroy();
            mParallaxHolder = null;
            mPlaceholderView = null;
        }
        mParallaxHolder = holder;
        mParallaxHolder.onCreate(getContext());
        mParallaxHolder.setParallaxMode(mParallaxMode);
        View view = mParallaxHolder.onCreateView(mLayoutInflater, this);
        // Attach to root view!
        addView(view, 0);
        mParallaxHolder.onViewCreated(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for(int i= 0;i< childCount ;i++){
            View view = getChildAt(i);
            if(view instanceof ParallaxScrollObserver){
                mParallaxTarget = view;
                break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(tryParallaxHolder()){
            View contentView = mParallaxHolder.getContentView();
            int width = contentView.getMeasuredWidth();
            int height = contentView.getMeasuredHeight();
            mParallaxHolder.onMeasured(width,height);
            ensurePlaceholderView();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(tryParallaxHolder()){
            View contentView = mParallaxHolder.getContentView();
            MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();
            int l = getPaddingLeft() + params.leftMargin;
            int t = getPaddingTop() + params.topMargin - mParallaxHolder.getParallaxTop();
            contentView.layout(l, t, l + contentView.getMeasuredWidth(), t + contentView.getMeasuredHeight());
        }
    }

    private void ensurePlaceholderView(){
        if(mParallaxTarget != null && mPlaceholderView == null){
            // To avoid repetition and add PlaceholderView
            ViewGroup targetView = (ViewGroup) mParallaxTarget;
            View view = targetView.findViewById(R.id.psvPlaceholderView);
            if(view != null){
                targetView.removeView(view);
            }
            int width = mParallaxHolder.getRealWidth();
            int height = getOriginalHeight();
            ViewGroup.LayoutParams params = generatePlaceholderLayoutParams(mParallaxTarget,width,height);
            // Due to system {@link android.widget.Space} minimum compatible to API 14,
            // the library minimum support API 9, so rewrite the View
            mPlaceholderView = new PlaceholderView(getContext());
            mPlaceholderView.setLayoutParams(params);
            mPlaceholderView.setId(R.id.psvPlaceholderView);
            ParallaxScrollObserver observer = (ParallaxScrollObserver) mParallaxTarget;
            observer.setPlaceholder(mPlaceholderView);
            observer.setScrollCallback(this);
        }
    }

    private ViewGroup.LayoutParams generatePlaceholderLayoutParams(@NonNull View observer,int width,int height){
        return observer instanceof AbsListView ? new AbsListView.LayoutParams(width,height) : new ViewGroup.LayoutParams(width,height);
    }

    private int getOriginalHeight(){
        return tryParallaxHolder() ? mParallaxHolder.getRealHeight()
                - mParallaxHolder.getParallaxTop()
                - mParallaxHolder.getParallaxBottom() : 0;
    }

    private int getMaxPlaceholderHeight(){
        return tryParallaxHolder() ? mParallaxHolder.getRealHeight()
                - mParallaxHolder.getParallaxTop() : 0;
    }

    private int getMaxScrollY(){
        return getMaxPlaceholderHeight() - getOriginalHeight();
    }

    private boolean tryParallaxHolder(){
        return mParallaxHolder != null;
    }

    @Override
    public void onParallaxScrollChanged(int scrollX, int scrollY, boolean isTouchEvent) {
        if(tryParallaxHolder()){
            mParallaxHolder.onScrollChanged(scrollX,scrollY,isTouchEvent);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartY = e.getY();
                mCurrentY = mTouchStartY;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = e.getY();
                float dy = currentY - mTouchStartY;
                if (dy > mTouchSlop && !Utils.canChildScrollUp(mParallaxTarget)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurrentY = e.getY();
                float dy = Utils.constrains(mCurrentY - mTouchStartY, 0, getMaxScrollY() * 2);
                if(mParallaxTarget != null){
                    float translationY = DECELERATE_INTERPOLATOR.getInterpolation(dy / getMaxScrollY() / 2) * dy / 2;
                    ViewCompat.setTranslationY(mParallaxTarget,translationY);
                    onParallaxScrollChanged(0, (int) -translationY,true);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                rollback();
                return true;
            default:
                return super.onTouchEvent(e);
        }
    }

    /** When raised his finger roll back to in situ */
    private void rollback() {
        if(tryParallaxHolder()){
            mParallaxHolder.onRollback();
        }
        if(hasPlaceReset()){
            RollbackAnimation animation = new RollbackAnimation(mParallaxTarget,
                    0,getMaxScrollY());
            animation.setInterpolator(ROLLBACK_INTERPOLATOR);
            animation.setDuration(ROLLBACK_DURATION);
            animation.start();
        }
    }

    private void onRefresh(){
        if(mOnRefreshListener != null){
            mOnRefreshListener.onRefresh();
        }
    }

    private boolean hasPlaceReset(){
        return tryParallaxHolder() && ViewCompat.getTranslationY(mParallaxTarget) > 0;
    }

    public void autoRefresh(){
        if(tryParallaxHolder() && mParallaxTarget != null){
            AutoRefreshAnimation animation = new AutoRefreshAnimation(mParallaxTarget,getMaxScrollY());
            animation.setInterpolator(ROLLBACK_INTERPOLATOR);
            animation.setDuration(AUTO_REFRESH_DURATION);
            animation.start();
        }
    }

    class RollbackAnimation extends Animation implements Animation.AnimationListener {

        View mTargetView;
        float  mTargetHeight;
        float  mCurrentHeight;
        boolean isNeedRefresh = false;

        @Override
        public void start() {
            mTargetView.startAnimation(this);
        }

        RollbackAnimation(View view,float targetHeight,int maxHeight){
            this.mTargetView = view;
            this.mTargetHeight = targetHeight;
            this.mCurrentHeight = ViewCompat.getTranslationY(view);
            this.isNeedRefresh = mCurrentHeight - mTargetHeight > (maxHeight - mTargetHeight) * mRefreshRatio;
            super.setAnimationListener(this);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ViewCompat.setTranslationY(mTargetView,(mTargetHeight - (mTargetHeight - mCurrentHeight)
                    * (1.0f - interpolatedTime)));
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isNeedRefresh){
                onRefresh();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class AutoRefreshAnimation extends Animation implements Animation.AnimationListener {

        View mTargetView;
        float mTargetValue;
        float mCurrentValue;

        @Override
        public void start() {
            mTargetView.startAnimation(this);
        }

        AutoRefreshAnimation(View target,float targetValue){
            this.mTargetView = target;
            this.mTargetValue = targetValue;
            this.mCurrentValue = ViewCompat.getTranslationY(target);
            super.setAnimationListener(this);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float translationY = mTargetValue - (mTargetValue - mCurrentValue)
                    * (1.0f - interpolatedTime);
            ViewCompat.setTranslationY(mTargetView,translationY);
            onParallaxScrollChanged(0, (int) -translationY,false);
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rollback();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public void setRefreshRatio(float refreshRatio) {
        if(refreshRatio < .0f || refreshRatio > 1.0f){
            throw new IllegalArgumentException("The refresh ratio only between 0.0 f to 1.0 f value");
        }
        this.mRefreshRatio = refreshRatio;
    }
}
