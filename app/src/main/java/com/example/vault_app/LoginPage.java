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

public class LoginPage extends AppCompatActivity {

    EditText etLogin_Email, etLogin_Pass;
    Button btnLogin;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etLogin_Email = findViewById(R.id.etLogin_Email);
        etLogin_Pass = findViewById(R.id.etLogin_Password);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        if(isUserLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, RegisterPage.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etLogin_Email.getText().toString().trim();
                String pass = etLogin_Pass.getText().toString().trim();

                if(email.isEmpty() || pass.isEmpty())
                    Toast.makeText(LoginPage.this, "Entries can not be Empty", Toast.LENGTH_SHORT).show();

                DataBase db = new DataBase(LoginPage.this);

                db.open();
                int status = db.LoginRegisteredUser(email, pass);
                db.close();

                if (status <= 0)
                {
                    Toast.makeText(LoginPage.this, "Invalid mail or password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(LoginPage.this, com.example.vault_app.MainActivity.class));
                finish();

            }
        });

    }

    private boolean isUserLoggedIn() {
        DataBase db = new DataBase(this);

        db.open();
        boolean res = db.isUserLoggedIn();
        db.close();

        return res;
    }
}