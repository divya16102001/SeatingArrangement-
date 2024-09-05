package com.example.seatingarrangement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;  // Import this package
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private EditText adminNameInput;
    private EditText adminPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        dbHelper = new DatabaseHelper(this);

        adminNameInput = findViewById(R.id.adminName);
        adminPasswordInput = findViewById(R.id.adminPassword);
        Button loginButton = findViewById(R.id.loginButton);

        // Make the fields visible
        adminNameInput.setVisibility(View.VISIBLE);
        adminPasswordInput.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(v -> {
            String name = adminNameInput.getText().toString();
            String password = adminPasswordInput.getText().toString();

            if (dbHelper.validateAdmin(name, password)) {
                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AdminLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
