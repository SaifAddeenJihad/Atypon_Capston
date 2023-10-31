package org.example.User;

import org.example.Disk.DiskUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.example.Disk.DiskUtils.readDocument;

public class UsersManager {
    private static UsersManager instance;
    Map<String, User> users;

    private UsersManager() throws IOException {
        users = getUsers();
    }

    private HashMap<String, User> getUsers() throws IOException {
        Map<String, User> users = new HashMap<String, User>();
        JSONArray usersArray = readDocument("users"); // Assuming readDocument returns JSONArray
        for (Object userObject : usersArray) {
            JSONObject jsonObject = (JSONObject) userObject;
            User user = new User(jsonObject.getString("username"), jsonObject.getString("password"));
            users.put(user.getUsername(), user);
        }
        return (HashMap<String, User>) users;
    }

    public void addUser(User user) throws IOException {
        users.put(user.getUsername(), user);
        DiskUtils.updateDocument("users", user.toJsonObject());
    }

    public static UsersManager getInstance() throws IOException {
        if (instance != null) {
            return instance;
        }
        synchronized (UsersManager.class) {
            if (instance == null) {
                instance = new UsersManager();
            }
            return instance;
        }
    }

    public String validateNewUsername(String username) {
        if (users.containsKey(username)) return "invalid";
        return "valid";
    }

    public String validateOldUsername(String username) {
        if (users.containsKey(username)) return "valid";
        return "invalid";
    }

    public User getUser(String username) {
        return users.get(username);
    }
}