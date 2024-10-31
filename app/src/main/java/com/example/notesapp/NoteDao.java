package com.example.notesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

//Data Access Object (DAO) for the Note entity
@Dao
public interface NoteDao {

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    //Gets all notes from the notes table
    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();
}
