package com.mycontactapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact")
    List<Contact> getAll();

    @Insert
    void insert(Contact... contacts);

    @Query("SELECT * FROM Contact " +
            "WHERE first_name LIKE '%' || :search || '%' OR last_name LIKE '%' || :search || '%'")
    List<Contact> findUserWithName(String search);

}
