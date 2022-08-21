package com.example.medi;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SelectedDecorator implements DayViewDecorator {
    private Drawable temp;
    private InsetDrawable draw;

    public SelectedDecorator(Activity context) {
        temp = context.getResources().getDrawable(R.drawable.calendar_selector, null);
        draw = new InsetDrawable(temp, 5, 5, 5, 5);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) { return true; }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(draw);
    }
}
