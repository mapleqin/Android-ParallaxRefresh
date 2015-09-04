# Android-ParallaxRefresh
This is a drop-down head View supports parallax effect, support refresh libraries！ If you have any questions in the course or suggestions, please send an e-mail to the following e-mail, thank you!

For more information please see <a href='http://devsoulwolf.github.io/Android-ParallaxRefresh'>the website</a>

## Screenshots

![Sample](https://img.alicdn.com/imgextra/i3/1025192026/TB2pBIHeVXXXXb3XXXXXXXXXXXX_!!1025192026.gif)
![Sample](https://img.alicdn.com/imgextra/i4/1025192026/TB2yYAweVXXXXaIXpXXXXXXXXXX_!!1025192026.gif)


## Android-ParallaxRefresh with xml code
```xml
	// ListView
	<net.soulwolf.widget.parallaxrefresh.ParallaxScrollLayout
        android:id="@+id/parallax"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <net.soulwolf.widget.parallaxrefresh.widget.ParallaxListView
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </net.soulwolf.widget.parallaxrefresh.ParallaxScrollLayout>
	
	// ScrollView
	<net.soulwolf.widget.parallaxrefresh.ParallaxScrollLayout
        android:id="@+id/parallax"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <net.soulwolf.widget.parallaxrefresh.widget.ParallaxScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <FrameLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@android:color/white">

                <android.support.v7.widget.CardView xmlns:card_view="http://schemas.android.com/apk/res-auto"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="5dp"
                    card_view:cardBackgroundColor="@color/sample_primary"
                    card_view:cardCornerRadius="10dp">

                    <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:padding="@dimen/activity_horizontal_margin"
                        android:text="@string/card_text"
                        android:textColor="@android:color/white"
                        android:textSize="16sp" />

                </android.support.v7.widget.CardView>
            </FrameLayout>
        </net.soulwolf.widget.parallaxrefresh.widget.ParallaxScrollView>

    </net.soulwolf.widget.parallaxrefresh.ParallaxScrollLayout>	
```
## Android-ParallaxRefresh with java code
```java
	// ParallaxHolder
	public class SimpleParallaxHolder extends BaseParallaxHolder {
	
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
	    public void onCreate(Context context) {
	        super.onCreate(context);
	    }
	
	    @Override
	    public void onMeasured(int width, int height) {
	        super.onMeasured(width, height);
	    }
	
	    @Override
	    public int getRealWidth() {
	        return super.getRealWidth();
	    }
	
	    @Override
	    public int getRealHeight() {
	        return super.getRealHeight();
	    }
	
	    @Override
	    public void onScrollChanged(int scrollX, int scrollY, boolean isTouchEvent) {
	        super.onScrollChanged(scrollX, scrollY, isTouchEvent);
	    }
	
	    @Override
	    public void onRollback() {
	        super.onRollback();
	    }
	
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }
	
	    @Override
	    public View getContentView() {
	        return super.getContentView();
	    }
	}

	// ListView And ScrollView
	ParallaxScrollLayout mParallaxScrollLayout = (ParallaxScrollLayout) findViewById(R.id.parallax);
        mListView = (ListView) findViewById(R.id.list);
        mParallaxScrollLayout.setParallaxHolder(new SimpleParallaxHolder());
        mParallaxScrollLayout.setRefreshRatio(.8f);
        mParallaxScrollLayout.setOnRefreshListener(this);

	// OnRefreshListener
	OnRefreshListener listener = new OnRefreshListener(){

		public void onRefresh(){
			// refresh
		}

	};
```

## Attr params  info

<table>
	<tbody>
		<tr>
			<td><em>attrName</em></td>
			<td><em>Explanation</em></td>
		</tr>
		<tr>
			<td>psvParallaxMode</td>
			<td>This parameter indicates whether or not to follow the head View scroll through the list, the default scroll (scroll)</td>
		</tr>
	</tbody>
</table>

## Implementing View!

 <ul>
   	<li><a href='javascript:'>ParallaxScrollView</a></li>
	<li><a href='javascript:'>ParallaxListView</a></li>
 </ul>

## Custom
```java
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
```

## Maven
	<dependency>
  	    <groupId>net.soulwolf.widget</groupId>
		<url>https://dl.bintray.com/soulwolf/maven</url>
  	    <artifactId>parallaxRefresh</artifactId>
  	    <version>1.0.0</version>
	</dependency>
## Gradle
	allprojects {
       repositories {
          jcenter()
       }
	}
	
	compile 'net.soulwolf.widget:parallaxRefresh:1.0.0'

## Developed by
 Ching Soulwolf - <a href='javascript:'>Ching.Soulwolf@gmail.com</a>


## License
	Copyright 2015 Soulwolf Ching
	Copyright 2015 The Android Open Source Project for Android-ParallaxRefresh
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	

