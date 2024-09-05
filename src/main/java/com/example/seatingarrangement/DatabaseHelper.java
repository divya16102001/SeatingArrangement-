package com.example.seatingarrangement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "seating_arrangement.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    static final String TABLE_STUDENT = "student";
    private static final String TABLE_HALL_ALLOCATION = "hall_allocation";

    // Common column names
    private static final String COLUMN_ID = "id";

    // STUDENT Table - column names
    public static final String COLUMN_STUDENT_REG_NO = "reg_no";
    public static final String COLUMN_STUDENT_NAME = "name";
    public static final String COLUMN_STUDENT_BRANCH = "branch";
    public static final String COLUMN_STUDENT_SEMESTER = "semester";
    public static final String COLUMN_STUDENT_SCHEME = "scheme";
    public static final String COLUMN_STUDENT_EXAM_NAME = "exam_name";
    public static final String COLUMN_STUDENT_PASSWORD = "password"; // Added for student password

    // HALL_ALLOCATION Table - column names
    public static final String COLUMN_ALLOCATION_BRANCH = "branch";
    public static final String COLUMN_ALLOCATION_ROOM_NO = "room_no";
    public static final String COLUMN_ALLOCATION_CAPACITY = "capacity";
    public static final String COLUMN_ALLOCATION_COLUMNS = "columns";
    public static final String COLUMN_ALLOCATION_START_REG_NO = "start_reg_no";
    public static final String COLUMN_ALLOCATION_DATE = "date";
    public static final String COLUMN_ALLOCATION_TIME = "time";
    public static final String COLUMN_ALLOCATION_SEATING = "seating_arrangement";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating required tables
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STUDENT_REG_NO + " TEXT,"
                + COLUMN_STUDENT_NAME + " TEXT,"
                + COLUMN_STUDENT_BRANCH + " TEXT,"
                + COLUMN_STUDENT_SEMESTER + " TEXT,"
                + COLUMN_STUDENT_SCHEME + " TEXT,"
                + COLUMN_STUDENT_EXAM_NAME + " TEXT,"
                + COLUMN_STUDENT_PASSWORD + " TEXT" + ")";

        String CREATE_HALL_ALLOCATION_TABLE = "CREATE TABLE " + TABLE_HALL_ALLOCATION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ALLOCATION_BRANCH + " TEXT,"
                + COLUMN_ALLOCATION_ROOM_NO + " TEXT,"
                + COLUMN_ALLOCATION_CAPACITY + " INTEGER,"
                + COLUMN_ALLOCATION_COLUMNS + " INTEGER,"
                + COLUMN_ALLOCATION_START_REG_NO + " TEXT,"
                + COLUMN_ALLOCATION_DATE + " TEXT,"
                + COLUMN_ALLOCATION_TIME + " TEXT,"
                + COLUMN_ALLOCATION_SEATING + " TEXT" + ")";

        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_HALL_ALLOCATION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HALL_ALLOCATION);

        // Create tables again
        onCreate(db);
    }

    // Adding a new student
    public boolean addStudent(String regNo, String name, String branch, String semester, String scheme, String examName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_REG_NO, regNo);
        values.put(COLUMN_STUDENT_NAME, name);
        values.put(COLUMN_STUDENT_BRANCH, branch);
        values.put(COLUMN_STUDENT_SEMESTER, semester);
        values.put(COLUMN_STUDENT_SCHEME, scheme);
        values.put(COLUMN_STUDENT_EXAM_NAME, examName);
        values.put(COLUMN_STUDENT_PASSWORD, name); // Set password as student's name

        // Inserting Row
        long result = db.insert(TABLE_STUDENT, null, values);
        return result != -1; // returns true if inserted successfully, false otherwise
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_STUDENT,
                null,
                null, null, null, null, null);
    }


    public boolean validateStudent(String regNo, String password, String branch) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENT,
                null,
                COLUMN_STUDENT_REG_NO + "=? AND " + COLUMN_STUDENT_PASSWORD + "=? AND " + COLUMN_STUDENT_BRANCH + "=?",
                new String[]{regNo, password, branch},
                null,
                null,
                null);

        boolean isValid = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
    }

    public Cursor getStudentsByBranchSortedByRegNo(String branch) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(TABLE_STUDENT,
                null,
                COLUMN_STUDENT_BRANCH + "=?",
                new String[]{branch},
                null,
                null,
                COLUMN_STUDENT_REG_NO + " ASC"); // Sort by registration number ascending
    }


    // Validating admin credentials
    public boolean validateAdmin(String adminName, String password) {
        // For demonstration, hardcoded admin credentials
        return adminName.equals("admin") && password.equals("password123");
    }

    // Adding a new hall allocation
    public void addHallAllocation(String branch, String roomNo, int capacity, int columns, String startRegNo, String date, String time, String[][] seatingArrangement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ALLOCATION_BRANCH, branch);
        values.put(COLUMN_ALLOCATION_ROOM_NO, roomNo);
        values.put(COLUMN_ALLOCATION_CAPACITY, capacity);
        values.put(COLUMN_ALLOCATION_COLUMNS, columns);
        values.put(COLUMN_ALLOCATION_START_REG_NO, startRegNo);
        values.put(COLUMN_ALLOCATION_DATE, date);
        values.put(COLUMN_ALLOCATION_TIME, time);
        values.put(COLUMN_ALLOCATION_SEATING, convertSeatingArrayToString(seatingArrangement));

        // Inserting Row
        db.insert(TABLE_HALL_ALLOCATION, null, values);
    }

    public Cursor getSeatingDetails(String regNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_HALL_ALLOCATION + " WHERE " + COLUMN_ALLOCATION_START_REG_NO + "=?", new String[]{regNo});
    }

    // Converting seating arrangement array to string
    // Converting seating arrangement array to string
    private String convertSeatingArrayToString(String[][] seatingArrangement) {
        StringBuilder seatingStringBuilder = new StringBuilder();
        for (String[] row : seatingArrangement) {
            for (int i = 0; i < 4; i++) { // Ensure 4 seats per row
                seatingStringBuilder.append(row[i] == null ? "null" : row[i]).append(",");
            }
            seatingStringBuilder.deleteCharAt(seatingStringBuilder.length() - 1); // Remove the last comma
            seatingStringBuilder.append(";");
        }
        return seatingStringBuilder.toString();
    }

}
