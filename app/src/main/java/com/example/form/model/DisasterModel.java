package com.example.form.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DisasterModel {
    String image;
    String title;
    String desc;
public DisasterModel(String image, String title, String desc) {
    this.image = image;
    this.title = title;
    this.desc = desc;
}
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }


    static public DisasterModel fromMap(Map<String, Object> map, String id) throws JSONException {
        return new DisasterModel(
                Objects.requireNonNull(map.getOrDefault("image","0")).toString(),
                Objects.requireNonNull(map.getOrDefault("title","0")).toString(),
                Objects.requireNonNull(map.getOrDefault("desc","0")).toString()
        );
    }
}
