package com.rakilahmed.controllers.user;

import java.util.List;

public abstract class UserController<T> {
    /**
     * Registers a new user.
     * 
     * @param user The user to register.
     * @return ID of the user if successful, -1 if not.
     */
    public abstract int register(T user);

    /**
     * Logs in a user.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @return ID of the user if successful, -1 otherwise.
     */
    public abstract int login(String username, String password);

    /**
     * View user's profile.
     * 
     * @param id The user's id.
     * @return User's profile as a string.
     */
    public abstract String viewProfile(int id);

    /**
     * Update user's profile.
     * 
     * @param id       The user's id.
     * @param username The updated username.
     * @param password The updated password.
     * @param fullName The updated full name.
     * @param email    The updated email.
     * @return The updated user.
     */
    public abstract String updateProfile(int id, String username, String password, String fullName, String email);

    /**
     * Returns the user with the given id.
     * 
     * @param id The user's id.
     * @return The user with the given id.
     */
    public abstract T get(int id);

    /**
     * Returns all users.
     * 
     * @return All users as a list.
     */
    public abstract List<T> getAll();
}
