/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for Android-ParallaxRefresh
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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.soulwolf.widget.parallaxrefresh.ParallaxScrollLayout;
import net.soulwolf.widget.parallaxrefresh.event.OnRefreshListener;

/**
 * author: Soulwolf Created on 2015/9/18 23:49.
 * email : Ching.Soulwolf@gmail.com
 */
public class ParallaxFragment extends Fragment implements OnRefreshListener {

    ParallaxScrollLayout mParallaxScrollLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParallaxScrollLayout = (ParallaxScrollLayout) view.findViewById(R.id.parallax);
        mParallaxScrollLayout.setParallaxHolder(new SimpleParallaxHolder(R.mipmap.header3));
        mParallaxScrollLayout.setRefreshRatio(.8f);
        mParallaxScrollLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "onRefresh", Toast.LENGTH_SHORT).show();
    }
}
