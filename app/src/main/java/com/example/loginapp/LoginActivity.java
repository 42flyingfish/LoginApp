package com.example.loginapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        DatabaseHandler helper = new DatabaseHandler(LoginActivity.this);


        final Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText userField = findViewById(R.id.username);
                EditText passwordField = findViewById(R.id.password);

                String localUser = userField.getText().toString();
                String localPassword = passwordField.getText().toString();

                if (localUser.isEmpty()) {
                    breadMessage("Username is empty");
                    return;
                } else if (localPassword.isEmpty()) {
                    breadMessage("Password is empty");
                    return;
                }

                User user = new User(localUser, localPassword);


                // Return early if the user already exists.
                if (helper.userExists(user)) {
                    breadMessage("The user already exists");
                    return;
                }

                // Register the user
                boolean result = helper.insertData(user);

                if (result) {
                    breadMessage("New user registered");
                } else {
                    breadMessage("Failed to register the user");
                }

            }
        });

        final Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userField = findViewById(R.id.username);
                EditText passwordField = findViewById(R.id.password);

                String localUser = userField.getText().toString();
                String localPassword = passwordField.getText().toString();

                if (localUser.isEmpty()) {
                    breadMessage("Username is empty");
                    return;
                } else if (localPassword.isEmpty()) {
                    breadMessage("Password is empty");
                    return;
                }

                User user = new User(localUser, localPassword);

                if (!helper.userExists(user)) {
                    breadMessage("The user does not exist");
                    return;
                }

                if (!helper.userMatch(user)) {
                    breadMessage("Incorrect password");
                    return;
                }

                breadMessage("Login Successful");

                Intent nextBestThing = new Intent(LoginActivity.this, ActivityHome.class);
                LoginActivity.this.startActivity(nextBestThing);

            }
        });

    }

    // Displays a toast message. This was to reduce writing this repeatably.
    // Also, I was using this for debugging.
    protected void breadMessage(String str) {
        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_LONG).show();
    }

}