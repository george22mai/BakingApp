package com.bakingapp.Utilities.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DatabaseContentProvider extends ContentProvider {

    private DatabaseHelper database;

    private static final int MOVIE = 10;
    private static final int MOVIE_ID = 20;
    public static final String BASE_PATH = "content://";
    public static final String PACKAGE = "com.bakingapp";

    private static final UriMatcher sURIMathcer = new UriMatcher(UriMatcher.NO_MATCH);


    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Table.TABLE);
        int uriType = sURIMathcer.match(uri);
        switch (uriType){
            case MOVIE:
                break;
            case MOVIE_ID:
                queryBuilder.appendWhere(Table.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
        }
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = database.getWritableDatabase();
        int match =  sURIMathcer.match(uri);
        long id = 0;
        id = db.insert(Table.TABLE, null, contentValues);
        uri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete(Table.TABLE, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

}
