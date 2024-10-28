package com.example.notesapp;

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

public class MainActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    private NoteAdapter adapter;
    private RecyclerView notesRecyclerView;

    private ActivityResultLauncher<Intent> addNoteLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the notes list and set up the ListView with an adapter
        notes = new ArrayList<>();
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        adapter = new NoteAdapter(notes);
        notesRecyclerView.setAdapter(adapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize ActivityResultLauncher for NoteActivity
        addNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //Handle the result from NoteActivity
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                        //Add the new note to the list and update the adapter
                        String noteText = result.getData().getStringExtra("noteText");
                        Note newNote = new Note(noteText);
                        adapter.addNote(newNote);
                        //adapter.notifyDataSetChanged(); //try it in a UI thread if not working
                        System.out.println("1. " + notes.get(0).getText() + " received and added to notes ArrayList");
                    }
                }
        );

        //AddNoteButton launches NoteActivity
        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Launch NoteActivity and create a new note
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                addNoteLauncher.launch(intent);
               //startActivity(intent); // onActivityResult implementation
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        System.out.println("OnActivityResult called");
//        System.out.println(requestCode + ", " + resultCode + ", " + data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            String noteText = data.getStringExtra("noteText");
//            notes.add(new Note(noteText));
//            adapter.add(noteText);
//            adapter.notifyDataSetChanged();
//        }
//    }
}
