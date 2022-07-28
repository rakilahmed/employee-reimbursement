package com.rakilahmed.controllers.user;

import com.rakilahmed.models.user.User;

public abstract class UserController {
    /**
     * Registers a new user.
     * 
     * @param user
     * @return String indicating whether the user was registered successfully.
     */
    public abstract String register(User user);

    /**
     * Logs in a user.
     * 
     * @param user
     * @return String indicating whether the user was logged in successfully.
     */
    public abstract String login(User user);

    /**
     * Logs out a user.
     * 
     * @param user
     * @return String indicating whether the user was logged out successfully.
     */
    public abstract String logout(User user);
}
