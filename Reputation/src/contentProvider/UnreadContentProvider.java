package contentProvider;

import java.util.HashMap;

import contentProvider.UnreadContentProviderMetaData.UnreadContentTableMetaData;

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

public class UnreadContentProvider extends ContentProvider {
    private UnreadContentDBHelper mUnreadContentDB;

    private static final int UNREAD_DATA_SINGLE_ID = 1;
    private static final int UNREAD_DATA_MULTI_ID = 2;

    private static HashMap<String, String> sUnreadContentProjectionMap;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private class UnreadContentDBHelper extends SQLiteOpenHelper {

        public UnreadContentDBHelper(Context context) {
            super(context, UnreadContentProviderMetaData.DATABASE_NAME, 
                    null, 
                    UnreadContentProviderMetaData.DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME + " (" +
                    UnreadContentTableMetaData._ID + " INTEGER PRIMARY KEY," +
                    UnreadContentTableMetaData.FIRST_NAME + " TEXT, " +
                    UnreadContentTableMetaData.LAST_NAME + " TEXT, " +
                    UnreadContentTableMetaData.CONTENT_TEXT + " TEXT, " +
                    UnreadContentTableMetaData.RATING + " INTEGER, " +
                    UnreadContentTableMetaData.UNREAD_CONTENT_ID + " TEXT, " +
                    UnreadContentTableMetaData.CREATED_DATE + " INTEGER, " +
                    UnreadContentTableMetaData.MODIFIED_DATE + " INTEGER, " +
                    "UNIQUE(" + UnreadContentTableMetaData.UNREAD_CONTENT_ID + ") ON CONFLICT IGNORE);" );
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME);

            // Create tables again
            onCreate(db);
        }
    }

    static {
        sUnreadContentProjectionMap = new HashMap<String, String>();

        sUnreadContentProjectionMap.put(UnreadContentTableMetaData._ID,UnreadContentTableMetaData._ID);

        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.FIRST_NAME, UnreadContentTableMetaData.FIRST_NAME);
        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.LAST_NAME, UnreadContentTableMetaData.LAST_NAME);
        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.CONTENT_TEXT, UnreadContentTableMetaData.CONTENT_TEXT);
        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.RATING, UnreadContentTableMetaData.RATING);
        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.UNREAD_CONTENT_ID, UnreadContentTableMetaData.UNREAD_CONTENT_ID);
        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.CREATED_DATE, UnreadContentTableMetaData.CREATED_DATE);
        sUnreadContentProjectionMap.put(UnreadContentTableMetaData.MODIFIED_DATE, UnreadContentTableMetaData.MODIFIED_DATE);
    }

    static {
        sURIMatcher.addURI(UnreadContentProviderMetaData.AUTHORITY, UnreadContentProviderMetaData.UNREAD_CONTENT_BASE_PATH, UNREAD_DATA_MULTI_ID);
        sURIMatcher.addURI(UnreadContentProviderMetaData.AUTHORITY, UnreadContentProviderMetaData.UNREAD_CONTENT_BASE_PATH + "/#", UNREAD_DATA_SINGLE_ID);
    }

    @Override
    public boolean onCreate() {
        mUnreadContentDB = new UnreadContentDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sURIMatcher.match(uri)) {

        case UNREAD_DATA_MULTI_ID:
            qb.setTables(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME);
            qb.setProjectionMap(sUnreadContentProjectionMap);
            break;

        case UNREAD_DATA_SINGLE_ID:
            qb.setTables(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME);
            qb.setProjectionMap(sUnreadContentProjectionMap);
            qb.appendWhere(UnreadContentTableMetaData._ID + "="
                    + uri.getPathSegments().get(1));
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mUnreadContentDB.getReadableDatabase();
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
        if (sURIMatcher.match(uri) != UNREAD_DATA_MULTI_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        if (values.containsKey(UnreadContentTableMetaData.FIRST_NAME) == false) {
            throw new SQLException("Failed to insert row because Album Name is needed " + uri);
        }

        if (values.containsKey(UnreadContentTableMetaData.LAST_NAME) == false) {
            values.put(UnreadContentTableMetaData.LAST_NAME, "Unknown Genre"); 
        }

        if (values.containsKey(UnreadContentTableMetaData.CONTENT_TEXT) == false) {
            values.put(UnreadContentTableMetaData.CONTENT_TEXT, "Unknown Artist"); 
        }
        
        if (values.containsKey(UnreadContentTableMetaData.RATING) == false) {
            values.put(UnreadContentTableMetaData.RATING, "Unknown Image"); 
        }
        
        Long now = Long.valueOf(System.currentTimeMillis());
        if (values.containsKey(UnreadContentTableMetaData.CREATED_DATE) == false) {
            values.put(UnreadContentTableMetaData.CREATED_DATE, now); 
        }

        if (values.containsKey(UnreadContentTableMetaData.MODIFIED_DATE) == false) {
            values.put(UnreadContentTableMetaData.MODIFIED_DATE, now); 
        }

        SQLiteDatabase db = mUnreadContentDB.getWritableDatabase(); 
        long rowId = db.insert(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME,
                UnreadContentTableMetaData.FIRST_NAME, values); 

        if (rowId > 0) {
            Uri insertedWeatherUri = ContentUris.withAppendedId(UnreadContentTableMetaData.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(insertedWeatherUri, null);
            return insertedWeatherUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mUnreadContentDB.getWritableDatabase();
        int count = 0;

        switch (sURIMatcher.match(uri)) {

        case UNREAD_DATA_MULTI_ID:
            count = db.delete(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME,where, whereArgs);
            break;

        case UNREAD_DATA_SINGLE_ID:
            String rowId = uri.getPathSegments().get(1);
            count = db.delete(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME,
                    UnreadContentTableMetaData._ID + "=" + rowId
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
        SQLiteDatabase db = mUnreadContentDB.getWritableDatabase();
        int count;

        switch (sURIMatcher.match(uri)) {

        case UNREAD_DATA_MULTI_ID:
            count = db.update(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME, values, where, whereArgs);
            break;

        case UNREAD_DATA_SINGLE_ID:
            String rowId = uri.getPathSegments().get(1);
            count = db.update(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME,
                    values, 
                    UnreadContentTableMetaData._ID + "=" + rowId
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
        if (sURIMatcher.match(uri) != UNREAD_DATA_MULTI_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mUnreadContentDB.getWritableDatabase();
        long rowId = -1;

        Long now = Long.valueOf(System.currentTimeMillis());
        if (values.containsKey(UnreadContentTableMetaData.CREATED_DATE) == false) {
            values.put(UnreadContentTableMetaData.CREATED_DATE, now); 
        }

        if (values.containsKey(UnreadContentTableMetaData.MODIFIED_DATE) == false) {
            values.put(UnreadContentTableMetaData.MODIFIED_DATE, now); 
        }

        rowId = db.replace(UnreadContentTableMetaData.UNREAD_CONTENT_TABLE_NAME, null, values);

        Uri insertedWeatherUri = ContentUris.withAppendedId(UnreadContentTableMetaData.CONTENT_URI, rowId);

        if (rowId > 0) {
            if(notify) {
                getContext().getContentResolver().notifyChange(insertedWeatherUri, null);
            }
            return insertedWeatherUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }
}