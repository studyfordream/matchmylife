package com.spacemangames.biomatcher.model.biorhythm;

public enum BioType {
    PHYSICAL(23), EMOTIONAL(28), INTELLECTUAL(33);

    private final int period;

    private BioType(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    public long getPeriodMillis() {
        return period * 24L * 60L * 60L * 1000L;
    }
}
