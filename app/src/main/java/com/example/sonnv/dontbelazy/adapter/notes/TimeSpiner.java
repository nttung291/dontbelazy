package com.example.sonnv.dontbelazy.adapter.notes;

/**
 * Created by cuonghx2709 on 9/29/2017.
 */

public class TimeSpiner {
    private String time;
    private String name;

    public static TimeSpiner instence = new TimeSpiner(null, null);

    public TimeSpiner(String time, String name) {
        this.time = time;
        this.name = name;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
