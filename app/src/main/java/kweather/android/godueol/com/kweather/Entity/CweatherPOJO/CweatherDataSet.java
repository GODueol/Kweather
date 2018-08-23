package kweather.android.godueol.com.kweather.Entity.CweatherPOJO;

import java.text.DecimalFormat;
import java.util.Date;

public class CweatherDataSet {

    private String city;
    private Double temp, temp_min, temp_max;
    private Integer humidity; // 습도
    private Integer cloud;
    private Double wind;
    private String icon;
    private Double rain;
    private Long sunrise,sunset;
    private static Double K = -273.15;

    public CweatherDataSet(CweatherPOJO cweatherPOJO) {
        this.city = cweatherPOJO.getName();
        this.temp = cweatherPOJO.getMain().getTemp() + K;
        this.temp_min = cweatherPOJO.getMain().getTempMin() + K;
        this.temp_max = cweatherPOJO.getMain().getTempMax() + K;
        this.humidity = cweatherPOJO.getMain().getHumidity();
        this.cloud = cweatherPOJO.getClouds().getAll();
        this.wind = cweatherPOJO.getWind().getSpeed();
        this.icon = cweatherPOJO.getWeather().get(0).getIcon();
        this.sunrise = cweatherPOJO.getSys().getSunrise();
        this.sunset = cweatherPOJO.getSys().getSunset();
        try {
            this.rain = cweatherPOJO.getRain().get3h();
        }catch (Exception e){
            rain = Double.valueOf(0);
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemp() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(temp);
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getTemp_min() {
        return convertTemp(temp_min);
    }

    public void setTemp_min(Double temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return convertTemp(temp_max);
    }

    private String convertTemp(Double l){
        DecimalFormat df = new DecimalFormat("#");
        return df.format(l)+"°";
    }

    public void setTemp_max(Double temp_max) {
        this.temp_max = temp_max;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public String getCloud() {
        return cloud+"%";
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public String getWind() {
        return wind+"㎧";
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
        return (rain != null ? rain : 0)+"mm";
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Date getDate() {
        return new Date(System.currentTimeMillis());
    }

    public Date getSunrise() {
        return new Date(sunrise*1000);
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Date getSunset() {
        return new Date(sunset*1000);
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }
}
