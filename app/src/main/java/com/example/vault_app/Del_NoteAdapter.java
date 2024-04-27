package com.example.vault_app;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Del_NoteAdapter extends RecyclerView.Adapter<Del_NoteAdapter.ViewHolder> {

    Context context;
    ArrayList<NoteClass> del_notes;

    public Del_NoteAdapter(Context c, ArrayList<NoteClass> list) {
        context = c;
        del_notes = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDelNote_Name, tvDelNote_Pass, tvDelNote_Url;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDelNote_Name = itemView.findViewById(R.id.tvName_Del_Note);
            tvDelNote_Pass = itemView.findViewById(R.id.tvPassword_Del_Note);
            tvDelNote_Url = itemView.findViewById(R.id.tvUrl_Del_Note);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_deleted_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDelNote_Name.setText(del_notes.get(position).getName());
        holder.tvDelNote_Pass.setText(del_notes.get(position).getPassword());
        holder.tvDelNote_Url.setText(del_notes.get(position).getUrl());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");

                builder.setMessage("Do you really want to Restore this Note");

                builder.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBase dataBase = new DataBase(context);

                        dataBase.open();
                        dataBase.restoreDeletedNote(del_notes.get(holder.getAdapterPosition()).getId());
                        dataBase.close();

                        del_notes.remove(holder.getAdapterPosition());
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
        return del_notes.size();
    }
}
