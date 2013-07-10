package com.spacemangames.biomatcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.spacemangames.biomatcher.R;

public class GraphAttributeHandler {
    private int                daysInView;
    private int                backgroundColor;
    private int                graphBackgroundColor;
    private int                graphTodayColor;
    private int                emotionalColor;
    private int                physicalColor;
    private int                intellectualColor;
    private int                baseLineColor;
    private TextPaint          monthTextPaint;
    private TextPaint          dayNumbersTextPaint;
    private Paint              dayLinesPaint;
    private Paint              graphBackgroundPaint;
    private Paint              graphTodayPaint;
    private Paint              physicalPaint;
    private Paint              emotionalPaint;
    private Paint              intellectualPaint;
    private Paint              baseLinePaint;
    private static final float DAYLINE_STROKE_WIDTH  = 0.5f;
    private static final float BASELINE_STROKE_WIDTH = 0.7f;

    public GraphAttributeHandler() {
        this.backgroundColor = Color.TRANSPARENT;
        this.graphBackgroundColor = Color.TRANSPARENT;
        this.graphTodayColor = Color.LTGRAY;
        this.emotionalColor = Color.GREEN;
        this.physicalColor = Color.RED;
        this.intellectualColor = Color.BLUE;
        this.baseLineColor = Color.WHITE;
    }

    public GraphAttributeHandler(Context context, AttributeSet attrs, int defStyle) {
        this();

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BioMatchResultGraph, defStyle, 0);

        float linethickness = a.getDimension(R.styleable.BioMatchResultGraph_linethickness, 1f);
        float daytextsize = a.getDimension(R.styleable.BioMatchResultGraph_daytextsize, 10f);
        float monthtextsize = a.getDimension(R.styleable.BioMatchResultGraph_monthtextsize, 10f);
        backgroundColor = a.getColor(R.styleable.BioMatchResultGraph_backgroundColor, backgroundColor);
        graphBackgroundColor = a.getColor(R.styleable.BioMatchResultGraph_graphBackgroundColor, graphBackgroundColor);
        graphTodayColor = a.getColor(R.styleable.BioMatchResultGraph_todayColor, graphTodayColor);
        emotionalColor = a.getColor(R.styleable.BioMatchResultGraph_emotionalColor, emotionalColor);
        physicalColor = a.getColor(R.styleable.BioMatchResultGraph_physicalColor, physicalColor);
        intellectualColor = a.getColor(R.styleable.BioMatchResultGraph_intellectualColor, intellectualColor);
        baseLineColor = a.getColor(R.styleable.BioMatchResultGraph_baselineColor, baseLineColor);
        int textColor = a.getColor(R.styleable.BioMatchResultGraph_textcolor, Color.WHITE);
        daysInView = a.getInt(R.styleable.BioMatchResultGraph_daysinview, 14);

        a.recycle();

        monthTextPaint = createTextPaint(monthtextsize, textColor);
        dayNumbersTextPaint = createTextPaint(daytextsize, textColor);

        dayLinesPaint = new Paint();
        dayLinesPaint.setColor(Color.LTGRAY);
        dayLinesPaint.setAntiAlias(true);
        dayLinesPaint.setStrokeWidth(DAYLINE_STROKE_WIDTH);

        graphBackgroundPaint = new Paint();
        graphBackgroundPaint.setColor(graphBackgroundColor);
        graphTodayPaint = new Paint();
        graphTodayPaint.setColor(graphTodayColor);

        physicalPaint = setupMatchLinePaint(physicalColor, linethickness);
        emotionalPaint = setupMatchLinePaint(emotionalColor, linethickness);
        intellectualPaint = setupMatchLinePaint(intellectualColor, linethickness);

        setupBaseLinePaint();
    }

    private void setupBaseLinePaint() {
        baseLinePaint = new Paint();
        baseLinePaint.setColor(baseLineColor);
        baseLinePaint.setAntiAlias(true);
        baseLinePaint.setStrokeWidth(BASELINE_STROKE_WIDTH);
    }

    private TextPaint createTextPaint(float monthtextsize, int color) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(color);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(monthtextsize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        return textPaint;
    }

    private Paint setupMatchLinePaint(int color, float linethickness) {
        Paint paint = new Paint();
        paint.setStrokeWidth(linethickness);
        paint.setAntiAlias(true);
        paint.setColor(color);
        return paint;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public TextPaint getMonthTextPaint() {
        return monthTextPaint;
    }

    public TextPaint getDayNumbersTextPaint() {
        return dayNumbersTextPaint;
    }

    public Paint getDayLinesPaint() {
        return dayLinesPaint;
    }

    public Paint getGraphBackgroundPaint() {
        return graphBackgroundPaint;
    }

    public Paint getGraphTodayPaint() {
        return graphTodayPaint;
    }

    public Paint getPhysicalPaint() {
        return physicalPaint;
    }

    public Paint getEmotionalPaint() {
        return emotionalPaint;
    }

    public Paint getIntellectualPaint() {
        return intellectualPaint;
    }

    public int getDaysInView() {
        return daysInView;
    }

    public Paint getBaseLinePaint() {
        return baseLinePaint;
    }
}