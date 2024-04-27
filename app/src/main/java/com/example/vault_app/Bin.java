package com.example.vault_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Bin extends AppCompatActivity {

    RecyclerView rvDeleted_Notes;
    Del_NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        rvDeleted_Notes = findViewById(R.id.rvDeleted_Notes);
        rvDeleted_Notes.setHasFixedSize(true);
        rvDeleted_Notes.setLayoutManager(new LinearLayoutManager(this));

        DataBase dataBase = new DataBase(this);
        dataBase.open();
        ArrayList<NoteClass> del_notes = dataBase.readAllDeletedNotes();
        dataBase.close();

        adapter = new Del_NoteAdapter(this, del_notes);
        rvDeleted_Notes.setAdapter(adapter);

    }
}