package com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hh.project1.R;

/**
 * Created by hh on 2016/3/21.
 */
public class NumberItem extends FrameLayout {

    private TextView mTv;
    private int number;
    public NumberItem(Context context) {
        super(context);
        initView(0);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(0);
    }
    public NumberItem(Context context,int number){
        super(context);
        initView(number);
    }

    private void initView(int number) {
        setBackgroundColor(0x35C8A015);//设置framLayout的背景颜色为灰色
        mTv = new TextView(getContext());
        //给帧布局设置宽和高 都是铺满父控件
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params.setMargins(5,5,5,5);//设置帧布局的外边距都是距离边缘5个像素，这里如果在代码中出现给控件
        //设置数字 若是没有单位 默认单位就是px
        mTv.setGravity(Gravity.CENTER);
        mTv.setTextSize(25);
        //mTv.setText(number+"");
        Log.i("number",number+"");
       setTextNumber(number);
        Log.i("initViewNumber",number+"");
        addView(mTv, params);
        this.number = number;//将传进来的number  对当前number进行赋值，以便于使用number参数
    }

    //向外界返回当前控件内保存的数据
    public int getNumber(){return  number;};

    public void setTextNumber(int num){
        //更改控件显示的数字的同时，应该把里面保存数字值的number同时更改

        number = num;
        //如果传入的数字为零的话，就显示空字符串
        //否则就显示相应的数字
        if (num==0){
            mTv.setText("");
        }else{
            mTv.setText(num+"");
            mTv.setTextColor(0x0000000);
        }

        //使用switch根据不同的数字，设置不同的背景显示颜色
        switch(num){
            case 0:
                mTv.setBackgroundColor(0x00000000);//设置为透名的颜色
                break;
            case 2:
                mTv.setBackgroundResource(R.mipmap.changjian);
                break;
            case 4:
                mTv.setBackgroundResource(R.mipmap.duolanjian);
                break;
            case 8:
                mTv.setBackgroundResource(R.mipmap.xixue);
                break;
            case 16:
                mTv.setBackgroundResource(R.mipmap.shiguang);
                break;
            case 32:
                mTv.setBackgroundResource(R.mipmap.bingchui);
                break;
            case 64:
                mTv.setBackgroundResource(R.mipmap.dajiangif);
                break;
            case 128:
                mTv.setBackgroundResource(R.mipmap.yinxue);
                break;
            case 256:
                mTv.setBackgroundResource(R.mipmap.yangdao);
                break;
            case 512:
                mTv.setBackgroundResource(R.mipmap.datianshi);
                break;
            case 1024:
                mTv.setBackgroundResource(R.mipmap.wujin);
                break;
            case 2048:
                mTv.setBackgroundResource(R.mipmap.maozi);
                break;
            case 4096:
                mTv.setBackgroundResource(R.mipmap.sanxiang);
                break;
        }
    }
}
