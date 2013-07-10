package com.spacemangames.biomatcher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.spacemangames.biomatcher.model.biorhythm.BioRhythmMatcher;
import com.spacemangames.biomatcher.model.biorhythm.BioType;
import com.spacemangames.biomatcher.util.DateUtils;

public class BioMatchResultGraph extends Graph {
    private BioRhythmMatcher bioMatch;

    float[]                  matches;

    public BioMatchResultGraph(Context context) {
        super(context);
        init(null, 0);
    }

    public BioMatchResultGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BioMatchResultGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        if (width < 4096) { // this can't be right, seems to happen on tablet UI
            matches = new float[width * 4];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        long leftTimestamp = (originDate.getTimeInMillis() - (attributes.getDaysInView() / 2) * DateUtils.DAY_MILLIS - (long) (touchEventHandler
                .getOffsetX() * timePerPixel));
        leftDate.setTimeInMillis(leftTimestamp);

        super.onDraw(canvas);

        if (bioMatch != null) {
            long graphStart = leftTimestamp;
            long graphEnd = leftTimestamp + attributes.getDaysInView() * DateUtils.DAY_MILLIS;
            onDrawPhysical(canvas, width, height - graphBottomOffset, graphStart, graphEnd);
            onDrawEmotional(canvas, width, height - graphBottomOffset, graphStart, graphEnd);
            onDrawIntellectual(canvas, width, height - graphBottomOffset, graphStart, graphEnd);
        }
    }

    private void onDrawPhysical(Canvas canvas, int width, int height, long graphStart, long graphEnd) {
        Paint paint = attributes.getPhysicalPaint();
        float stroke = paint.getStrokeWidth() / 2f;
        bioMatch.getMatches(BioType.PHYSICAL, graphStart, graphEnd, width, stroke, height - stroke, matches);
        canvas.drawLines(matches, paint);
    }

    private void onDrawEmotional(Canvas canvas, int width, int height, long graphStart, long graphEnd) {
        Paint paint = attributes.getEmotionalPaint();
        float stroke = paint.getStrokeWidth() / 2f;
        bioMatch.getMatches(BioType.EMOTIONAL, graphStart, graphEnd, width, stroke, height - stroke, matches);
        canvas.drawLines(matches, paint);
    }

    private void onDrawIntellectual(Canvas canvas, int width, int height, long graphStart, long graphEnd) {
        Paint paint = attributes.getIntellectualPaint();
        float stroke = paint.getStrokeWidth() / 2f;
        bioMatch.getMatches(BioType.INTELLECTUAL, graphStart, graphEnd, width, stroke, height - stroke, matches);
        canvas.drawLines(matches, paint);
    }

    public void setBioMatch(BioRhythmMatcher bioMatch) {
        this.bioMatch = bioMatch;
        invalidate();
    }
}
