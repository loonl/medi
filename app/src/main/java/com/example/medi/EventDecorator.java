package com.example.medi;


import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private GradientDrawable temp;
    private InsetDrawable draw;

    public EventDecorator(int color, Collection<CalendarDay> dates, Activity context) {
        this.color = color;
        this.dates = new HashSet<>(dates);

        this.temp = new GradientDrawable();
        temp.setShape(GradientDrawable.OVAL);
        temp.setStroke(2, color);

        this.draw = new InsetDrawable(temp, 5, 5, 5, 5);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(draw);
        //view.addSpan(new DotSpan(9, color));
    }
}