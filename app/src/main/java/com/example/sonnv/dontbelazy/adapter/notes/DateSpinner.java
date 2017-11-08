package com.example.sonnv.dontbelazy.adapter.notes;

/**
 * Created by cuonghx2709 on 9/29/2017.
 */

public class DateSpinner {
    private int id;
    private String name;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public DateSpinner(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
