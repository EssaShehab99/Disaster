package com.example.disaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class WeatherRVModel {
    private String time;
    private String city;
    private String temperature;
    private String icon;
    private String windSpeed;
    private String humidity;
    private String uv;

    public WeatherRVModel(String time, String temperature, String icon, String windSpeed, String humidity, String uv,String city) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.uv = uv;
        this.city = city;
    }
    static public WeatherRVModel fromMap(JSONObject json) throws JSONException {

        return new WeatherRVModel(
                json.getJSONObject("current").getString("last_updated"),
                json.getJSONObject("current").getString("temp_c"),
                json.getJSONObject("current").getJSONObject("condition").getString("icon"),
                json.getJSONObject("current").getString("wind_degree"),
                json.getJSONObject("current").getString("humidity"),
                json.getJSONObject("current").getString("uv"),
                json.getJSONObject("location").getString("name")
        );
    }
    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getUv() {
        return uv;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
