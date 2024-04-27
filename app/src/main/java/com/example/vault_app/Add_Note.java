package com.example.vault_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add_Note extends AppCompatActivity {

    EditText etName_Note, etPassword_Note, etUrl_Note;
    Button btnAdd_Note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnAdd_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName_Note.getText().toString().trim();
                String pass = etPassword_Note.getText().toString().trim();
                String url = etUrl_Note.getText().toString().trim();

                if (name.isEmpty() || pass.isEmpty() || url.isEmpty())
                {
                    Toast.makeText(Add_Note.this, "Input can't be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                DataBase dataBase = new DataBase(Add_Note.this);

                dataBase.open();
                dataBase.insertNote(name, pass, url);
                dataBase.close();

                startActivity(new Intent(Add_Note.this, Notes.class));
                finish();
            }
        });
    }

    private void init() {
        etName_Note = findViewById(R.id.etName_Note);
        etPassword_Note = findViewById(R.id.etPassword_Note);
        etUrl_Note = findViewById(R.id.etURL_Note);

        btnAdd_Note = findViewById(R.id.btnAdd_Note);
    }
}