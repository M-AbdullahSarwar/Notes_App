package com.example.vault_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    ArrayList<NoteClass> notes;

    public NoteAdapter(Context c, ArrayList<NoteClass> notes)
    {
        context = c;
        this.notes = notes;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName_Note, tvPassword_Note, tvUrl_note;
        ImageView ivUpdate_Note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName_Note = itemView.findViewById(R.id.tvName_Note);
            tvPassword_Note = itemView.findViewById(R.id.tvPassword_Note);
            tvUrl_note = itemView.findViewById(R.id.tvUrl_Note);
            ivUpdate_Note = itemView.findViewById(R.id.imgUpdate_Note);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName_Note.setText(notes.get(position).getName());
        holder.tvPassword_Note.setText(notes.get(position).getPassword());
        holder.tvUrl_note.setText(notes.get(position).getUrl());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
