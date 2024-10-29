package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteTextView.setText(note.getText());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void addNote(Note newNote) {
        notes.add(newNote);
        notifyItemInserted(notes.size()-1);
    }

    public void updateNote(int index, Note newNote) {
        notes.set(index, newNote);
        notifyItemChanged(index);
    }

    public void removeNote(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);

            //Click listener for editing
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    onNoteClickListener.onNoteClick(position);
                }
            });

            //LongClick listener for deleting
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClickListener.onNoteLongClick(position);
                }
                return true;
            });
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position);
        void onNoteLongClick(int position);
    }
}
