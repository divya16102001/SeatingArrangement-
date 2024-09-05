package com.example.seatingarrangement;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {

    private EditText regNoInput, nameInput, branchInput, semesterInput, schemeInput;
    private RadioGroup examNameGroup;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        regNoInput = findViewById(R.id.regNo);
        nameInput = findViewById(R.id.name);
        branchInput = findViewById(R.id.branch);
        semesterInput = findViewById(R.id.semester);
        schemeInput = findViewById(R.id.scheme);
        examNameGroup = findViewById(R.id.examNameGroup);
        Button addButton = findViewById(R.id.addButton);

        databaseHelper = new DatabaseHelper(this);

        // Set input filter to limit the registration number to 4 digits
        regNoInput.setFilters(new InputFilter[]{new LengthFilter(4)});
        regNoInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
    }

    private void addStudent() {
        String regNo = regNoInput.getText().toString();
        String name = nameInput.getText().toString();
        String branch = branchInput.getText().toString();
        String semester = semesterInput.getText().toString();
        String scheme = schemeInput.getText().toString();
        String examName = getSelectedExamName();

        // Validate registration number length
        if (regNo.length() != 4) {
            Toast.makeText(AddStudentActivity.this, "Enter a 4-digit registration number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (examName == null) {
            Toast.makeText(AddStudentActivity.this, "Please select an exam type", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = databaseHelper.addStudent(regNo, name, branch, semester, scheme, examName);

    if (isInserted) {
            Toast.makeText    (AddStudentActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddStudentActivity.this, "Error adding student", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedExamName() {
        int selectedId = examNameGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return null;
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }
}
