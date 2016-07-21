package io.gank.gank.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.gank.R;
import io.gank.gank.utils.NetWorkUtil;
import io.gank.gank.utils.SnackBarUtil;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.iv_start)
    ImageView iv_start;
    @Bind(R.id.tv_start)
    TextView tv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        initImage();
    }

    private void initImage() {
        File dir = getFilesDir();
        final File imgFile = new File(dir, "splash.png");
        if(NetWorkUtil.isNetWorkConnected(this)){
            iv_start.setImageResource(R.mipmap.splash);
            tv_start.setText("干货集中营");
            //RequestImage.loadSplash(Menu.URL_Splash,iv_start,tv_start,imgFile);
        }else{
            SnackBarUtil.ShortSnackBar(iv_start,"当前无网络连接，请连接网络！.. ( ＞ω＜)", SnackBarUtil.COLOR_PINK, SnackBarUtil.COLOR_GREEN).show();
            if (imgFile.exists()) {
                iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                tv_start.setText("干货集中营");
            } else {
                iv_start.setImageResource(R.mipmap.splash);
                tv_start.setText("干货集中营");
            }
        }
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnim);
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }
}
