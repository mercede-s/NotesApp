package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {

    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Initialize EditText and Save button
        noteEditText = findViewById(R.id.noteEditText);
        Button saveNoteButton = findViewById(R.id.saveNoteButton);

        //Set click listener for save button
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Supposedly gets the note text and sends it back to MainActivity
                String noteText = noteEditText.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("noteText", noteText);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
