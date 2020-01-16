package hakemy.balance.balance1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class draw extends View {
    private   Paint mPaint; // hold style and color information about geometries text and bitmaps;
    int left;int top;int right;
    int bottom;

    public int getLeft1() {
        return left;
    }


    public void setLeft1(int left) {
        this.left = left;
    }

    public int getTop1() {
        return top;
    }


    public void setTop1(int top) {
        this.top = top;
    }


    public int getRight1() {
        return right;
    }


    public void setRight1(int right) {
        this.right = right;
    }

    public int getBottom1() {
        return bottom;
    }


    public void setBottom1(int bottom) {
        this.bottom = bottom;
    }


    public draw(Context context) {
        super(context);

        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getLeft1(),getTop1(),getRight1(),getBottom1(),mPaint);

    }
}
