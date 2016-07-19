package io.gank.gank.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.gank.R;
import io.gank.gank.utils.ImageUtil;
import io.gank.gank.utils.SnackBarUtil;

public class GirlActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_big_girl)
    ImageView girlView;

    private String desc;
    private String url;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getExtra();//获取intent的数据
        initView();//初始化view
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_back);
        getSupportActionBar().setTitle(desc);
        //girlView
        Glide
                .with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        girlView.setImageBitmap(resource);
                        bitmap = resource;
                    }
                });
    }

    private void getExtra() {
        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        url = intent.getStringExtra("url");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_girl,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                SnackBarUtil.ShortSnackBar(girlView,"还没开发呢.. ( ＞ω＜)", SnackBarUtil.COLOR_PINK, SnackBarUtil.COLOR_GREEN).show();
                break;
            case R.id.action_save:
                ImageUtil.saveImage(this,url,bitmap,girlView,"save");
                break;
            case R.id.action_click_me:
                SnackBarUtil.ShortSnackBar(girlView,"还没开发呢.. ( ＞ω＜)", SnackBarUtil.COLOR_PINK, SnackBarUtil.COLOR_GREEN).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
