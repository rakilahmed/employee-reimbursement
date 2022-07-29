package com.rakilahmed.services;

import java.util.List;

public interface DAO<T> {
    /**
     * Inserts a new item into the database.
     * 
     * @param item The item to insert.
     * @return The id of the item inserted into the database.
     */
    int insert(T item);

    /**
     * Checks if an item exists in the database.
     * 
     * @param item The item to check.
     * @return If the item exists in the database.
     */
    boolean exists(T item);

    /**
     * Updates an item in the database.
     * 
     * @param id          The id of the item to update.
     * @param updatedItem The updated item.
     * @return The updated item.
     */
    T update(int id, T updatedItem);

    /**
     * Retrieves an item from the database.
     * 
     * @param id The id of the item to retrieve.
     * @return The item with the given id.
     */
    T get(int id);

    /**
     * Retrieves all items from the database.
     * 
     * @return A list of all items in the database.
     */
    List<T> getAll();
}
