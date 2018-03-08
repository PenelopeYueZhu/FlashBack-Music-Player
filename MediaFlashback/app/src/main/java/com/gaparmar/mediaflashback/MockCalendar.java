package com.gaparmar.mediaflashback;

import java.util.Calendar;

/**
 * Created by Gordee on 3/7/2018.
 */

class MockCalendar extends Calendar{

    public long millis;

    public MockCalendar(){
    }

    public void setTimeInMillis(long millis){
        this.millis = millis;
    }

    public long getTimeInMillis(){
        return this.millis;
    }

    public static MockCalendar getInstance(){
        return new MockCalendar();
    }

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
}
