package com.example.form.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.Map;
import java.util.Objects;

public class HelpCenterModel {
    String name;
    String phone;
    String description;
    LatLng latLng;

    public HelpCenterModel(String name, String phone, String description, LatLng latLng) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.latLng = latLng;
    }
    public HelpCenterModel(){}
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setName(String name) {
        this.name = name;
    }

    static public HelpCenterModel fromMap(Map<String, Object> map, String id) throws JSONException {
        String[] latlong =  Objects.requireNonNull(map.getOrDefault("latLng","0")).toString().split(",");

        return new HelpCenterModel(
                Objects.requireNonNull(map.getOrDefault("name","0")).toString(),
                Objects.requireNonNull(map.getOrDefault("phone","0")).toString(),
                Objects.requireNonNull(map.getOrDefault("description","0")).toString(),
                new LatLng(Double.parseDouble(latlong[0]),Double.parseDouble(latlong[1]))
        );
    }
}
