package com.spacemangames.biomatcher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.spacemangames.biomatcher.model.biorhythm.BioRhythm;
import com.spacemangames.biomatcher.model.biorhythm.BioType;
import com.spacemangames.biomatcher.util.DateUtils;

public class BioRhythmGraph extends Graph {
    private BioRhythm bioRhythm;

    float[]           data;

    public BioRhythmGraph(Context context) {
        super(context);
        init(null, 0);
    }

    public BioRhythmGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BioRhythmGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        data = new float[getMeasuredWidth() * 4];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        long leftTimestamp = (originDate.getTimeInMillis() - (attributes.getDaysInView() / 2) * DateUtils.DAY_MILLIS - (long) (getOffsetX() * timePerPixel));
        leftDate.setTimeInMillis(leftTimestamp);

        super.onDraw(canvas);

        if (bioRhythm != null) {
            long graphStart = leftTimestamp;
            long graphEnd = leftTimestamp + attributes.getDaysInView() * DateUtils.DAY_MILLIS;
            onDrawPhysical(canvas, width, height - graphBottomOffset, graphStart, graphEnd);
            onDrawEmotional(canvas, width, height - graphBottomOffset, graphStart, graphEnd);
            onDrawIntellectual(canvas, width, height - graphBottomOffset, graphStart, graphEnd);
        }
    }

    private void onDrawPhysical(Canvas canvas, int width, int height, long graphStart, long graphEnd) {
        Paint paint = attributes.getPhysicalPaint();
        float strokeWidth = paint.getStrokeWidth() / 2f;
        bioRhythm.getValues(BioType.PHYSICAL, graphStart, graphEnd, width, strokeWidth, height - strokeWidth, data);
        canvas.drawLines(data, paint);
    }

    private void onDrawEmotional(Canvas canvas, int width, int height, long graphStart, long graphEnd) {
        Paint paint = attributes.getEmotionalPaint();
        float strokeWidth = paint.getStrokeWidth() / 2f;
        bioRhythm.getValues(BioType.EMOTIONAL, graphStart, graphEnd, width, strokeWidth, height - strokeWidth, data);
        canvas.drawLines(data, paint);
    }

    private void onDrawIntellectual(Canvas canvas, int width, int height, long graphStart, long graphEnd) {
        Paint paint = attributes.getIntellectualPaint();
        float strokeWidth = paint.getStrokeWidth() / 2f;
        bioRhythm.getValues(BioType.INTELLECTUAL, graphStart, graphEnd, width, strokeWidth, height - strokeWidth, data);
        canvas.drawLines(data, paint);
    }

    public void setBioRhythm(BioRhythm bioRhythm) {
        this.bioRhythm = bioRhythm;
        invalidate();
    }
}
