package com.example.seatingarrangement;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class ViewStudentActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView studentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        dbHelper = new DatabaseHelper(this);
        studentListView = findViewById(R.id.studentListView);

        loadStudentData();
    }

    private void loadStudentData() {
        Cursor cursor = dbHelper.getAllStudents();
        if (cursor == null) {
            Log.e("ViewStudentActivity", "Cursor is null");
            return;
        }
        if (cursor.getCount() == 0) {
            Log.e("ViewStudentActivity", "No student data found");
            return;
        }

        String[] fromColumns = {
                DatabaseHelper.COLUMN_STUDENT_REG_NO,
                DatabaseHelper.COLUMN_STUDENT_NAME,
                DatabaseHelper.COLUMN_STUDENT_BRANCH,
                DatabaseHelper.COLUMN_STUDENT_SEMESTER,
                DatabaseHelper.COLUMN_STUDENT_SCHEME,
                DatabaseHelper.COLUMN_STUDENT_EXAM_NAME
        };

        int[] toViews = {
                R.id.regNo, R.id.name, R.id.branch, R.id.semester, R.id.scheme, R.id.examName
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.student_list_item,
                cursor,
                fromColumns,
                toViews,
                0
        );

        studentListView.setAdapter(adapter);
    }
}
