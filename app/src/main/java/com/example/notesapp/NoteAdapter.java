package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> notes;
    private OnNoteClickListener onNoteClickListener;

    public NoteAdapter(ArrayList<Note> notes, OnNoteClickListener onNoteClickListener) {
        this.notes = notes;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate the layout for each note item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        //Set the note text for the current item in the list
        Note note = notes.get(position);
        holder.noteTextView.setText(note.getText());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    //Adds a new note to the list
    public void addNote(Note newNote) {
        notes.add(newNote);
        //Notify adapter of change
        notifyItemInserted(notes.size());
    }

    //Updates an existing note
    public void updateNote(int position, Note newNote) {
        notes.set(position, newNote);
        //Notify adapter of change

        notifyItemChanged(position);
    }

    //Removes a note from the list
    public void removeNote(int position) {
        notes.remove(position);
        //Notify adapter of change
        notifyItemRemoved(position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);

            //Click listener for editing a note
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClickListener.onNoteClick(position);
                }
            });

            //LongClick listener for deleting a note
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClickListener.onNoteLongClick(position);
                }
                return true;
            });
        }
    }

    //Interface for handling click and long-click events
    public interface OnNoteClickListener {
        void onNoteClick(int position);
        void onNoteLongClick(int position);
    }
}
