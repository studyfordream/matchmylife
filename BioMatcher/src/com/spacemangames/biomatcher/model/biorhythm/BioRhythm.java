package com.spacemangames.biomatcher.model.biorhythm;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

@SuppressWarnings("serial")
public class BioRhythm implements Serializable {
    private Calendar           date    = new GregorianCalendar();
    private static final float floatPI = (float) Math.PI;

    public BioRhythm() {
    }

    public BioRhythm(long millis) {
        setDate(millis);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDate(long millis) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);
        setDate(calendar);
    }

    @SuppressLint("FloatMath")
    public float getValue(BioType type, long time) {
        double timeDiff = time - date.getTimeInMillis();
        double periodMillis = (float) type.getPeriodMillis();
        return (float) Math.sin(2 * floatPI * timeDiff / periodMillis);
    }

    public Direction getDirection(BioType type, long time) {
        long time2 = time;
        while (Math.abs(getValue(type, time2) - getValue(type, time)) < 0.00001) {
            time2 += 1000;
        }
        if (getValue(type, time2) - getValue(type, time) > 0) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    public int getPercentageValue(BioType type, long time) {
        return Math.round((getValue(type, time) + 1) * 50);
    }

    public void getValues(BioType type, long startTime, long endTime, int points, float low, float high, float[] result) {
        long timePerPoint = (endTime - startTime) / points;
        for (int i = 0; i < points - 1; ++i) {
            int index = i * 4;
            long time1 = startTime + timePerPoint * i;
            long time2 = startTime + timePerPoint * (i + 1);

            float y1 = calculateScaled(type, low, high, time1);
            float y2 = calculateScaled(type, low, high, time2);

            result[index] = i;
            result[index + 1] = y1;
            result[index + 2] = i + 2;
            result[index + 3] = y2;
        }
    }

    private float calculateScaled(BioType type, float low, float high, long time) {
        float scale = high - low;
        return (scale - ((1 + getValue(type, time)) / 2f) * scale) + low;
    }
}
