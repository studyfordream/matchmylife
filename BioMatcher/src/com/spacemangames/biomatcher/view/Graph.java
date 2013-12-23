package com.spacemangames.biomatcher.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.spacemangames.biomatcher.util.DateUtils;

public class Graph extends View {

    private static final int        FLING_FRAMERATE = 30;
    protected GraphAttributeHandler attributes;
    private float                   monthTextPaddingBottom;
    private float                   dayTextOffsetBottom;
    protected int                   graphBottomOffset;
    private float                   pixelsPerDay;
    protected long                  timePerPixel;
    protected final Calendar        originDate      = new GregorianCalendar();
    protected final Calendar        leftDate        = new GregorianCalendar();
    @SuppressLint("SimpleDateFormat")
    private final DateFormat        format          = new SimpleDateFormat("MMMM");

    private TouchEventHandler       touchEventHandler;
    private GestureDetector         gestureDetector;

    private float                   offsetX;
    private Scroller                scroller;

    public Graph(Context context) {
        super(context);
    }

    public Graph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Graph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        attributes = new GraphAttributeHandler(getContext(), attrs, defStyle);

        touchEventHandler = new TouchEventHandler(this);
        gestureDetector = new GestureDetector(getContext(), touchEventHandler);

        scroller = new Scroller(getContext());

        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        Paint.FontMetrics fontMetrics = attributes.getMonthTextPaint().getFontMetrics();
        monthTextPaddingBottom = fontMetrics.bottom;
        dayTextOffsetBottom = fontMetrics.bottom - fontMetrics.top;
        graphBottomOffset = (int) dayTextOffsetBottom * 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    protected void onDrawGraphBackground(Canvas canvas, int width, int height) {
        canvas.drawRect(0, 0, width, height - graphBottomOffset, attributes.getGraphBackgroundPaint());
        canvas.drawRect(calculateTodayRect(leftDate), attributes.getGraphTodayPaint());
    }

    private RectF calculateTodayRect(Calendar centerDate) {
        Calendar today = new GregorianCalendar();
        float xoffset = calculatePosition(DateUtils.roundToFullDay(today.getTimeInMillis()));

        return new RectF(xoffset, 0, xoffset + pixelsPerDay, getHeight() - graphBottomOffset);
    }

    protected void onDrawDaysText(Canvas canvas, int width, int height) {
        Calendar tempCal = new GregorianCalendar();
        long currentDay = DateUtils.roundToFullDay(leftDate.getTimeInMillis()) + DateUtils.DAY_MILLIS / 2;
        for (int i = 0; i < attributes.getDaysInView() + 1; ++i) {
            float center = calculatePosition(currentDay);
            tempCal.setTimeInMillis(currentDay);
            int day = tempCal.get(Calendar.DAY_OF_MONTH);
            canvas.drawText(String.valueOf(day), center, height - dayTextOffsetBottom, attributes.getDayNumbersTextPaint());
            currentDay += DateUtils.DAY_MILLIS;
        }
    }

    protected float calculatePosition(long time) {
        return (time - leftDate.getTimeInMillis()) / timePerPixel;
    }

    protected void onDrawMonthText(Canvas canvas, int width, int height) {
        Calendar center = new GregorianCalendar();
        center.setTimeInMillis(leftDate.getTimeInMillis() + (attributes.getDaysInView() / 2) * DateUtils.DAY_MILLIS);
        String month = format.format(center.getTime());
        canvas.drawText(month, width / 2, height - monthTextPaddingBottom, attributes.getMonthTextPaint());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (scroller.computeScrollOffset()) {
            offsetX = scroller.getCurrX();
            postInvalidateDelayed(FLING_FRAMERATE);
        }

        int width = getWidth();
        int height = getHeight();

        canvas.drawColor(attributes.getBackgroundColor());

        onDrawMonthText(canvas, width, height);
        onDrawDaysText(canvas, width, height);

        onDrawGraphBackground(canvas, width, height);

        onDrawDayLines(canvas, width, height);

        onDrawBaseLine(canvas, width, height);
    }

    private void onDrawBaseLine(Canvas canvas, int width, int height) {
        int y = (height - graphBottomOffset) / 2;
        canvas.drawLine(0, y, width, y, attributes.getBaseLinePaint());
    }

    private void onDrawDayLines(Canvas canvas, int width, int height) {
        long currentDay = DateUtils.roundToFullDay(leftDate.getTimeInMillis());
        for (int i = 0; i < attributes.getDaysInView() + 1; ++i) {
            float center = calculatePosition(currentDay);
            canvas.drawLine(center, 0, center, height - graphBottomOffset, attributes.getDayLinesPaint());
            currentDay += DateUtils.DAY_MILLIS;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width < height) {
            setMeasuredDimension(width, width);
        } else {
            setMeasuredDimension(width, height);
        }
        pixelsPerDay = getMeasuredWidth() / (float) attributes.getDaysInView();
        timePerPixel = (long) (DateUtils.DAY_MILLIS / pixelsPerDay);
    }

    public void scroll(float distanceX) {
        scroller.forceFinished(true);
        this.offsetX -= distanceX;
        invalidate();
    }

    protected float getOffsetX() {
        return offsetX;
    }

    public void fling(float velocityX) {
        scroller.forceFinished(true);
        scroller.fling((int) offsetX, 0, (int) velocityX, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        invalidate();
    }
}