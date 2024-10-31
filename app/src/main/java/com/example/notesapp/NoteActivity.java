package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
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

        //Initialize editText field
        noteEditText = findViewById(R.id.noteEditText);

        //Retrieve note position from the intent
        Intent intent = getIntent();
        notePosition = intent.getIntExtra("notePosition", -1);

        //Check if theres exiting text to edit
        String noteText = intent.getStringExtra("noteText");
        if (noteText != null) {
            //Add existing text to the editText
            noteEditText.setText(noteText);
        }

        //Set up the save button
        Button saveNoteButton = findViewById(R.id.saveNoteButton);
        saveNoteButton.setOnClickListener(v -> {

            //Get the new text from the editText
            String newNoteText = noteEditText.getText().toString();

            //Pass back an Intent to MainActivity with the new text
            Intent resultIntent = new Intent();
            resultIntent.putExtra("noteText", newNoteText);
            resultIntent.putExtra("notePosition", notePosition); // position might be off?
            setResult(RESULT_OK, resultIntent);

            //End activity and return to MainActivity
            finish();
        });
    }

}
