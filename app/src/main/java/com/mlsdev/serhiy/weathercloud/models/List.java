
package com.mlsdev.serhiy.weathercloud.models;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;

public class List {

    @Expose
    private Long dt;
    @Expose
    private Temp temp;
    @Expose
    private Double pressure;
    @Expose
    private Double humidity;
    @Expose
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    @Expose
    private Double speed;
    @Expose
    private Double deg;
    @Expose
    private Integer clouds;
    @Expose
    private Double snow;

    /**
     * @return
     */
    public Long getDt() {
        return dt;
    }

    /**
     * @param dt
     */
    public void setDt(Long dt) {
        this.dt = dt;
    }

    /**
     * @return
     */
    public Temp getTemp() {
        return temp;
    }

    /**
     * @param temp
     */
    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    /**
     * @return
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * @param pressure
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * @return
     */
    public Double getHumidity() {
        return humidity;
    }

    /**
     * @param humidity
     */
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    /**
     * @return
     */
    public java.util.List<Weather> getWeather() {
        return weather;
    }

    /**
     * @param weather
     */
    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    /**
     * @return
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * @param speed
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * @return
     */
    public Double getDeg() {
        return deg;
    }

    /**
     * @param deg
     */
    public void setDeg(Double deg) {
        this.deg = deg;
    }

    /**
     * @return
     */
    public Integer getClouds() {
        return clouds;
    }

    /**
     * @param clouds
     */
    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    /**
     * @return
     */
    public Double getSnow() {
        return snow;
    }

    /**
     * @param snow
     */
    public void setSnow(Double snow) {
        this.snow = snow;
    }

}
