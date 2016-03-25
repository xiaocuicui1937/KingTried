package com.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.example.hh.project1.MyGame2048.MainActivity;
import com.example.hh.project1.MyGame2048.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hh on 2016/3/21.
 */
//GridLayout类似于tableLayout可以画出表格形式，正式是在android4.0之后引入的效率比较高
public class GameView extends GridLayout {
    private String TAG = "GameView";
    private int mCollomNumber = 4;
    private int mRowNumber = 4;
    private List<Point> blanklist;
    private NumberItem[][] numberItemMatrix;
    private WindowManager wm;
    private int startx = 0;
    private int starty = 0;
    private int stopx;
    private int stopy;
    private NumberItem item;

    //用于计算滑动之后每行或者每列合并后的数组

    private List<Integer> caculorList;

    //决定是否可以撤销的标志位
    boolean canRevert = false;
    //保存当前分数的一个成员变量

    private int currentScore;
    private int highestScore;
    private MainActivity activity;

    private int mTarget;
    private int[][] historyMatrix;
    private SharedPreferences sp;
    private int widthPixels;
    private float dx;
    private float dy;
    private int lineLevel;


    //private NumberItem[][]
    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //得到当前Activity的应用
        activity = MainActivity.getActivity();

        MyApplication app = (MyApplication) activity.getApplication();
        Log.i("app",app.toString());
        mRowNumber = app.getLineNumber();//动态修改棋盘行与列
        mCollomNumber = mRowNumber;
        mTarget = app.getTarget();//动态修改目标分数


        blanklist = new ArrayList<Point>();
        //初始化集合
        caculorList = new ArrayList<Integer>();
        numberItemMatrix = new NumberItem[mRowNumber][mCollomNumber];
        //历史记录
        historyMatrix = new int[mRowNumber][mRowNumber];

        currentScore = 0;
        //使用sharepreference保存上一个宫格的位置
        sp = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE);
        highestScore = sp.getInt("HighestScore", 0);

        //通过以下的方法可以获得屏幕的宽度和高度
        wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();//这个就是传说中的屏幕

        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;

        setRowCount(mRowNumber);
        setColumnCount(mCollomNumber);

        //使用循环把gridlayout加入到GameView中
        for (int i=0;i<mRowNumber;i++){
            for (int j=0;j<mCollomNumber;j++){
                Log.i("content",getContext().toString());
                item = new NumberItem(getContext(),0);
                //指定增加子控件的宽和高
                addView(item, widthPixels /mRowNumber, widthPixels /mCollomNumber);

                //把item宽和高 保存在二维矩阵中
                numberItemMatrix[i][j] = item;

                //初始化的时候记录当前空白的位置
                Point p = new Point();
                p.x = i;
                p.y = j;
                blanklist.add(p);
            }
        }



        //表示随机找两个位置，分别产生连个数字
        addRandomNumber();
        //表示随机找两个位置，分别产生连个数字
        addRandomNumber();
    }
    //在棋盘空白的位置上，随机找到一个位置，给他的item设置一个数字
    private void addRandomNumber() {
        updataBlankList();
        int size = blanklist.size();
        int location = (int) Math.floor(Math.random() * size);//floor返回不大于的最大整数
        Point point = blanklist.get(location);
        numberItemMatrix[point.x][point.y].setTextNumber(Math.random() > 0.5d ? 2 : 4);
    }

    private void updataBlankList() {
        blanklist.clear();
        for (int i=0;i<mRowNumber;i++){
            for (int j=0;j<mCollomNumber;j++){
                NumberItem numberItem = numberItemMatrix[i][j];
                int number = numberItem.getNumber();
                Log.i("number_updataBlankList",number+"");
                if (numberItem.getNumber()==0){
                    blanklist.add(new Point(i,j));
                }
            }
        }
    }

    public void restart(){

        removeAllViews();


        init();
        updateCurrentScore();
        Log.i(TAG, "restart GmaeView");


        //举例
        //int i =1/0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                startx=(int)event.getX();
                starty=(int)event.getY();

                saveHistroy();
                Log.i(TAG,"ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                stopx=(int)event.getX();
                stopy=(int)event.getY();
                Log.i(TAG,"ACTION_UP");

                judgeDeriction(startx,starty,stopx,stopy);

                //判断游戏是否结束 1 可以继续玩 2 成功了  3 gameover
                updateCurrentScore();
                handleResult(isOver());


                break;

        }


        return   true ;//super.onTouchEvent(event);
        //Down MOve ...MOve UP
        //表示当前控件来处理这个触摸事件的序列

    }

    private void updateCurrentScore() {
        //更新当前的分数
        activity.updateCurrentScore(currentScore);
    }

    //恢复上一步的状态
    public void revert(){

        //方法1，遍历一遍history矩阵，如果里面全是0，就直接return。


        //方法2，添加一个flag，当且仅当histroy矩阵有过赋值之后，才置位1.
        if (canRevert) {
            for (int i = 0; i < mRowNumber; i++)
                for (int j = 0; j < mCollomNumber; j++) {
                    numberItemMatrix[i][j].setTextNumber(historyMatrix[i][j]);

                }
        }
    }


    //把当前的记录保存到history矩阵中
    private void saveHistroy() {

        for (int i=0;i<mRowNumber;i++)
            for (int j=0;j<mCollomNumber;j++){
                historyMatrix[i][j] =  numberItemMatrix[i][j].getNumber() ;
            }

        canRevert=true;
    }

    private void handleResult(int result) {
        if (result==2){//完成游戏

            if (highestScore<currentScore){

                MyApplication app = (MyApplication) activity.getApplication();
                app.setHighestRecord(currentScore);
                activity.updateHighestScore(currentScore);
            }

            new     AlertDialog.Builder(getContext()).
                    setTitle("恭喜")
                    .setMessage("您已经完成游戏！")
                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("挑战更难", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

        }else  if (result==3){//gameover

            new     AlertDialog.Builder(getContext()).
                    setTitle("失败")
                    .setMessage("游戏结束")
                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();

                        }
                    })
                    .setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            activity.finish();//当前Activity关闭
                        }
                    })
                    .show();

        }else{//1 表示继续，则给出一个随机数
            if(dx>lineLevel||dy>lineLevel){
                addRandomNumber();
            }

        }
    }


    // 2 成功了    1 可以继续玩   3 gameover
    private int isOver(){

        for (int i=0;i<mRowNumber;i++)
            for (int j=0;j<mCollomNumber;j++){

                if (numberItemMatrix[i][j].getNumber()==mTarget){
                    return 2;
                }

            }

        //说明没有成功。

        updataBlankList();
        if (blanklist.size()==0){

            //这种情况下如果还有可以合并的，则返回1

            for (int i=0;i<mRowNumber;i++)
                for (int j=0;j<mCollomNumber-1;j++){
                    int current =   numberItemMatrix[i][j].getNumber();
                    int next = numberItemMatrix[i][j+1] .getNumber();
                    if (current==next){
                        return  1;
                    }
                }

            for (int i=0;i<mRowNumber;i++)
                for (int j=0;j<mCollomNumber-1;j++){
                    int current =   numberItemMatrix[j][i].getNumber();
                    int next = numberItemMatrix[j+1][i] .getNumber();
                    if (current==next){
                        return  1;
                    }
                }

            //如果没有可以合并的了，返回3
            return 3;


        }

        return 1;


    }


    private void judgeDeriction(float startx, float starty, float stopx, float stopy) {
        dx = Math.abs(startx-stopx);
        dy = Math.abs(starty-stopy);

        //设置一个阀值，防止出现问题
        lineLevel = widthPixels/5;
        if (dx > lineLevel || dy > lineLevel){
            boolean flag = dx > dy ?true:false;


            //Log.i(TAG,  "flag="+flag+"startx:"+ startx+", starty: "+starty+"stopx"+stopx+"stopy:"+ stopy);
            if (flag){//水平方向滑动

                if (stopx>startx){
                    //右滑
                    slideRigth();
                }else{
                    //左滑
                    slideLeft();
                }
            }else{ //竖直方向滑动
                if (stopy>starty){
                    //下滑
                    slideDwon();
                }else{
                    //上滑
                    slideUP();
                }


            }

        }


    }

    private void slideUP() {

        int prenumber=-1;
        for(int i =0;i<mRowNumber;i++) {
            for (int j = 0; j < mCollomNumber; j++) {

                final int number = numberItemMatrix[j][i].getNumber();

                if (number!=0){
                    if (number!=prenumber&&prenumber!=-1){
                        caculorList.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorList.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }
                    prenumber=number;
                }

            }

            //把最后一个prenumber加入到集合中
            if (prenumber!=0&&prenumber!=-1)
                caculorList.add(prenumber);


            //把通过计算后合并的数字放到矩阵中
            for(int p=0;p<caculorList.size();p++){
                numberItemMatrix[p][i].setTextNumber(caculorList.get(p));
            }

            //合并长度之后的部分以0来填充
            for (int q=caculorList.size();q<mCollomNumber;q++){
                numberItemMatrix[q][i].setTextNumber(0);
            }

            //重置中间变量，为下次循环做准备。
            caculorList.clear();
            prenumber=-1;

        }


        Log.i(TAG, "slide up");
    }

    private void slideDwon() {

        int prenumber=-1;
        for(int i =0;i<mRowNumber;i++) {
            for (int j = mCollomNumber-1; j >=0; j--) {

                final int number = numberItemMatrix[j][i].getNumber();

                if (number!=0){
                    if (number!=prenumber&&prenumber!=-1){
                        caculorList.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorList.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }
                    prenumber=number;
                }

            }

            //把最后一个prenumber加入到集合中
            if (prenumber!=0&&prenumber!=-1)
                caculorList.add(prenumber);


            //把通过计算后合并的数字放到矩阵中
            for(int p=mCollomNumber-1;p>=mCollomNumber-caculorList.size();p--){
                numberItemMatrix[p][i].setTextNumber(caculorList.get(mCollomNumber-1-p));
            }

            //合并长度之后的部分以0来填充
            for (int q=mCollomNumber-caculorList.size()-1;q>=0;q--){
                numberItemMatrix[q][i].setTextNumber(0);
            }

            //重置中间变量，为下次循环做准备。
            caculorList.clear();
            prenumber=-1;

        }


        Log.i(TAG, "slide down");
    }

    private void slideLeft() {


        int prenumber=-1;
        for(int i =0;i<mRowNumber;i++) {
            for (int j = 0; j < mCollomNumber; j++) {

                final int number = numberItemMatrix[i][j].getNumber();

                if (number!=0){
                    if (number!=prenumber&&prenumber!=-1){
                        caculorList.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorList.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }
                    prenumber=number;
                }

            }

            //把最后一个prenumber加入到集合中
            if (prenumber!=0&&prenumber!=-1)
                caculorList.add(prenumber);


            //把通过计算后合并的数字放到矩阵中
            for(int p=0;p<caculorList.size();p++){
                numberItemMatrix[i][p].setTextNumber(caculorList.get(p));
            }

            //合并长度之后的部分以0来填充
            for (int q=caculorList.size();q<mCollomNumber;q++){
                numberItemMatrix[i][q].setTextNumber(0);
            }

            //重置中间变量，为下次循环做准备。
            caculorList.clear();
            prenumber=-1;

        }

        Log.i(TAG, "slide left");
    }

    private void slideRigth() {

        int prenumber=-1;
        for(int i =0;i<mRowNumber;i++) {
            for (int j = mCollomNumber-1; j>=0; j--) {

                final int number = numberItemMatrix[i][j].getNumber();

                if (number!=0){
                    if (number!=prenumber&&prenumber!=-1){
                        caculorList.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorList.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }
                    prenumber=number;
                }

            }

            //把最后一个prenumber加入到集合中
            if (prenumber!=0&&prenumber!=-1)
                caculorList.add(prenumber);


            //把通过计算后合并的数字放到矩阵中
            for(int p=mRowNumber-1;p>=mRowNumber-caculorList.size();p--){
                numberItemMatrix[i][p].setTextNumber(caculorList.get(mRowNumber-1-p));
            }

            //合并长度之后的部分以0来填充
            for (int q=mRowNumber-caculorList.size()-1; q >=0;q--){
                numberItemMatrix[i][q].setTextNumber(0);
            }

            //重置中间变量，为下次循环做准备。
            caculorList.clear();
            prenumber =-1;

        }



        Log.i(TAG, "slide right");
    }


}


