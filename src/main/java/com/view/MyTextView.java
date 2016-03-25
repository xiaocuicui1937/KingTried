package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hh on 2016/3/21.
 */
//自定义textView是用来给textview添加一个边框的
public class MyTextView extends TextView {
    //自己new出控件的实例的时候会调用这个函数
    public MyTextView(Context context) {
        super(context);

    }
    //这个是用来给系统调用的 系统通过该构造方法实例化控件的时候，会把xml里定义的属性一起传进来，供控件使用
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //创建一个画笔
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        //实体
        paint.setStyle(Paint.Style.STROKE);
        //该控件必须调用过onMeasure方法之后才能够获得正确的宽和高的值
        canvas.drawRect(1,1,getMeasuredWidth(),getMeasuredHeight(),paint);

        //调用父类的画图方法将，将控件中内容画出来
        super.onDraw(canvas);

    }
}
