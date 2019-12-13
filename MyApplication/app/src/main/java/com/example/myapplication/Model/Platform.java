package com.example.myapplication.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Platform {

    int id;
    String name;

    public Platform(JSONObject jObject) throws JSONException {
        id = jObject.getInt("id");
        name = jObject.getString("name");
    }

}
