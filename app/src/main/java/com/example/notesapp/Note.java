package com.example.notesapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Define Note as entity in Room database
@Entity(tableName = "notes")
public class Note {

    //Unique identifier for eac note, automatically generated
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;

    public Note() { }

    public Note(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
