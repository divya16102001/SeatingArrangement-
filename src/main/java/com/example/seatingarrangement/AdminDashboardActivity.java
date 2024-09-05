package com.example.seatingarrangement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button addStudentButton = findViewById(R.id.addStudentButton);
        Button viewStudentButton = findViewById(R.id.viewStudentButton);
        Button allocateHallButton = findViewById(R.id.allocateHallButton);

        addStudentButton.setText(getString(R.string.add_student));
        viewStudentButton.setText(getString(R.string.view_students));
        allocateHallButton.setText(getString(R.string.allocate_hall));

        addStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddStudentActivity.class);
            startActivity(intent);
        });

        viewStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ViewStudentActivity.class);
            startActivity(intent);
        });

        allocateHallButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AllocateHallActivity.class);
            startActivity(intent);
        });
    }
}
