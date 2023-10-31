package org.example.security;

public interface Validator {
    public void addUser(String username, String password);
    public  boolean userIsValid(String userName, String password);

}
