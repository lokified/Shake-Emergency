package com.loki.credo.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.loki.credo.models.Contacts;

@Database(entities = Contacts.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB database;
    public static String DATABASE_NAME = "ShakeApp";


    public synchronized static RoomDB getInstance(Context context) {

        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    public abstract MainDao mainDao();
}
