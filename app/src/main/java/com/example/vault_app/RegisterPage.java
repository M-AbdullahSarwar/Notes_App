package com.example.vault_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterPage extends AppCompatActivity {

    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPass;
    RadioButton rbtnMale, rbtnFemale;
    Button btnRegister, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gender;

                String fname = etFirstName.getText().toString().trim();
                String lname = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String confirmpass = etConfirmPass.getText().toString().trim();

                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()){
                    Toast.makeText(RegisterPage.this, "Input can not be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(rbtnMale.isChecked()){
                        gender = "Male";
                    }
                    else if (rbtnFemale.isChecked()) {
                        gender = "Female";
                    }
                    else {
                        Toast.makeText(RegisterPage.this, "Gender is not Selected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pass.equals(confirmpass)){

                        //save details in DataBase
                        registerUser(fname, lname, email, pass, gender);

                        Intent intent = new Intent(RegisterPage.this, com.example.vault_app.LoginPage.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegisterPage.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterPage.this, "Registration Cancelled", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterPage.this, com.example.vault_app.LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String fname, String lname, String email, String pass, String gender)
    {
        DataBase db = new DataBase(this);

        db.open();
        db.insertLoginUser(fname, lname, email, pass, gender);
        db.close();
    }
    private void init(){
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);
        rbtnMale = findViewById(R.id.rbtnMale);
        rbtnFemale = findViewById(R.id.rbtnFemale);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

    }
}