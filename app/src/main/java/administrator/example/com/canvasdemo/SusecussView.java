package administrator.example.com.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.sql.Ref;

/**
 * Created by yuer on 2016/6/23.
 */
public class SusecussView extends View {
    private Paint mPaint;
    private float mWidth;
    private float mHeight;
    private Path SuPath ;
    private Path BgPath ;

    public SusecussView(Context context) {
        this(context,null);
    }

    public SusecussView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        initPaint();
        initPath();
    }

    private void initPath() {
        SuPath = new Path();
        SuPath.lineTo(-20,-20);
        SuPath.lineTo(0,10);
        SuPath.lineTo(40,-40);

        BgPath = new Path();
        BgPath.addCircle(0,0,50, Path.Direction.CW);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);




    }
}
