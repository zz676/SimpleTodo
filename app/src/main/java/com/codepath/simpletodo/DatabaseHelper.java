package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Zhisheng Zhou
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Log
    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DamageDetection";

    // Table Name
    public static final String TABLE_TASKS = "TASKS";

    //COLUMN NAMES OF TABLE "TASKS"
    public static final String KEY_TASK_ID = "task_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DUE_DATE = "due_date";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_PRIORITY_LEVEL = "priority_level";
    public static final String KEY_STATUS = "status";

    //TASKS table create statement
    private static final String CREATE_TASKS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASKS + "("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_NOTES + " TEXT,"
            + KEY_PRIORITY_LEVEL + " TEXT,"
            + KEY_STATUS + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TASKS);
        onCreate(db);
    }

    public ArrayList<Task> getTasks() {
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " ORDER BY " + KEY_PRIORITY_LEVEL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(0));
                task.setName(cursor.getString(1));
                task.setDuedate(cursor.getString(2));
                task.setNotes(cursor.getString(3));
                task.setPriority_level(cursor.getString(4));
                task.setStatus(cursor.getString(5));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        return tasks;
    }

    public void insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, task.getName());
            values.put(KEY_DUE_DATE, task.getDuedate());
            values.put(KEY_NOTES, task.getNotes());
            values.put(KEY_PRIORITY_LEVEL, task.getPriority_level());
            values.put(KEY_STATUS, task.getStatus());
            db.insert(TABLE_TASKS, null, values);
            db.close();
        } catch (Exception ex) {
            Log.d(TAG, "Insert data into " + TABLE_TASKS + " failed: " + ex.getMessage());
        }
    }

    public boolean deleteTask(int id) {
        Boolean isDeletedSuccessful = false;
        try {
            this.getWritableDatabase().execSQL("delete from " + TABLE_TASKS + " where " + KEY_TASK_ID + " = " + id);
            isDeletedSuccessful = true;
        } catch (Exception ex) {
            Log.d(TAG, "delete task: " + id + " " + TABLE_TASKS + " failed: " + ex.getMessage());
        }
        return isDeletedSuccessful;
    }

    public void updateTask(Task updatedTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, updatedTask.getName());
            values.put(KEY_DUE_DATE, updatedTask.getDuedate());
            values.put(KEY_NOTES, updatedTask.getNotes());
            values.put(KEY_PRIORITY_LEVEL, updatedTask.getPriority_level());
            values.put(KEY_STATUS, updatedTask.getStatus());
            db.update(TABLE_TASKS, values, KEY_TASK_ID + " = " + updatedTask.getId(), null);
            db.close();
        } catch (Exception ex) {
            Log.d(TAG, "update " + TABLE_TASKS + " failed: " + ex.getMessage());
        }
    }
}