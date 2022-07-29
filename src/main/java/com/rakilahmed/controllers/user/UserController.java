package com.rakilahmed.controllers.user;

import java.util.List;

public abstract class UserController<T> {
    /**
     * Registers a new user.
     * 
     * @param user The user to register.
     * @return String indicating whether the user was registered successfully.
     */
    public abstract String register(T user);

    /**
     * Logs in a user.
     * 
     * @param user The user to login.
     * @return String indicating whether the user was logged in successfully.
     */
    public abstract String login(T user);

    /**
     * Logs out a user.
     * 
     * @param user The user to log out.
     * @return String indicating whether the user was logged out successfully.
     */
    public abstract String logout(T user);

    /**
     * View user's profile.
     * 
     * @param id The user's id.
     * @return User's profile as a string.
     */
    public abstract String viewProfile(int id);

    /**
     * Edit user's profile.
     * 
     * @param user        The user to edit.
     * @param newUsername The new username of the user.
     * @param newPassword The new password of the user.
     * @param newFullName The new full name of the user.
     * @param newEmail    The new email of the user.
     * @return User, with the new profile.
     */
    public abstract T editProfile(T user, String newUsername, String newPassword, String newFullName,
            String newEmail);

    /**
     * Returns the user with the given id.
     * 
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
