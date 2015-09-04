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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: Soulwolf Created on 2015/9/4 13:07.
 * email : Ching.Soulwolf@gmail.com
 */
public interface IParallaxHolder {

    public void onCreate(Context context);

    public View onCreateView(LayoutInflater inflater,ViewGroup container);

    public void onViewCreated(View view);

    public void onMeasured(int width,int height);

    public void onDestroy();

    public int getRealWidth();

    public int getRealHeight();

    public int getParallaxTop();

    public int getParallaxBottom();

    public void setParallaxMode(@NonNull ParallaxMode mode);

    public void onScrollChanged(int scrollX,int scrollY, boolean isTouchEvent);

    public void onRollback();

    public View getContentView();
}
