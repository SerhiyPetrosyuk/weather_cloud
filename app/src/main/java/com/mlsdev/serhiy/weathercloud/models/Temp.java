
package com.mlsdev.serhiy.weathercloud.models;

import com.google.gson.annotations.Expose;

public class Temp {

    @Expose
    private Double day;
    @Expose
    private Double min;
    @Expose
    private Double max;
    @Expose
    private Double night;
    @Expose
    private Double eve;
    @Expose
    private Double morn;

    /**
     * @return
     */
    public Double getDay() {
        return day;
    }

    /**
     * @param day
     */
    public void setDay(Double day) {
        this.day = day;
    }

    /**
     * @return
     */
    public Double getMin() {
        return min;
    }

    /**
     * @param min
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     * @return
     */
    public Double getMax() {
        return max;
    }

    /**
     * @param max
     */
    public void setMax(Double max) {
        this.max = max;
    }

    /**
     * @return
     */
    public Double getNight() {
        return night;
    }

    /**
     * @param night
     */
    public void setNight(Double night) {
        this.night = night;
    }

    /**
     * @return
     */
    public Double getEve() {
        return eve;
    }

    /**
     * @param eve
     */
    public void setEve(Double eve) {
        this.eve = eve;
    }

    /**
     * @return
     */
    public Double getMorn() {
        return morn;
    }

    /**
     * @param morn
     */
    public void setMorn(Double morn) {
        this.morn = morn;
    }

}
