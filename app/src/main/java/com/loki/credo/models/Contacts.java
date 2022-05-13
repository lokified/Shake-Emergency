package com.loki.credo.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "contacts")
public class Contacts implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id = 0;

    @ColumnInfo(name = "contact")
    String contact = "";

//    public Contacts(String contact) {
//        this.contact = contact;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
