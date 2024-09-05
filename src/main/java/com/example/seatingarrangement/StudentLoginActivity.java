package com.example.seatingarrangement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentLoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        dbHelper = new DatabaseHelper(this);
        EditText nameInput = findViewById(R.id.name);
        EditText regNo = findViewById(R.id.regNo);
        EditText password = findViewById(R.id.password);
        EditText branch = findViewById(R.id.branch);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String nameStr = nameInput.getText().toString();
            String regNoStr = regNo.getText().toString();
            String passwordStr = password.getText().toString();
            String branchStr = branch.getText().toString();

            // Check if the password is the same as the name
            if (!passwordStr.equals(nameStr)) {
                Toast.makeText(StudentLoginActivity.this, "Password must be the same as the name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.validateStudent(regNoStr, passwordStr, branchStr)) {
                Intent intent = new Intent(StudentLoginActivity.this, StudentSeatingActivity.class);
                intent.putExtra("regNo", regNoStr);
                startActivity(intent);
            } else {
                Toast.makeText(StudentLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
