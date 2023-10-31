package org.example.Json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class SerializableJSONArray implements Serializable {
    private  StringBuilder jsonStringBuilder;

    public SerializableJSONArray(JSONArray jsonArray) {
        this.jsonStringBuilder.insert(0,jsonArray.toString());
    }
    public SerializableJSONArray() {
        this.jsonStringBuilder.insert(0,new JSONArray());
    }
    public void addJsonObject(JSONObject jsonObject){
        jsonStringBuilder.insert(jsonStringBuilder.length()-1,jsonObject.toString());
    }
    @Override
    public String toString() {
        return jsonStringBuilder.toString();
    }
}