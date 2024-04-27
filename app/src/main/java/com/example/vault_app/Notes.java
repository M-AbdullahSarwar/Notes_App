package com.example.vault_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Notes extends AppCompatActivity {

    RecyclerView rvNotes;
    Button btnAddNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_notes_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //init();

//        btnAddNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Notes.this, Add_Note.class));
//
//            }
//        });
    }

    private void init() {
        btnAddNote = findViewById(R.id.btnAddNote1);
        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setHasFixedSize(true);

        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        DataBase dataBase = new DataBase(this);
        dataBase.open();
        ArrayList<NoteClass> notes = dataBase.readAllNotes();
        dataBase.close();

        rvNotes.setAdapter(new NoteAdapter(this, notes));
    }
}