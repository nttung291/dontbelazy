package com.example.sonnv.dontbelazy.activities.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.sonnv.dontbelazy.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

/**
 * Created by cuonghx2709 on 10/2/2017.
 */

public class DaynowDecor  implements DayViewDecorator {

    private CalendarDay date;
    private Drawable drawable;

    public DaynowDecor(Context context) {
        date = CalendarDay.today();
        drawable = context.getResources().getDrawable(R.drawable.my_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.5f));
        view.setBackgroundDrawable(drawable);
    }

    /**
     * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
     */
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
