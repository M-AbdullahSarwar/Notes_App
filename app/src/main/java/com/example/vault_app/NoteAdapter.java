package com.example.vault_app;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

        holder.ivUpdate_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(context).create();
                View view = LayoutInflater.from(context).inflate(R.layout.update_note_dialog, null, false);
                dialog.setView(view);

                EditText etName_Note = view.findViewById(R.id.etNameup_Note);
                EditText etPassword_Note = view.findViewById(R.id.etPasswordup_Note);
                EditText etUrl_Note = view.findViewById(R.id.etURLup_Note);
                Button btnUpdate = view.findViewById(R.id.btnUpdate_Note);

                etName_Note.setText(notes.get(holder.getAdapterPosition()).getName());
                etPassword_Note.setText(notes.get(holder.getAdapterPosition()).getPassword());
                etUrl_Note.setText(notes.get(holder.getAdapterPosition()).getUrl());

                dialog.show();
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName_Note.getText().toString().trim();
                        String password = etPassword_Note.getText().toString().trim();
                        String url = etUrl_Note.getText().toString().trim();

                        if (name.isEmpty() || password.isEmpty() || url.isEmpty()) {
                            Toast.makeText(context, "Input can't be Empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DataBase dataBase = new DataBase(context);
                        dataBase.open();
                        dataBase.updateNote(notes.get(holder.getAdapterPosition()).getId(), name, password, url);
                        dataBase.close();

                        dialog.dismiss();

                        notes.get(holder.getAdapterPosition()).setName(name);
                        notes.get(holder.getAdapterPosition()).setPassword(password);
                        notes.get(holder.getAdapterPosition()).setUrl(url);

                        notifyDataSetChanged();
                    }
                });

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");

                builder.setMessage("Do you really want to delete the Note");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBase dataBase = new DataBase(context);
                        dataBase.open();

                        dataBase.deleteNote(notes.get(holder.getAdapterPosition()).getId());

                        dataBase.close();

                        notes.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
