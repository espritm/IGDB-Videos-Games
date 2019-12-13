package com.example.myapplication.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DonneesJeuxVideos {

    String jsonString;

    ArrayList<JeuVideo> lsGames;

    public String gJsonString(){
        return jsonString;
    }

    public void setJsonString(String jSon){
        jsonString = jSon;
    }


    public DonneesJeuxVideos(String json) throws JSONException
    {
        lsGames = new ArrayList<JeuVideo>();

        JSONArray jArray = new JSONArray(json);

        for (int i = 0; i < 10; i++)
        {
            JeuVideo jeu = new JeuVideo(jArray.getString(i));
            lsGames.add(jeu);
        }
    }

    public ArrayList<JeuVideo> getGames() {
        return lsGames;
    }
}
