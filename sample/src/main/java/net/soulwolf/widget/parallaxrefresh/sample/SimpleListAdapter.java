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

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * author: Soulwolf Created on 2015/9/4 22:38.
 * email : Ching.Soulwolf@gmail.com
 */
public class SimpleListAdapter extends BaseAdapter {

    @DrawableRes int [] mArray;
    Context mContext;

    public SimpleListAdapter(Context context,@DrawableRes int ... drawableRes){
        this.mArray = drawableRes;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mArray.length;
    }

    @Override
    public Integer getItem(int position) {
        return mArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item,parent,false);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mImageView.setImageResource(mArray[position]);
        return convertView;
    }

    class ViewHolder{
        ImageView mImageView;
    }
}
