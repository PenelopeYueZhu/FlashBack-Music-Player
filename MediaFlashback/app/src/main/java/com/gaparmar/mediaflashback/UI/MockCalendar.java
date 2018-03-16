package com.gaparmar.mediaflashback.UI;

import java.util.Calendar;

import static com.gaparmar.mediaflashback.Constant.MORNING_DIVIDER;
import static com.gaparmar.mediaflashback.Constant.NOON_DIVIVER;

/**
 * Created by Gordee on 3/7/2018.
 */

/**
 * A class to create a mock calendar object
 */
class MockCalendar extends Calendar{

    int hour;
    Calendar cal;
    long millis;

    public MockCalendar(){
        this.millis = 0;
    }


    public MockCalendar(long millis){
        this.millis = millis;
    }

    public static MockCalendar getInstance(){
        return new MockCalendar();
    }

    /**
     * Sets the hour of the mockCalendar
     * @param hour The hour to set the calendar to
     */
    public void setHour(int hour){
        this.hour = hour;
        cal.set(0,0,0,hour,0);
    }

    /**
     * Returns the hour of the calendar
     * @return The hour
     */
    public int getHour(){
      return cal.HOUR_OF_DAY;
    }

    //TODO make into enum
    public int getState(){
        if(cal.HOUR_OF_DAY >= 0 && cal.HOUR_OF_DAY <= MORNING_DIVIDER ){
            return 0;
        }else if (cal.HOUR_OF_DAY > MORNING_DIVIDER && cal.HOUR_OF_DAY <= NOON_DIVIVER){
            return 1;
        }else{
            return 2;
        }
    }


    /********** Overridden methods for the calendar class **********/
    @Override
    protected void computeTime() {

    }

    @Override
    protected void computeFields() {

    }

    @Override
    public void add(int field, int amount) {

    }

    @Override
    public void roll(int field, boolean up) {

    }

    @Override
    public int getMinimum(int field) {
        return 0;
    }

    @Override
    public int getMaximum(int field) {
        return 0;
    }

    @Override
    public int getGreatestMinimum(int field) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int field) {
        return 0;
    }

    /********* End overridden methods **********/
}
