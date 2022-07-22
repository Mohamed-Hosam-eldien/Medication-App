package com.example.medicationapp.models;

import java.io.Serializable;

public class TimeLong implements Serializable {

    private long[] times;

    public TimeLong() {
    }

    public TimeLong(long[] times) {
        this.times = times;
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
    }
}
