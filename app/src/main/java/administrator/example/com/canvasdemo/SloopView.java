package administrator.example.com.canvasdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yuer on 2016/6/2.
 */
public class SloopView extends View {


    // View 宽高
    private int mViewWidth;
    private int mViewHeight;

    // 这个视图拥有的状态
    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    // 当前的状态(非常重要)
    private State mCurrentState = State.NONE;

    // 放大镜与外部圆环
    private Path path_srarch;
    private Path path_circle;

    // 测量Path 并截取部分的工具
    private PathMeasure mMeasure;

    // 默认的动效周期 2s
    private int defaultDuration = 2000;

    // 控制各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;

    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    // 用于控制动画状态转换
    private Handler mAnimatorHandler;

    // 判断是否已经搜索结束
    private boolean isOver = false;

    private int count = 0;
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
    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;

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
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.translate(mWidth/2,mHeight/2);
//        RectF rectf = new RectF(0,-200,200,0);
//        canvas.drawRect(rectf,mPaint);
//        canvas.skew(1,0);
//        mPaint.setColor(Color.GREEN);
//        canvas.drawRect(rectf,mPaint);
//
//        Path  path = new Path();
//        path.moveTo(100,100);
//        path.rLineTo(100,200);
//        canvas.drawPath(path,mPaint);

//        canvas.translate(mWidth/2,mHeight/2);
//
//        RectF rectF = new RectF(0,0,400,400);
//
//        mPaint.setStyle(Paint.Style.FILL);
//
//        Path path = new Path();
//
//        path.setFillType(Path.FillType.INVERSE_WINDING);
//
//        path.addRect(rectF, Path.Direction.CW);


    /*使用path的布尔运算画了一个太极图案

      canvas.translate(mWidth / 2, mHeight / 2);

        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        Path path4 = new Path();
        RectF rectF = new RectF(0, -200, 200, 200);
        path1.addCircle(0, 0, 200, Path.Direction.CW);
        path2.addRect(rectF, Path.Direction.CW);
        path3.addCircle(0, -100, 100, Path.Direction.CW);
        path4.addCircle(0, 100, 100, Path.Direction.CW);

        path1.op(path2, Path.Op.DIFFERENCE);
        path1.op(path3, Path.Op.UNION);
        path1.op(path4, Path.Op.DIFFERENCE);

        canvas.drawPath(path1, mPaint);
        canvas.save();
        Path path = new Path();
        path.addCircle(0,-100,20, Path.Direction.CW);
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(path,mPaint);
        canvas.restore();
        canvas.save();
        Path  path5 = new Path();
        path5.addCircle(1,100,20,Path.Direction.CW);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(path5,mPaint);
        canvas.restore();
        canvas.save();
        Path path6 = new Path();
        path6.addCircle(0, 0, 200, Path.Direction.CW);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        canvas.drawPath(path6,mPaint);
        canvas.restore();*/

     /*主要的任务是测量了path的大小，使用一个RectF来存储

        canvas.translate(mWidth/2,mHeight/2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        RectF rectF = new RectF();  //用来存储测量path所占的大小，，是一个矩形区域，以及path所在的位置
        Path path = new Path();
        path.lineTo(100,-50);
        path.lineTo(100,50);
        path.close();    //图形封闭，链接第一个点和最后一个点，实现图片的封闭
        path.addCircle(-100,0,100, Path.Direction.CW);

        path.computeBounds(rectF,true);//最后一个参数已经是废弃，一般设置为true

        canvas.drawPath(path,mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rectF,mPaint);*/



    /*pathMeasure 与Path之间的关联，，主要区分就是PathMeasure中的第二个参数的使用，设置为true，则会测量一个封闭的图形
    就算图形没有封闭，也会自动将path的第一个点和最后一个点连接起来，但是对path本身没有什么卵影响

        canvas.translate(mWidth/2,mHeight/2);
        mPaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.lineTo(0,100);
        path.lineTo(100,100);
        path.lineTo(100,0);
        canvas.drawPath(path,mPaint);
        PathMeasure pathMeasure1 = new PathMeasure(path,false);
        PathMeasure pathMeasure2 = new PathMeasure(path,true);

        Log.e("pathMeasure 111 ",pathMeasure1.getLength()+"");
        Log.e("pathMeasure 222 ",pathMeasure2.getLength()+"");*/
        //getSagment  获取path的指定的lenght，nextContent获取下一个path的长度，对path的添加顺序有关系


        //画一个箭头跟着圆来转动,什么图形都是可以的，应为图形会跟着path的路径去走

       /* canvas.translate(mWidth/2,mHeight/2);
        Path path = new Path();

        RectF rectF = new RectF(-100,-50,100,50);
//        path.addRect(rectF, Path.Direction.CW);
        path.lineTo(0,100);
        path.lineTo(100,-100);
        path.close();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        PathMeasure pathMeasure = new PathMeasure(path,true);

        currentValue += 0.005;
        if(currentValue >= 1){
            currentValue = 0;
        }
        pathMeasure.getPosTan(pathMeasure.getLength()*currentValue,pos,tan);  //获取到path的值，已经在某点的tan和pos

        mMatrix.reset();//重置Matrix，以免和上一次的有冲突
        //根据该点的pos值以及tan值，去计算箭头移动的方向以及轨迹
        float degree = (float) ((Math.atan2(tan[1],tan[0]))*180.0/Math.PI); //计算图片要旋转的角度

        mMatrix.preRotate(degree,mBitmap.getWidth()/2,mBitmap.getHeight()/2);
        mMatrix.postTranslate(pos[0]-mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2);
        canvas.drawPath(path,mPaint);
        canvas.drawBitmap(mBitmap,mMatrix,mPaint);
        invalidate();
*/
/*
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        canvas.translate(mWidth/2,mHeight/2);
       Path path_srarch = new Path();
        Path path_circle = new Path();

      PathMeasure  mMeasure = new PathMeasure();

        // 注意,不要到360度,否则内部会自动优化,测量不能取到需要的数值
        RectF oval1 = new RectF(-50, -50, 50, 50);          // 放大镜圆环
        path_srarch.addArc(oval1, 45, 359.9f);

        RectF oval2 = new RectF(-100, -100, 100, 100);      // 外部圆环
        path_circle.addArc(oval2, 45, -359.9f);

        float[] pos = new float[2];

        mMeasure.setPath(path_circle, false);               // 放大镜把手的位置
        mMeasure.getPosTan(0, pos, null);

        path_srarch.lineTo(pos[0], pos[1]);                 // 放大镜把手

        canvas.drawPath(path_circle,mPaint);
        canvas.drawPath(path_srarch,mPaint);

        Log.i("TAG", "pos=" + pos[0] + ":" + pos[1]);
*/
        super.onDraw(canvas);
        drawSearch(canvas);


    }
    private int num= 0;

    private void drawSearch(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        mPaint.setColor(Color.RED);
//        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        Path dst = new Path();
        canvas.drawColor(Color.parseColor("#0082D7"));
        canvas.save();
        mMeasure.setPath(path_circle,true);
        currentValue += 0.1;
        if(currentValue>=1){
            currentValue = 0;

        }
        mMeasure.getSegment(10,mMeasure.getLength(),dst,true);
            canvas.drawPath(dst, mPaint);
        PathMeasure pathMeasure = new PathMeasure(dst,false);

        Log.e("截取的长度",pathMeasure.getLength()+"");


        canvas.restore();
//        canvas.drawPath(path_circle,mPaint);

//        invalidate();





//        canvas.drawPath(path_circle,mPaint);
//        canvas.save();
//        canvas.drawPath(path_srarch,mPaint);
//        canvas.restore();






//        mCurrentState = State.STARTING;
//        switch (mCurrentState) {
//            case NONE:   //none
//                canvas.drawPath(path_srarch, mPaint);
//                break;
//            case STARTING:  //starting
//                mMeasure.setPath(path_circle, false);
//                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue, mMeasure.getLength(), dst, true);
//                Log.e("AnimationValue--",mAnimatorValue+":"+mMeasure.getLength());
//                canvas.drawPath(dst, mPaint);
//                break;
//            case SEARCHING: //searching
//                mMeasure.setPath(path_circle, false);
//                Path dst2 = new Path();
//                float stop = mMeasure.getLength() * mAnimatorValue;
//                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f));
//                mMeasure.getSegment(start, stop, dst2, true);
//                canvas.drawPath(dst2, mPaint);
//                break;
//            case ENDING: //ending
//                mMeasure.setPath(path_srarch, false);
//                Path dst3 = new Path();
//                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue, mMeasure.getLength(), dst3, true);
//                canvas.drawPath(dst3, mPaint);
//                break;
//        }
    }
    private void initAnimator() {
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mSearchingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mEndingAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration);

        mStartingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);

        mStartingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);
    }


    private void initHandler() {
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurrentState) {
                    case STARTING:
                        // 从开始动画转换好搜索动画
                        isOver = false;
                        mCurrentState = State.SEARCHING;
                        mStartingAnimator.removeAllListeners();
                        mSearchingAnimator.start();
                        break;
                    case SEARCHING:
                        if (!isOver) {  // 如果搜索未结束 则继续执行搜索动画
                            mSearchingAnimator.start();
                            Log.e("Update", "RESTART");

                            count++;
                            if (count>2){       // count大于2则进入结束状态
                                isOver = true;
                            }
                        } else {        // 如果搜索已经结束 则进入结束动画
                            mCurrentState = State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;
                    case ENDING:
                        // 从结束动画转变为无状态
                        mCurrentState = State.NONE;
                        break;
                }
            }
        };
    }
    private void initPath() {
        path_srarch = new Path();
        path_circle = new Path();

        mMeasure = new PathMeasure();

        // 注意,不要到360度,否则内部会自动优化,测量不能取到需要的数值
        RectF oval1 = new RectF(-50, -50, 50, 50);          // 放大镜圆环
        path_srarch.addArc(oval1, 45, 359.9f);

        RectF oval2 = new RectF(-100, -100, 100, 100);      // 外部圆环
        path_circle.addArc(oval2, 45, -359.9f);

        float[] pos = new float[2];

        mMeasure.setPath(path_circle, false);               // 放大镜把手的位置
        mMeasure.getPosTan(0, pos, null);

        path_srarch.lineTo(pos[0], pos[1]);                 // 放大镜把手

        Log.i("TAG", "pos=" + pos[0] + ":" + pos[1]);
    }
    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // getHandle发消息通知动画状态更新
                mAnimatorHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }
    private void initView(Context context) {

        initPaint();

        initPath();

//        initListener();
//
//        initHandler();
//
//        initAnimator();
//
//        // 进入开始动画
//        mCurrentState = State.STARTING;
//        mStartingAnimator.start();

//        tan = new float[2];
//        pos = new float[2];
//
//        mMatrix = new Matrix();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
//        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.round, options);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(10f);
//        mPaint.setColor(Color.BLACK);
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
    }
}
