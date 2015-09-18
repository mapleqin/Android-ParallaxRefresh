package net.soulwolf.widget.parallaxrefresh.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    public void onScrollViewClick(View view){
        startActivity(new Intent(this, ParallaxScrollViewActivity.class));
    }

    public void onListViewClick(View view){
        startActivity(new Intent(this,ParallaxListViewActivity.class));
    }

    public void onFragmentClick(View view){
        startActivity(new Intent(this,SimpleFragmentActivity.class));
    }
}
