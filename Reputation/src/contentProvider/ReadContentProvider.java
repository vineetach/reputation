package contentProvider;

import java.util.HashMap;

import contentProvider.ReadContentProviderMetaData.ReadContentTableMetaData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ReadContentProvider extends ContentProvider {
    private ReadContentDBHelper mReadContentDB;

    private static final int READ_DATA_SINGLE_ID = 1;
    private static final int READ_DATA_MULTI_ID = 2;

    private static HashMap<String, String> sReadContentProjectionMap;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private class ReadContentDBHelper extends SQLiteOpenHelper {

        public ReadContentDBHelper(Context context) {
            super(context, UnreadContentProviderMetaData.DATABASE_NAME, 
                    null, 
                    UnreadContentProviderMetaData.DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + ReadContentTableMetaData.READ_CONTENT_TABLE_NAME + " (" +
                    ReadContentTableMetaData._ID + " INTEGER PRIMARY KEY," +
                    ReadContentTableMetaData.FIRST_NAME + " TEXT, " +
                    ReadContentTableMetaData.LAST_NAME + " TEXT, " +
                    ReadContentTableMetaData.CONTENT_TEXT + " TEXT, " +
                    ReadContentTableMetaData.RATING + " INTEGER, " +
                    ReadContentTableMetaData.READ_CONTENT_ID + " TEXT, " +
                    ReadContentTableMetaData.CREATED_DATE + " INTEGER, " +
                    ReadContentTableMetaData.MODIFIED_DATE + " INTEGER, " +
                    "UNIQUE(" + ReadContentTableMetaData.READ_CONTENT_ID + ") ON CONFLICT IGNORE);" );
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ReadContentTableMetaData.READ_CONTENT_TABLE_NAME);

            // Create tables again
            onCreate(db);
        }
    }

    static {
        sReadContentProjectionMap = new HashMap<String, String>();

        sReadContentProjectionMap.put(ReadContentTableMetaData._ID,ReadContentTableMetaData._ID);

        sReadContentProjectionMap.put(ReadContentTableMetaData.FIRST_NAME, ReadContentTableMetaData.FIRST_NAME);
        sReadContentProjectionMap.put(ReadContentTableMetaData.LAST_NAME, ReadContentTableMetaData.LAST_NAME);
        sReadContentProjectionMap.put(ReadContentTableMetaData.CONTENT_TEXT, ReadContentTableMetaData.CONTENT_TEXT);
        sReadContentProjectionMap.put(ReadContentTableMetaData.RATING, ReadContentTableMetaData.RATING);
        sReadContentProjectionMap.put(ReadContentTableMetaData.READ_CONTENT_ID, ReadContentTableMetaData.READ_CONTENT_ID);
        sReadContentProjectionMap.put(ReadContentTableMetaData.CREATED_DATE, ReadContentTableMetaData.CREATED_DATE);
        sReadContentProjectionMap.put(ReadContentTableMetaData.MODIFIED_DATE, ReadContentTableMetaData.MODIFIED_DATE);
    }

    static {
        sURIMatcher.addURI(UnreadContentProviderMetaData.AUTHORITY, UnreadContentProviderMetaData.UNREAD_CONTENT_BASE_PATH, READ_DATA_MULTI_ID);
        sURIMatcher.addURI(UnreadContentProviderMetaData.AUTHORITY, UnreadContentProviderMetaData.UNREAD_CONTENT_BASE_PATH + "/#", READ_DATA_SINGLE_ID);
    }

    @Override
    public boolean onCreate() {
        mReadContentDB = new ReadContentDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sURIMatcher.match(uri)) {

        case READ_DATA_MULTI_ID:
            qb.setTables(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME);
            qb.setProjectionMap(sReadContentProjectionMap);
            break;

        case READ_DATA_SINGLE_ID:
            qb.setTables(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME);
            qb.setProjectionMap(sReadContentProjectionMap);
            qb.appendWhere(ReadContentTableMetaData._ID + "="
                    + uri.getPathSegments().get(1));
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mReadContentDB.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sURIMatcher.match(uri) != READ_DATA_MULTI_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        if (values.containsKey(ReadContentTableMetaData.FIRST_NAME) == false) {
            throw new SQLException("Failed to insert row because Album Name is needed " + uri);
        }

        if (values.containsKey(ReadContentTableMetaData.LAST_NAME) == false) {
            values.put(ReadContentTableMetaData.LAST_NAME, "Unknown Genre"); 
        }

        if (values.containsKey(ReadContentTableMetaData.CONTENT_TEXT) == false) {
            values.put(ReadContentTableMetaData.CONTENT_TEXT, "Unknown Artist"); 
        }
        
        if (values.containsKey(ReadContentTableMetaData.RATING) == false) {
            values.put(ReadContentTableMetaData.RATING, "Unknown Image"); 
        }
        
        Long now = Long.valueOf(System.currentTimeMillis());
        if (values.containsKey(ReadContentTableMetaData.CREATED_DATE) == false) {
            values.put(ReadContentTableMetaData.CREATED_DATE, now); 
        }

        if (values.containsKey(ReadContentTableMetaData.MODIFIED_DATE) == false) {
            values.put(ReadContentTableMetaData.MODIFIED_DATE, now); 
        }

        SQLiteDatabase db = mReadContentDB.getWritableDatabase(); 
        long rowId = db.insert(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME,
                ReadContentTableMetaData.FIRST_NAME, values); 

        if (rowId > 0) {
            Uri insertedWeatherUri = ContentUris.withAppendedId(ReadContentTableMetaData.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(insertedWeatherUri, null);
            return insertedWeatherUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mReadContentDB.getWritableDatabase();
        int count = 0;

        switch (sURIMatcher.match(uri)) {

        case READ_DATA_MULTI_ID:
            count = db.delete(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME,where, whereArgs);
            break;

        case READ_DATA_SINGLE_ID:
            String rowId = uri.getPathSegments().get(1);
            count = db.delete(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME,
                    ReadContentTableMetaData._ID + "=" + rowId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), 
                    whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mReadContentDB.getWritableDatabase();
        int count;

        switch (sURIMatcher.match(uri)) {

        case READ_DATA_MULTI_ID:
            count = db.update(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME, values, where, whereArgs);
            break;

        case READ_DATA_SINGLE_ID:
            String rowId = uri.getPathSegments().get(1);
            count = db.update(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME,
                    values, 
                    ReadContentTableMetaData._ID + "=" + rowId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), 
                    whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = values.length;
        boolean hadFailure = false;
        for (int i = 0; i < numValues; i++) {
            try {
                if(values[i] != null) {
                    replaceInsert(uri, values[i], false);
                }
            } catch (Throwable t) {
                hadFailure = true;
            }
        }
        if (numValues > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        if (hadFailure) {
            throw new Error("There was an error bulk inserting deals");
        }
        return numValues;
    }

    public Uri replaceInsert(Uri uri, ContentValues initialValues, boolean notify) {
        if (sURIMatcher.match(uri) != READ_DATA_MULTI_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mReadContentDB.getWritableDatabase();
        long rowId = -1;

        Long now = Long.valueOf(System.currentTimeMillis());
        if (values.containsKey(ReadContentTableMetaData.CREATED_DATE) == false) {
            values.put(ReadContentTableMetaData.CREATED_DATE, now); 
        }

        if (values.containsKey(ReadContentTableMetaData.MODIFIED_DATE) == false) {
            values.put(ReadContentTableMetaData.MODIFIED_DATE, now); 
        }

        rowId = db.replace(ReadContentTableMetaData.READ_CONTENT_TABLE_NAME, null, values);

        Uri insertedWeatherUri = ContentUris.withAppendedId(ReadContentTableMetaData.CONTENT_URI, rowId);

        if (rowId > 0) {
            if(notify) {
                getContext().getContentResolver().notifyChange(insertedWeatherUri, null);
            }
            return insertedWeatherUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }
}