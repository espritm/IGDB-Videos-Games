package com.example.myapplication.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Video {

     int id;
     String name;
     String video_id;

     public Video(JSONObject jObject) throws JSONException {
        id = jObject.getInt("id");
        name = jObject.getString("name");
        video_id = jObject.getString("video_id");
     }

     public String getYoutubeID()
     {
         return video_id;
     }
}
