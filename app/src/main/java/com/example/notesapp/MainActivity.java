package com.example.notesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private ArrayList<Note> notes;
    private NoteAdapter adapter;
    private RecyclerView notesRecyclerView;
    private ActivityResultLauncher<Intent> editNoteLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the notes list and set up the RecyclerView with an adapter
        notes = new ArrayList<>();
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(notes, this); // this = onclick listener??
        notesRecyclerView.setAdapter(adapter);

        //Initialize ActivityResultLauncher for NoteActivity
        editNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //Handle the result from NoteActivity
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int position = result.getData().getIntExtra("notePosition", -1);
                        String noteText = result.getData().getStringExtra("noteText");
                        if (noteText != null) {
                            if (position == notes.size()) {
                                Note newOrUpdatedNote = new Note(noteText);
                                adapter.addNote(newOrUpdatedNote);
                            }
                            else {
                                Note newOrUpdatedNote = new Note(noteText);
                                adapter.updateNote(position, newOrUpdatedNote);
                            }
                        }
                    }
                }
        );

        //AddNoteButton launches NoteActivity
        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Calculate the new notes position
                int newPosition = notes.size();

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);

                //Send new position to NoteActivity
                intent.putExtra("notePosition", newPosition);
                //here - calculate size of current list and send that info through with the intent
                editNoteLauncher.launch(intent);
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("noteText", notes.get(position).getText());
        intent.putExtra("notePosition", position);
        editNoteLauncher.launch(intent);
    }

    @Override
    public void onNoteLongClick(int position) {
        new AlertDialog.Builder(this).setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", ((dialog, which) -> {
                    adapter.removeNote(position);
                })).setNegativeButton("No", null).show();
    }
}
