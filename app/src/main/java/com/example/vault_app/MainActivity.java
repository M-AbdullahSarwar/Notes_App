package com.example.vault_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView ivProfile;
    LinearLayout llLogins, llBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        ivProfile.setOnClickListener(new View.OnClickListener() {
            // Show who is currently logged In and an option to log out as well

            @Override
            public void onClick(View v) {
                // attaching/inflating xml file to a view
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_logout, null);

                TextView tvProfileName = dialogView.findViewById(R.id.tvName);
                Button btnLogout = dialogView.findViewById(R.id.btnLogout);

                // attaching view on alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                tvProfileName.setText(getLoggedInUserName());

                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataBase database = new DataBase(MainActivity.this);

                        database.open();
                        database.logoutUser();
                        database.close();

                        startActivity(new Intent(MainActivity.this, LoginPage.class));
                        finish();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        llLogins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Notes.class));
            }
        });
    }

    private String getLoggedInUserName() {
        DataBase db = new DataBase(this);

        db.open();
        String userName = db.getLoggedInUser();
        db.close();

        return userName;
    }
    private void init() {
        ivProfile = findViewById(R.id.ivProfile);
        llLogins = findViewById(R.id.llLogin);
        llBin = findViewById(R.id.llBin);
    }
}