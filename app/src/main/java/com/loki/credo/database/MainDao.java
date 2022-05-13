package com.loki.credo.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.loki.credo.models.Contacts;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(Contacts contact);

    @Query("SELECT * FROM contacts")
    List<Contacts> getAll();

    @Query("UPDATE contacts SET contact = :contact WHERE id =:id")
    void update(int id, String contact);

    @Delete
    void delete(Contacts contacts);

}
