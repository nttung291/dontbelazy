package com.example.sonnv.dontbelazy.databases;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cuonghx2709 on 9/20/2017.
 */

public class CourseMode implements Serializable {
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private String location;
    private Date currentDate;
    private int id;

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public CourseMode(){
    }

    public CourseMode(String name, Date dateStart, Date dateEnd, String location, int id) {

        this.name = name;
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.location = location;



    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CourseMode{" +
                "name='" + name + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", location='" + location + '\'' +
                '}';
    }
}
