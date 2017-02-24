package liamkengineering.led_matrix;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class DrawView extends View {
    private String mExampleString = "hello"; // TODO: use a default from R.string...
    private int mExampleColor = 25; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public boolean bg = false; // whether to draw background or not
    private float x, y;
    public boolean drawXY; // whether to draw a new circle or not

    List<float[]> l = new ArrayList<>();

    public Bitmap mB;

    public DrawView(Context context) {
        super(context);
        setFocusableInTouchMode(true);
        setFocusable(true);
        //init(null, 0);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusableInTouchMode(true);
        setFocusable(true);
        //init(attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusableInTouchMode(true);
        setFocusable(true);
        //init(attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(bg)
        {
            l.clear();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        //canvas.setBitmap(mB);
        //if (drawXY) {
        Paint blk = new Paint();
        blk.setColor(getResources().getColor(R.color.black));
        blk.setStrokeWidth(25);
        for(int i = 0; i<l.size()-1; i++) {
            if(l.get(i)[0]>=0 && l.get(i+1)[0]>=0)
            canvas.drawLine(l.get(i)[0], l.get(i)[1], l.get(i+1)[0], l.get(i+1)[1], blk);
        }
        //canvas.drawCircle(x, y, 15, blk);
        //drawXY = false;
        //}


    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DrawView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.DrawView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.DrawView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.DrawView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.DrawView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.DrawView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initbg(canvas);
        if(bg)
        {
            l.clear();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }

        //canvas.setBitmap(mB);
        //if (drawXY) {
            Paint blk = new Paint();
            blk.setColor(getResources().getColor(R.color.black));
            blk.setStrokeWidth(25);
            for(int i = 0; i<l.size()-1; i++) {
                if(l.get(i)[0]>=0 && l.get(i)[0]>=0 && l.get(i+1)[0]>=0)
                canvas.drawLine(l.get(i)[0], l.get(i)[1], l.get(i+1)[0], l.get(i+1)[1], blk);
            }
            //canvas.drawCircle(x, y, 15, blk);
            //drawXY = false;
        //}


    }

    private void initbg(Canvas canvas) {
        Paint white = new Paint();
        Paint red = new Paint();
        red.setColor(getResources().getColor(R.color.red));
        white.setColor(getResources().getColor(R.color.background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), white);
        //canvas.drawLine(0,0,getWidth(),getHeight(),red);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        x = e.getX();
        y = e.getY();

        float[] temp = {x, y};

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawXY = true;
                l.add(temp);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                drawXY = true;
                l.add(temp);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                float[] neg = {-1,-1};
                l.add(neg);
        }
        return true;
    }



    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
