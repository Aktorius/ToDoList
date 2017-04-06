package aktorius.com.android.todolist.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aktorius.com.android.todolist.Contracts.TodoListContract;

/**
 * Created by Aktorius on 06/04/2017.
 */

public class TodoListDBHelper extends SQLiteOpenHelper {

    //MUST BE UPDATED WHEN CHANGING DB SCHEMA
    private static final int DB_VERSION = 1;

    public static final String DB_NAME = "todolist.db";

    public TodoListDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TODO_TABLE = "CREATE TABLE" + TodoListContract.TodoEntry.TABLE_NAME + "("+
                TodoListContract.TodoEntry._ID + "INTEGER PRIMARY KEY," +
                TodoListContract.TodoEntry.COLUMN_DATE + "INTEGER PRIMARY KEY," +
                TodoListContract.TodoEntry.COLUMN_DESCRIPTION + "INTEGER PRIMARY KEY," +
                TodoListContract.TodoEntry.COLUMN_DONE + "INTEGER PRIMARY KEY," +
                "UNIQUE (" + TodoListContract.TodoEntry.COLUMN_DATE +", " + TodoListContract.TodoEntry.COLUMN_DESCRIPTION +") ON"+
                "CONFLICT IGNORE" +
                ");";
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TodoListContract.TodoEntry.TABLE_NAME);
        onCreate(db);
    }
}
