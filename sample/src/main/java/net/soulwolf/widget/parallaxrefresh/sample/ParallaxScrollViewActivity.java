package net.soulwolf.widget.parallaxrefresh.sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.soulwolf.widget.parallaxrefresh.ParallaxScrollLayout;
import net.soulwolf.widget.parallaxrefresh.event.OnRefreshListener;


public class ParallaxScrollViewActivity extends AppCompatActivity implements OnRefreshListener {

    ParallaxScrollLayout mParallaxScrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax_scroll_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.sample_primary);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(R.color.sample_primary);
        }
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mParallaxScrollLayout = (ParallaxScrollLayout) findViewById(R.id.parallax);
        mParallaxScrollLayout.setParallaxHolder(new SimpleParallaxHolder(R.mipmap.header1));
        mParallaxScrollLayout.setRefreshRatio(.8f);
        mParallaxScrollLayout.setOnRefreshListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parallax,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            mParallaxScrollLayout.autoRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this,"onRefresh",Toast.LENGTH_SHORT).show();
    }
}
