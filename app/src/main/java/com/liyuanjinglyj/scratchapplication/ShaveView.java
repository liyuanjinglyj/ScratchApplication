package com.liyuanjinglyj.scratchapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ShaveView extends View {
    private float mPreX,mPreY;//刮刮乐手指定位的X,Y坐标
    private Bitmap dstBitmap,srcBitmap;//目标图像（手指刮除图层），原图像
    private Paint mPaint,mTextPaint;//图像画笔工具，文字画笔工具
    private Path mPath;//手指刮除路径
    private int strLength;//文字的长度
    private int strHeight;//文字的高度
    private String textStr="恭喜你中奖了";//刮除后显示的文字
    public ShaveView(Context context) {
        super(context);
    }

    public ShaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        this.mPaint=new Paint();
        this.mPaint.setColor(Color.RED);
        this.mPaint.setStrokeWidth(45);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPath=new Path();
        this.mTextPaint=new Paint();
        this.mTextPaint.setColor(Color.BLACK);
        this.mTextPaint.setTextSize(56);
        Rect rect = new Rect();
        this.mTextPaint.getTextBounds(textStr,0,textStr.length(),rect);
        this.strLength=rect.width();
        this.strHeight=rect.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        if(this.dstBitmap==null || this.srcBitmap==null){
            this.srcBitmap=Bitmap.createBitmap(getWidth()/4*3,getHeight()/4*3,Bitmap.Config.ARGB_8888);
            this.dstBitmap=Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
            this.srcBitmap.eraseColor(Color.GRAY);
        }
        canvas.drawText(textStr,getWidth()/2-strLength/2,getHeight()/2-strHeight/2,mTextPaint);
        int layerId=canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
        Canvas c=new Canvas(dstBitmap);
        c.drawPath(mPath,mPaint);

        canvas.drawBitmap(dstBitmap,0,0,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(srcBitmap,getWidth()/8,getHeight()/8,mPaint);

        this.mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(),event.getY());
                this.mPreX=event.getX();
                this.mPreY=event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX=(this.mPreX+event.getX())/2;
                float endY=(this.mPreY+event.getY())/2;
                mPath.quadTo(this.mPreX,this.mPreY,endX,endY);
                this.mPreX=event.getX();
                this.mPreY=event.getY();
                break;
            default:
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    public ShaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
