package com.example.hh.project1.MyGame2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.view.GameView;
import com.example.hh.project1.R;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView tv_mainActivity_target;
    private TextView tv_mainAcitivity_score;
    private TextView tv_mainActivity_record;

    private static MainActivity mActivity;
    private Button bt_mainActivity_revrese;
    private Button bt_mainActivity_restart;
    private Button bt_mainActivity_option;
    private GameView gv;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;


        RelativeLayout rl_mainactivity_content = (RelativeLayout) findViewById(R.id.rl_mainactivity_content);
        gv = new GameView(this);

        rl_mainactivity_content.addView(gv);

        //初始化button
        bt_mainActivity_revrese = (Button) findViewById(R.id.bt_mainActivity_revrese);
        bt_mainActivity_restart = (Button) findViewById(R.id.bt_mainActivity_restart);
        bt_mainActivity_option = (Button) findViewById(R.id.bt_mainActivity_option);

        //初始化显示分数的textView
        tv_mainActivity_target = (TextView) findViewById(R.id.tv_mainActivity_target);
        tv_mainAcitivity_score = (TextView) findViewById(R.id.tv_mainActivity_score);
        tv_mainActivity_record = (TextView) findViewById(R.id.tv_mainActivity_record);



        //注册button
        bt_mainActivity_revrese.setOnClickListener(this);
        bt_mainActivity_restart.setOnClickListener(this);
        bt_mainActivity_option.setOnClickListener(this);



        application = (MyApplication) getApplication();

        tv_mainActivity_target.setText(application.getTarget()+"");
        tv_mainActivity_record.setText(application.getHighestRecord()+"");

    }
    public static  MainActivity getActivity(){
        Log.i("slfjl","mainActivity 执行了");

        return mActivity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_mainActivity_revrese:
                reverse();
                break;
            case R.id.bt_mainActivity_restart:
                restart();
                break;
            case R.id.bt_mainActivity_option:
                option();
                break;
        }
    }

    private void option() {
        startActivityForResult(new Intent(this, OptionsActivity.class), 100);
    }

    private void restart() {
        new AlertDialog.Builder(this)
                .setTitle("确认")
                .setMessage("亲，真的要重新开始吗")
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gv.restart();

                    }
                })
                .setNegativeButton("不要啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    private void reverse() {
        gv.revert();
    }


    public void updateCurrentScore(int score){

        tv_mainAcitivity_score.setText(score + "");

    }

    public void updateHighestScore(int score){

        tv_mainActivity_record.setText(score+"");

    }

    public void updateTargetScore(int score){

        tv_mainActivity_target.setText(score+"");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateTargetScore(application.getTarget());
        tv_mainActivity_target.setTextColor(0xFFFFFF);
        switch (application.getTarget()) {

            case 1024:
                tv_mainActivity_target.setBackgroundResource(R.mipmap.zuanshi);
                break;
            case 2048:
                tv_mainActivity_target.setBackgroundResource(R.mipmap.dashi);
                break;
            case 4096:
                tv_mainActivity_target.setBackgroundResource(R.mipmap.zhenwangzhe);
                break;
        }
        gv.restart();
    }

    /*public void showOption(View v){
        Intent intent = new Intent(this,OptionsActivity.class);
        intent.putExtra("test","你好,man");
        startActivity(intent);
    }*/
    /*GridLayout gl = (GridLayout) findViewById(R.id.gd);

        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                NumberItem item = new NumberItem(this,0);
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display defaultDisplay = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                defaultDisplay.getMetrics(metrics);
                int widthPixels = metrics.widthPixels;

                gl.addView(item,widthPixels/4,widthPixels/4);
            }
        }*/
}
