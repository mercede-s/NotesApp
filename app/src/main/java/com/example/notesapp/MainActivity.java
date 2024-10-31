package com.example.notesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private ArrayList<Note> notes;
    private NoteAdapter adapter;
    private RecyclerView notesRecyclerView;

    private ActivityResultLauncher<Intent> editNoteLauncher;

    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the database instance
        noteDatabase = NoteDatabase.getInstance(this);

        //Initialize ArrayList, adapter, and Recycler View for displaying notes
        notes = new ArrayList<>();
        adapter = new NoteAdapter(notes, this);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(adapter);

        loadNotesFromDatabase();

        //Launcher handles result from Note Activity
        editNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                        //Get the position and text from NoteActivity's result intent
                        int position = result.getData().getIntExtra("notePosition", -1);
                        String noteText = result.getData().getStringExtra("noteText");

                        if (noteText != null) {

                            //Adding new note
                            if (position == notes.size()) {
                                Note newNote = new Note();
                                newNote.setText(noteText);

                                //Update the ArrayList
                                adapter.addNote(newNote);

                                //Insert the new note into the database asynchronously
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    noteDatabase.noteDao().insert(newNote);
                                });
                            }

                            //All text from existing note has been deleted
                            else if (noteText.isEmpty()) {
                                deleteNote(position);
                                adapter.removeNote(position);
                            }

                            //Editing existing note
                            else {
                                Note UpdatedNote = notes.get(position);
                                UpdatedNote.setText(noteText);

                                // Update the note in the ArrayList and the database
                                adapter.updateNote(position, UpdatedNote);
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    noteDatabase.noteDao().update(UpdatedNote);
                                });

                            }
                        }
                    }
                }
        );

        //Setup button to add a new note
        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(v -> {

            //Send the new notes' position + current size of notes array to NoteActivity
            int newPosition = notes.size();
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("notePosition", newPosition);
            intent.putExtra("notesSize", notes.size());
            editNoteLauncher.launch(intent);
        });
    }

    //Notes loaded from the database asynchronously
    private void loadNotesFromDatabase() {
        Executors.newSingleThreadExecutor().execute(() -> {
            notes.clear();
            notes.addAll(noteDatabase.noteDao().getAllNotes());

            //Notify adapter that data has changed on the main thread
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    //When a note is clicked NoteActivity is launched
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, NoteActivity.class);
        Note note = notes.get(position);
        intent.putExtra("noteText", note.getText());
        intent.putExtra("notePosition", position);
        intent.putExtra("noteSize", notes.size());

        editNoteLauncher.launch(intent);
    }

    //When a note is long-clicked a dialog is shown to confirm the delete
    @Override
    public void onNoteLongClick(int position) {
        new AlertDialog.Builder(this).setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", ((dialog, which) -> {
                    deleteNote(position);
                } )).setNegativeButton("No", null).show();
    }

    //Deletes a note from both the list and the database
    public void deleteNote(int position) {
        Note noteToDelete = notes.get(position);
        Executors.newSingleThreadExecutor().execute(() -> {
            noteDatabase.noteDao().delete(noteToDelete);
            notes.remove(position);

            //Notify adapter of removal on the main thread
            runOnUiThread(() -> adapter.notifyItemRemoved(position));
        });
    }
}
