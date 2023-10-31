package org.example.security;


import java.util.HashMap;
import java.util.Map;

public class UserValidator implements Validator {
    Map<String, String> users;
    private static UserValidator instance;

    private UserValidator() {
        users = new HashMap<>();
    }

    @Override
    public void addUser(String username, String password) {

        users.put(username, password);
    }

    @Override
    public boolean userIsValid(String userName, String password) {
        if (users.get(userName).equals(password)) {
            users.remove(userName);
            return true;
        }
        return false;
    }

    public static UserValidator getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (UserValidator.class) {
            if (instance == null) {
                instance = new UserValidator();
            }
            return instance;
        }
    }
}