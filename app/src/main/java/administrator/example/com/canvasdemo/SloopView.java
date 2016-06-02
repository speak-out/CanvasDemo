package administrator.example.com.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuPopupHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yuer on 2016/6/2.
 */
public class SloopView extends View {
    // 颜色表
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<PieData> mData;
    // 宽高
    private int mWidth, mHeight;
    // 画笔
    private Paint mPaint = new Paint();

    public SloopView(Context context) {
        super(context);
        initView(context);

    }

    public SloopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

       /* 为了证明画布是可以移动的，
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);

        mPaint.setColor(Color.BLUE);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);*/


       /* 证明了画布缩小，画的图也会跟着缩小，如果没有特意说明中心点的话，默认缩放中心点就是原点
       先设置画布缩小，再去画图
       canvas.translate(mWidth/2,mHeight/2);
        RectF rect = new RectF(0,-400,400,0);
        canvas.drawRect(rect,mPaint);

        canvas.scale(0.5f,0.5f);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect,mPaint);*/


        /*
            改变缩放的中心点
        mPaint.setColor(Color.RED);
        canvas.drawPoint(0,0,mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.translate(mWidth/2,mHeight/2);
        RectF rect = new RectF(0,-400,400,0);
        canvas.drawRect(rect,mPaint);

        canvas.scale(0.5f,0.5f,200,0);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect,mPaint);*/


       /*
           当设置为负数的时候，缩放点在原点，则会围绕原点中心旋转，并缩放
        mPaint.setColor(Color.RED);
        canvas.drawPoint(0,0,mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.translate(mWidth/2,mHeight/2);
        RectF rect = new RectF(0,-400,400,0);
        canvas.drawRect(rect,mPaint);
        canvas.scale(-0.5f,-0.5f);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect,mPaint);*/


       /*
       X轴旋转并缩放
       mPaint.setColor(Color.RED);
        canvas.drawPoint(0,0,mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.translate(mWidth/2,mHeight/2);
        RectF rect = new RectF(0,-400,400,0);
        canvas.drawRect(rect,mPaint);
        canvas.scale(-0.5f,-0.5f,200,0);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect,mPaint);*/

      /*
        画布尽心缩放，中心点缩放，正好是矩形的中心点
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.translate(mWidth/2,mHeight/2);
        RectF rect = new RectF(-400,-400,400,400);
        for(int i = 0 ;i <= 20 ;i++){
            canvas.scale(0.5f,0.5f);
            canvas.drawRect(rect,mPaint);
        }
*/

        //旋转

        /*
          原点旋转
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.translate(mWidth/2,mHeight/2);
        RectF rectf = new RectF(0,-400,400,0);
        canvas.drawRect(rectf,mPaint);
        canvas.rotate(180);  //需要提示加入旋转
        X轴对称旋转
        canvas.translate(mWidth/2,mHeight/2);
        RectF rectf = new RectF(0,-400,400,0);
        canvas.drawRect(rectf,mPaint);
        canvas.rotate(180,200,0);  //绕X轴

        mPaint.setColor(Color.GREEN);
        canvas.drawRect(rectf,mPaint);
        */


   /*     //使用旋转去做一个类似于火车道的东西，收尾相连的东西

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1f);
        mPaint.setStyle(Paint.Style.STROKE);

        //将画布移动到我们需要的中心点
        canvas.translate(mWidth/2,mHeight/2);
        canvas.drawCircle(0,0,380,mPaint);
        canvas.drawCircle(0,0,360,mPaint);
        for(int i = 0 ; i< 36;i++){
            canvas.drawLine(0,360,0,380,mPaint);
            canvas.rotate(10);
        }*/

        //错切
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.translate(mWidth/2,mHeight/2);
        RectF rectf = new RectF(0,-200,200,0);
        canvas.drawRect(rectf,mPaint);
        canvas.skew(1,0);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(rectf,mPaint);

    }
























    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    // 设置数据
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initDate(mData);
        invalidate();   // 刷新
    }

    // 初始化数据
    private void initDate(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
            return;

        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            sumValue += pie.getValue();       //计算数值和

            int j = i % mColors.length;       //设置颜色
            pie.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue;   // 百分比
            float angle = percentage * 360;                 // 对应的角度

            pie.setPercentage(percentage);                  // 记录百分比
            pie.setAngle(angle);                            // 记录角度大小
            sumAngle += angle;

            Log.i("angle", "" + pie.getAngle());
        }
    }
    private void initView(Context context) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10f);
        mPaint.setColor(Color.BLACK);
    }

    class PieData {
        // 用户关心数据
        private String name;        // 名字
        private float value;        // 数值
        private float percentage;   // 百分比
        // 非用户关心数据
        private int color = 0;      // 颜色
        private float angle = 0;    // 角度

        public PieData(@NonNull String name, @NonNull float value, @NonNull float percentage) {
            this.name = name;
            this.value = value;
            this.percentage = percentage;
        }

        public String getName() {
            return name;
        }

        public float getValue() {
            return value;
        }

        public float getPerengage() {
            return percentage;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setAngle(float angle) {
            this.angle = angle;

        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public float getAngle() {
            return angle;
        }
    }}
