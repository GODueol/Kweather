package kweather.android.godueol.com.kweather.Entity.TweatherPOJO;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TweatherDataSet {
    private Long date;
    private Double temp;
    private Integer humidity; // 습도
    private Integer cloud;
    private Double wind;
    private String icon;
    private Double rain;
    private static Double K = -273.15;
    private List<TweatherDataSet> list;

    public TweatherDataSet(TweatherPOJO tweatherPOJO) {
        list = new ArrayList<>();

        for (kweather.android.godueol.com.kweather.Entity.TweatherPOJO.List l : tweatherPOJO.getList()) {
            Weather w = l.getWeather().get(0);
            Main m = l.getMain();

            date = l.getDt();
            wind = l.getWind().getSpeed();
            icon = w.getIcon();
            temp = m.getTemp() + K;
            humidity = m.getHumidity();
            cloud = l.getClouds().getAll();
            try {
                rain = l.getRain().get3h();
            } catch (Exception e) {
                rain = 0.0;
            }

            list.add(new TweatherDataSet(date, temp, humidity, cloud, wind, icon, rain));
        }

    }

    public TweatherDataSet(Long date, Double temp, Integer humidity, Integer cloud, Double wind, String icon, Double rain) {
        this.date = date;
        this.temp = temp;
        this.humidity = humidity;
        this.cloud = cloud;
        this.wind = wind;
        this.icon = icon;
        this.rain = rain;
    }

    public Date getDate() {
        return new Date(date * 1000);
    }

    public void setDate(Long date) {
        this.date = date;
    }


    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Float getFtemp(){
        return temp.floatValue();

    }
    public String getTemp() {
        return convertTemp(temp);
    }

    private String convertTemp(Double l) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(l) + "°";
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public String getCloud() {
        return cloud + "%";
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public String getWind() {
        return wind + "㎧";
    }

    public void setWind(Double wind) {
        this.wind = wind;
    }

    public String getIcon() {
        return "http://openweathermap.org/img/w/" + icon + ".png";
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRain() {
        return (rain != null ? rain : 0) + "mm";
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public List<TweatherDataSet> getList() {
        return list;
    }

    public void setList(List<TweatherDataSet> list) {
        this.list = list;
    }
}
