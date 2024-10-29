package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {

    private EditText noteEditText;
    private int notePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Initialize EditText and Save button
        noteEditText = findViewById(R.id.noteEditText);
        Button saveNoteButton = findViewById(R.id.saveNoteButton);

        //Retrieve position from the intent
        Intent intent = getIntent();
        notePosition = intent.getIntExtra("notePosition", -1);

        //Check if theres exiting text to edit
        String noteText = intent.getStringExtra("noteText");
        if (noteText != null) {
            noteEditText.setText(noteText);
        }

        saveNoteButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("noteText", noteEditText.getText().toString());
            resultIntent.putExtra("notePosition", notePosition);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
