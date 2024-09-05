package com.example.seatingarrangement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class UserSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        Button adminButton = findViewById(R.id.adminButton);
        Button studentButton = findViewById(R.id.studentButton);

        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserSelectionActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserSelectionActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });
    }
}
