package com.spacemangames.biomatcher.model.biorhythm;

import android.annotation.SuppressLint;
import android.util.FloatMath;

public class BioRhythmMatcher {
    public static final long          MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    private final BioRhythm           one;
    private final BioRhythm           two;
    private final BioMatcherAlgorithm algorithm;

    public BioRhythmMatcher(BioRhythm one, BioRhythm two, BioMatcherAlgorithm algorithm) {
        this.one = one;
        this.two = two;
        this.algorithm = algorithm;
    }

    public int calculateTotalMatch() {
        int days = calculateDayDifference();
        int physical = calculateCompatibility(BioType.PHYSICAL, days);
        int emotional = calculateCompatibility(BioType.EMOTIONAL, days);
        int intellectual = calculateCompatibility(BioType.INTELLECTUAL, days);
        return Math.round((physical + emotional + intellectual) / 3f);
    }

    public int calculateMatch(BioType type) {
        return calculateCompatibility(type, calculateDayDifference());
    }

    @SuppressLint("FloatMath")
    private int calculateCompatibility(BioType type, int days) {
        float period = type.getPeriod();
        if (algorithm == BioMatcherAlgorithm.PHASE_DIFF) {
            return Math.round((200 / period) * Math.abs(((days % period) - period / 2f)));
        } else {
            return Math.round(100 * Math.abs(FloatMath.cos((float) Math.PI * days / period)));
        }
    }

    private int calculateDayDifference() {
        long MillisDiff = one.getDate().getTimeInMillis() - two.getDate().getTimeInMillis();
        return (int) Math.abs(MillisDiff / MILLIS_IN_A_DAY);
    }

    public void getMatches(BioType type, long startTime, long endTime, int points, float low, float high, float[] result) {
        long timePerPoint = (endTime - startTime) / points;
        for (int i = 0; i < points - 1; ++i) {
            int index = i * 4;
            long time1 = startTime + timePerPoint * i;
            long time2 = startTime + timePerPoint * (i + 1);
            float y1 = calculateMatch(one.getValue(type, time1), two.getValue(type, time1), low, high);
            float y2 = calculateMatch(one.getValue(type, time2), two.getValue(type, time2), low, high);

            result[index] = i;
            result[index + 1] = y1;
            result[index + 2] = i + 2;
            result[index + 3] = y2;
        }
    }

    private float calculateMatch(float value1, float value2, float low, float high) {
        float height = high - low;
        return (height - (1 - (Math.abs(value1 - value2) / 2f)) * height) + low;
    }
}
