package com.example.seatingarrangement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AllocateHallActivity extends AppCompatActivity {

    private RadioGroup branchRadioGroup;
    private EditText capacityInput, columnsInput, startRegNoInput, dateInput, timeInput;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_hall);

        branchRadioGroup = findViewById(R.id.branchRadioGroup);
        capacityInput = findViewById(R.id.capacity);
        columnsInput = findViewById(R.id.columns);
        startRegNoInput = findViewById(R.id.startRegNo);
        dateInput = findViewById(R.id.date);
        timeInput = findViewById(R.id.time);
        Button allocateButton = findViewById(R.id.allocateButton);

        databaseHelper = new DatabaseHelper(this);

        allocateButton.setOnClickListener(v -> allocateSeating());
    }

    private void allocateSeating() {
        int selectedBranchId = branchRadioGroup.getCheckedRadioButtonId();

        if (selectedBranchId == -1) {
            Toast.makeText(this, "Please select a branch", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedBranchButton = findViewById(selectedBranchId);
        String branch = selectedBranchButton.getText().toString();
        int capacity = Integer.parseInt(capacityInput.getText().toString());
        int columns = Integer.parseInt(columnsInput.getText().toString());
        String startRegNo = startRegNoInput.getText().toString();
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();
        String roomNo;

        // Determine room number based on branch
        if (branch.equalsIgnoreCase("CS")) {
            roomNo = "101CS";
        } else if (branch.equalsIgnoreCase("IT")) {
            roomNo = "102IT";
        } else if (branch.equalsIgnoreCase("EC")) {
            roomNo = "103EC";
        } else if (branch.equalsIgnoreCase("CE")) {
            roomNo = "104CE";
        } else {
            Toast.makeText(this, "Invalid branch selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor studentsCursor = databaseHelper.getStudentsByBranchSortedByRegNo(branch);
        if (studentsCursor != null && studentsCursor.moveToFirst()) {
            int row = 0;
            int column = 0;
            String[][] seatingArrangement = new String[capacity / columns][columns];
            String lastSemester = null; // Track the last assigned semester

            do {
                // Ensure columns exist in the cursor
                int regNoIndex = studentsCursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_REG_NO);
                int semesterIndex = studentsCursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_SEMESTER);

                if (regNoIndex == -1 || semesterIndex == -1) {
                    Toast.makeText(this, "Database column index not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regNo = studentsCursor.getString(regNoIndex);
                String semester = studentsCursor.getString(semesterIndex);

                // Check if semester changes and reset column count for new row
                if (lastSemester != null && !semester.equals(lastSemester)) {
                    row++; // Move to the next row for a new semester
                    column = 0; // Start from the first column
                }

                // Ensure no students of the same semester sit next to each other
                while (seatingArrangement[row][column] != null) {
                    column++; // Move to the next column if current one is occupied
                    if (column >= columns) {
                        column = 0;
                        row++; // Move to the next row if all columns are occupied
                    }
                }

                // Assign the seat to the current student
                seatingArrangement[row][column] = regNo;
                lastSemester = semester; // Update last assigned semester

                // Move to the next column
                column++;
                if (column >= columns) {
                    column = 0;
                    row++; // Move to the next row if all columns are occupied
                }

            } while (studentsCursor.moveToNext() && row < seatingArrangement.length); // Continue until seats are filled

            databaseHelper.addHallAllocation(branch, roomNo, capacity, columns, startRegNo, date, time, seatingArrangement);
            Toast.makeText(this, "Seating allocated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No students found for this branch", Toast.LENGTH_SHORT).show();
        }
    }
}
