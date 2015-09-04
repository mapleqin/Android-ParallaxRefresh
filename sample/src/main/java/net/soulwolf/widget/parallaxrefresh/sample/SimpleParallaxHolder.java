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
package net.soulwolf.widget.parallaxrefresh.sample;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.soulwolf.widget.parallaxrefresh.BaseParallaxHolder;

/**
 * author: Soulwolf Created on 2015/9/4 17:31.
 * email : Ching.Soulwolf@gmail.com
 */
public class SimpleParallaxHolder extends BaseParallaxHolder {

    private @DrawableRes int drawableRes;

    public SimpleParallaxHolder(@DrawableRes int drawableRes){
        this.drawableRes = drawableRes;
    }

    @Override
    public int getParallaxTop() {
        return (int) (getRealHeight() * .1f);
    }


    @Override
    public int getParallaxBottom() {
        return (int) (getRealHeight() * .4f);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container, boolean attachRoot) {
        return inflater.inflate(R.layout.simple_parallax_holder,container,attachRoot);
    }

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(drawableRes);
    }
}
