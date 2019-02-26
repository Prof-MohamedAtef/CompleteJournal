package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Prof-Mohamed Atef on 2/20/2019.
 */

public class NewsProvider extends ContentProvider {

    static final String PROVIDER_NAME = "journal.nanodegree.capstone.prof.journal_capstonnanodegree";
    static final String URL = "content://" + PROVIDER_NAME + "/news";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID="_ID";
    public static final String AUTHOR="AUTHOR";
    public static final String TITLE="TITLE";
    public static final String DESCRIPTION="DESCRIPTION";
    public static final String ARTICLE_URL="ARTICLE_URL";
    public static final String IMAGE_URL="IMAGE_URL";
    public static final String PUBLISHED_AT="PUBLISHED_AT";
    public static final String SOURCE_NAME="SOURCE_NAME";
    public static final String CATEGORY="CATEGORY";

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final int NEWS = 1;
    static final int NEWS_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "news", NEWS);
        uriMatcher.addURI(PROVIDER_NAME, "news/#", NEWS_ID);
    }

    /*
    Database
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "News";
    static final String NEWS_TABLE_NAME = "NEWS";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + NEWS_TABLE_NAME +
                    " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " AUTHOR TEXT NOT NULL, " +
                    " TITLE TEXT NOT NULL, " +
                    " DESCRIPTION TEXT NOT NULL, " +
                    " ARTICLE_URL TEXT NOT NULL, " +
                    " IMAGE_URL TEXT NOT NULL, " +
                    " PUBLISHED_AT TEXT NOT NULL, " +
                    " CATEGORY TEXT NOT NULL, " +
                    " SOURCE_NAME TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  NEWS_TABLE_NAME);
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(NEWS_TABLE_NAME);

            switch (uriMatcher.match(uri)) {
                case NEWS:
                    qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                    break;

                case NEWS_ID:
                    qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
            }

            if (sortOrder == null || sortOrder == ""){
                /**
                 * By default sort on student names
                 */
                sortOrder = CATEGORY;
            }

            Cursor c = qb.query(db,	projection,	selection,
                    selectionArgs,null, null, sortOrder);
            /**
             * register to watch a content URI for changes
             */
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case NEWS:
                return "vnd.android.cursor.dir/vnd.journal.nanodegree.capstone.prof.journal_capstonnanodegree.items";
            /**
             * Get a particular student
             */
            case NEWS_ID:
                return "vnd.android.cursor.item/vnd.journal.nanodegree.capstone.prof.journal_capstonnanodegree.items";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(	NEWS_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case NEWS:
                count = db.delete(NEWS_TABLE_NAME, selection, selectionArgs);
                break;

            case NEWS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( NEWS_TABLE_NAME, _ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case NEWS:
                    count = db.update(NEWS_TABLE_NAME, values, selection, selectionArgs);
                    break;

                case NEWS_ID:
                    count = db.update(NEWS_TABLE_NAME, values,
                            _ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }
}