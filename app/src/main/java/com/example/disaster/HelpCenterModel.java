package com.example.disaster;

import com.google.android.gms.maps.model.LatLng;

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
}
