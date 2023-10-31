package org.example.models;

import org.example.Json.SerializableJSONArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Result implements Serializable {
    private final String RED = "\u001B[31m";
    private final String RESET = "\u001B[0m";
    private Status status;
    private String message;
    private SerializableJSONArray jsonArray;

    public Result() {
        this.jsonArray = new SerializableJSONArray();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJsonArray() {
        return jsonArray.toString();
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = new SerializableJSONArray(jsonArray);
    }

    public void addToJsonArray(JSONObject jsonObject) {
        jsonArray.addJsonObject(jsonObject);
    }

    @Override
    public String toString() {
        String result = status + " : " + message;
        if (jsonArray.toString().length() > 2) result = result + "\n" + jsonArray;
        if (status.equals(Status.OK))
            System.out.println(result);
        else System.out.println(RED + result + RESET);
        return "";
    }
}
