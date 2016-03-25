package com.example.hh.project1.MyGame2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.hh.project1.R;

import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;

public class OptionsActivity extends ActionBarActivity implements View.OnClickListener {


    private Button bt_option_line;
    private Button  bt_option_target;
    private Button bt_option_back;
    private Button bt_option_down;
    private MyApplication app;

    private int line1;
    private int target1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        //初始化button
        bt_option_line = (Button) findViewById(R.id.bt_option_line);
        bt_option_target = (Button) findViewById(R.id.bt_option_target);
        bt_option_back = (Button) findViewById(R.id.bt_option_back);
        bt_option_down = (Button) findViewById(R.id.bt_option_down);

        //注册监听按钮
        bt_option_line.setOnClickListener(this);
        bt_option_target.setOnClickListener(this);
        bt_option_back.setOnClickListener(this);
        bt_option_down.setOnClickListener(this);

        app = (MyApplication) getApplication();
        line1 = app.getLineNumber();
        target1 = app.getTarget();

        //根据application获取sharepreference取得用户之前保存的设置
        bt_option_line.setText(line1+"");
        bt_option_target.setText(target1 + "");

        //实例化广告条
        AdView adView = new AdView(this);
        //获取要加载广告的布局对象
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
        //将广告条加入到布局中
        adLayout.addView(adView);

        adView.setAdListener(new AdViewListener() {
            @Override
            public void onReceivedAd(AdView adView) {
                Log.i("onReceivedAd","onReceivedAd is called");
            }

            @Override
            public void onSwitchedAd(AdView adView) {
                Log.i("onSwitchedAd","onSwitchedAd is called");
            }

            @Override
            public void onFailedToReceivedAd(AdView adView) {
                Log.i("onFailedToReceivedAd","onFailedToReceivedAd is called");
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_option_target:
                setTarget();
                break;
            case R.id.bt_option_back:
                finish();//直接销毁这个activity

                break;
            case R.id.bt_option_down:
                done();
                break;
            case R.id.bt_option_line:
                setLine();
                break;
        }
    }

    private void setLine() {
        final String[] items = {"4","5","6"};

        new AlertDialog.Builder(this)

                .setTitle("选择战场行列数")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        line1 = Integer.parseInt(items[which]);
                        bt_option_line.setText(line1 +"");
                        Log.i("line2", line1 +"");
                    }
                })
                .show();
    }

    private void done() {
        Log.i("done",line1+":"+target1);
        app.setLineNumber(line1);
        app.setTarget(target1);
        finish();
    }

    private void setTarget() {
        final String[] items = {"钻石小练:1024","大师熟路:2048","王者荣耀:4096"};

        new AlertDialog.Builder(this)
                .setTitle("改变目标分数")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = items[which].substring(5);
                        target1 = Integer.parseInt(item);
                        bt_option_target.setText(target1 + "");


                    }
                })
                .show();
    }
}
