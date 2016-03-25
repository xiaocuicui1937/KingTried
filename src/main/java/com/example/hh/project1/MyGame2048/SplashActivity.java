package com.example.hh.project1.MyGame2048;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hh.project1.R;

public class SplashActivity extends ActionBarActivity {

    private ImageView iv_splashActivity_firstpic;
    private TextView tv_splashActivity_firsttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //iv_splashActivity_firstpic = (ImageView) findViewById(R.id.iv_splashActivity_firstpic);
        //tv_splashActivity_firsttext = (TextView) findViewById(R.id.tv_splashActivity_firsttext);

        //tv_splashActivity_firsttext.setText("王者试炼");

        /*//使用颜色表示法显示图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        //将传入的图片与设置关联起来
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.l3, options);
        iv_splashActivity_firstpic.setImageBitmap(bitmap);
        //使用blur实现图片的毛玻璃效果
        int scaleRatio = 50;
        int blurRadius = 8;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                bitmap.getWidth() / scaleRatio,
                bitmap.getHeight() / scaleRatio,
                false);
        Bitmap blurBitmap = FastBlur.doBlur(scaledBitmap, blurRadius, true);
        iv_splashActivity_firstpic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv_splashActivity_firstpic.setImageBitmap(blurBitmap);*/


        //创建一个线程在当前页面停留一会然后跳转到，游戏主页面
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //系统提供一个api用来相当于使用handler message机制，只不过这个api已经封装实现了
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        }.start();


        /*//初始化广告
        AdManager.getInstance(this).init("d035c27d7f1990db", "e4e83362229c99e3", true);

        //在当前页面加入插屏广告
        SpotManager.getInstance(  this).loadSpotAds();
        SpotManager.getInstance(  this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
        SpotManager.getInstance(  this).setAnimationType(SpotManager.ANIM_ADVANCE);
        SpotManager.getInstance(  this).showSpotAds(this);

        //插屏监听接口（可选）在这里可以监听到用户一些操作

        SpotManager.getInstance(  this).showSpotAds(this, new SpotDialogListener() {
            @Override
            public void onShowSuccess() {
                Log.i("SplashActivity","onShowSuccess");
            }

            @Override
            public void onShowFailed() {
                Log.i("SplashActivity","onShowFailed");

            }

            @Override
            public void onSpotClosed() {
                Log.i("SplashActivity","onSpotClosed");

            }

            @Override
            public void onSpotClick(boolean b) {
                Log.i("SplashActivity","onSpotClick");

            }
        });
    }


    //当用户点击手机的back键会调用该函数
    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        // 如果有需要，可以点击后退关闭插播广告。
        if (!SpotManager.getInstance(  this).disMiss()) {
            // 弹出退出窗口，可以使用自定义退屏弹出和回退动画,参照demo,若不使用动画，传入-1
            super.onBackPressed();
        }
        Log.i("splash", "onBackPressed");
    }

    @Override
    protected void onDestroy() {
        SpotManager.getInstance(this).onDestroy();
        super.onDestroy();
    }*/
    }
}
