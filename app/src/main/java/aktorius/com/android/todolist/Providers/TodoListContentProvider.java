package aktorius.com.android.todolist.Providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import aktorius.com.android.todolist.Contracts.TodoListContract;
import aktorius.com.android.todolist.Helpers.TodoListDBHelper;

/**
 * Created by Aktorius on 06/04/2017.
 */

public class TodoListContentProvider extends ContentProvider {
    private static final int TODO = 100;
    private static final int TODO_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private TodoListDBHelper dbHelper;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoListContract.CONTENT_AUTHORITHY;

        matcher.addURI(authority, TodoListContract.PATH_TODO, TODO);
        matcher.addURI(authority, TodoListContract.PATH_TODO +"/#", TODO_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new TodoListDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)){
            case TODO: {
                cursor = dbHelper.getReadableDatabase().query(
                        TodoListContract.TodoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TODO_ID: {
                cursor = dbHelper.getReadableDatabase().query(
                        TodoListContract.TodoEntry.TABLE_NAME,
                        projection,
                        TodoListContract.TodoEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri toReturn;

        switch (match){
            case TODO:
                long _id = db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, values);
                if(_id>0)
                    toReturn = TodoListContract.TodoEntry.CONTENT_URI;
                else
                    throw new android.database.SQLException("Failed to insert row in "+ uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return toReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowDeleted;

        switch (match){
            case TODO:
                rowDeleted = db.delete(
                        TodoListContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }
        if(selection == null || rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowUpdated;

        switch (match){
            case TODO:
                rowUpdated = db.update(
                        TodoListContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }
        if(rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match){
            case TODO:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values){
                        long _id = db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            count ++;
                        }
                    }
                    db.setTransactionSuccessful();
                }catch (Exception ex){
                    Log.e("BULKINSERT", ex.getMessage());
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
