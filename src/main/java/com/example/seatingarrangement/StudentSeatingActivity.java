package com.example.seatingarrangement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentSeatingActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView seatingDetailsTextView;
    private GridLayout seatingGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_seating);

        seatingDetailsTextView = findViewById(R.id.seatingDetails);
        seatingGrid = findViewById(R.id.seatingGrid);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String regNo = intent.getStringExtra("regNo");

        if (regNo != null) {
            displaySeatingDetails(regNo);
        }
    }

    private void displaySeatingDetails(String regNo) {
        Cursor cursor = dbHelper.getSeatingDetails(regNo);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int branchIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ALLOCATION_BRANCH);
                int roomNoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ALLOCATION_ROOM_NO);
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ALLOCATION_DATE);
                int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ALLOCATION_TIME);
                int seatingArrangementIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ALLOCATION_SEATING);

                // Check if all indices are valid
                if (branchIndex >= 0 && roomNoIndex >= 0 && dateIndex >= 0 && timeIndex >= 0 && seatingArrangementIndex >= 0) {
                    String branch = cursor.getString(branchIndex);
                    String roomNo = cursor.getString(roomNoIndex);
                    String date = cursor.getString(dateIndex);
                    String time = cursor.getString(timeIndex);
                    String seatingArrangement = cursor.getString(seatingArrangementIndex);

                    String seatingDetails = "Branch: " + branch + "\n" +
                            "Room No: " + roomNo + "\n" +
                            "Date: " + date + "\n" +
                            "Time: " + time + "\n" +
                            "Seating Arrangement:\n";

                    seatingDetailsTextView.setText(seatingDetails);

                    String[][] seatingArray = convertStringToSeatingArray(seatingArrangement);
                    seatingGrid.removeAllViews(); // Clear any existing views

                    for (String[] row : seatingArray) {
                        for (int i = 0; i < 4; i++) { // Ensure 4 seats per line
                            TextView seatView = new TextView(this);
                            seatView.setText(row[i] == null ? "Empty" : row[i]);
                            seatView.setTextSize(18);
                            seatView.setPadding(8, 8, 8, 8);
                            seatView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.width = 0; // Use 0 width and set weight to 1 to ensure equal spacing
                            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                            params.columnSpec = GridLayout.spec(i, 1, 1f); // Span 1 column with weight 1
                            params.setMargins(8, 8, 8, 8);
                            seatView.setLayoutParams(params);

                            seatingGrid.addView(seatView);
                        }
                    }
                } else {
                    seatingDetailsTextView.setText("Invalid column index.");
                }
            } finally {
                cursor.close();
            }
        } else {
            seatingDetailsTextView.setText("No seating details found for the given registration number.");
        }
    }

    private String[][] convertStringToSeatingArray(String seatingString) {
        String[] rows = seatingString.split(";");
        String[][] seatingArrangement = new String[rows.length][4]; // Assuming 4 seats per row

        for (int i = 0; i < rows.length; i++) {
            String[] seats = rows[i].split(",");
            for (int j = 0; j < 4; j++) {
                seatingArrangement[i][j] = seats[j].equals("null") ? null : seats[j];
            }
        }

        return seatingArrangement;
    }
}
