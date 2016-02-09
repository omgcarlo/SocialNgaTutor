package com.incc.softwareproject.socialngatutor.calendar;

import com.p_v.flexiblecalendar.entity.Event;

/**
 * @author p-v
 */
public class CustomEvent implements Event {

    private int color;

    public CustomEvent(int color){
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }
}
