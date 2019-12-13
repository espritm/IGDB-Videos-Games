package com.example.myapplication.Model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JeuVideo {

    String sJsonString;
    String name;
    String summary;
    ArrayList<Video> lsVideos = new ArrayList<Video>();
    ArrayList<Platform> lsPlatforms = new ArrayList<Platform>();


    public JeuVideo(String sJson) throws JSONException {
        sJsonString = sJson;

        JSONObject jObject = new JSONObject(sJson);

        name = jObject.getString("name");

        if (jObject.has("summary"))
            summary = jObject.getString("summary");

        if (jObject.has("videos")){
            JSONArray jArray = jObject.getJSONArray("videos");

            for (int i = 0; i < jArray.length(); i++){
                lsVideos.add(new Video(jArray.getJSONObject(i)));
            }
        }

        if (jObject.has("platforms")){
            JSONArray jArray = jObject.getJSONArray("platforms");

            for (int i = 0; i < jArray.length(); i++){
                lsPlatforms.add(new Platform(jArray.getJSONObject(i)));
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getJson() {
        return sJsonString;
    }

    public String getVideoID() {
        if (lsVideos == null || lsVideos.size() == 0)
            return null;
        else
            return lsVideos.get(0).getYoutubeID();
    }

    public ArrayList<String> getPlatforms(){
        ArrayList<String> lsRes = new ArrayList<String>();

        if (lsPlatforms != null)
            for (Platform p: lsPlatforms) {
                lsRes.add(p.name);
            }

        return  lsRes;
    }
}
