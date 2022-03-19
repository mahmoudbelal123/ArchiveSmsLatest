package com.kingsms.archivesms.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyDatabaseAdapter {


    static final String KEY_ROWID = "_id";
    static final String TAG = "********";
    static final String DATABASE_NAME = "NotificationFolder";
    static final String DATABASE_TABLE_OF_NOTIFICATION_ALL = "notifications_content";
    static final String DATABASE_TABLE_OF_SENDER_NAMES = "sender_names";
    static final String DATABASE_TABLE_OF_CONTACTS = "contacts";

    static final int DATABASE_VERSION = 1;


    static final String NOTIFICATION_TABLE
            = "create table " + DATABASE_TABLE_OF_NOTIFICATION_ALL + " (_id integer primary key autoincrement,"
            + "notification_id varchar not null unique, sender_name varchar not null,title varchar not null,time text not null,content varchar not null);";

    static final String SENDER_NAMES_TABLE = "create table " + DATABASE_TABLE_OF_SENDER_NAMES + " (_id integer primary key autoincrement, "
            + "sender_name varchar not null unique, status integer ,time_milli long);";

    static final String CONTACTS_TABLE
            = "create table " + DATABASE_TABLE_OF_CONTACTS + " (_id integer primary key autoincrement,"
            + "contact_name varchar, contact_phone varchar);";


    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;


    public MyDatabaseAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //---opens the database---
    public MyDatabaseAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a Trip into the database---  //
    public long insertNotification(String notification_id, String sender_name, String time, String content, String title) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("notification_id", notification_id);
        initialValues.put("sender_name", sender_name);
        initialValues.put("time", time);
        initialValues.put("content", content);
        initialValues.put("title", title);

        return db.insert(DATABASE_TABLE_OF_NOTIFICATION_ALL, null, initialValues);
    }

    public long insertContacts(String contact_name, String contact_phone) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("contact_name", contact_name);
        initialValues.put("contact_phone", contact_phone);

        return db.insert(DATABASE_TABLE_OF_CONTACTS, null, initialValues);
    }

    public long insertSenderNames(String sender_name, int status, long time_milli) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("sender_name", sender_name);
        initialValues.put("status", status);
        initialValues.put("time_milli", time_milli);

        return db.insert(DATABASE_TABLE_OF_SENDER_NAMES, null, initialValues);
    }

    public long updateStatusOfNotification(String sender_name, int status, long time_milli) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("status", status);
        initialValues.put("time_milli", time_milli);
        return db.update(DATABASE_TABLE_OF_SENDER_NAMES, initialValues, "sender_name" + "='" + sender_name + "'", null);
    }

    public long updateStatusOfNotificationForClickToDetails(String sender_name, int status) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("status", status);
        return db.update(DATABASE_TABLE_OF_SENDER_NAMES, initialValues, "sender_name" + "='" + sender_name + "'", null);
    }

    public Cursor getAllUnread() {
        return db.query(DATABASE_TABLE_OF_SENDER_NAMES, new String[]{KEY_ROWID}, "status" + "!='0'", null, null, null, null);
    }

    public Cursor getStatusOfSenderName(String senderName) {
        return db.query(DATABASE_TABLE_OF_SENDER_NAMES, new String[]{KEY_ROWID, "status"}, "sender_name" + "='" + senderName + "'", null, null, null, null);
    }

    public Cursor getContact(String contactPhone) {
        return db.query(DATABASE_TABLE_OF_CONTACTS, new String[]{KEY_ROWID, "contact_name"}, "contact_phone" + "='" + contactPhone + "'", null, null, null, null);
    }

    public boolean deleteSpecificNotificationById(String rowId) {
        return db.delete(DATABASE_TABLE_OF_NOTIFICATION_ALL, "notification_id = " + rowId, null) > 0;
    }

    public boolean deleteSenderNameOfAll(String senderName) {
        return db.delete(DATABASE_TABLE_OF_NOTIFICATION_ALL, "sender_name" + "='" + senderName + "'", null) > 0;
    }

    public boolean deleteSenderName(String senderName) {
        return db.delete(DATABASE_TABLE_OF_SENDER_NAMES, "sender_name" + "='" + senderName + "'", null) > 0;
    }

    //---retrieves all the UpComing Trips---
    public Cursor getAllSenderNames() {
        return db.query(DATABASE_TABLE_OF_SENDER_NAMES, new String[]{KEY_ROWID, "sender_name", "status", "time_milli"}, null, null, null, null, "time_milli DESC");
    }

    public Cursor getNotificationsBySenderName(String sender_name) {
        return db.query(DATABASE_TABLE_OF_NOTIFICATION_ALL, new String[]{KEY_ROWID, "title", "time", "content", "notification_id"}, "sender_name" + "='" + sender_name + "'", null, null, null, null, null);
    }

    public Cursor getAllNotificationIds() {
        return db.query(DATABASE_TABLE_OF_NOTIFICATION_ALL, new String[]{KEY_ROWID, "notification_id"}, null, null, null, null, null, null);
    }

    public Cursor getLastId() {
        return db.query(DATABASE_TABLE_OF_NOTIFICATION_ALL, new String[]{KEY_ROWID}, null, null, null, null, null);
    }

    //---retrieves a particular Trip---//
    public Cursor getContact(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE_OF_NOTIFICATION_ALL, new String[]{KEY_ROWID, "name_of_trip", "start_point", "end_point", "date", "time", "notes", "trip_type", "status"}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getStartAndEndPoint(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE_OF_NOTIFICATION_ALL, new String[]{KEY_ROWID, "start_point", "end_point"}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // get Id
    public Cursor getId(long id) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE_OF_NOTIFICATION_ALL, new String[]{KEY_ROWID}, KEY_ROWID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //--------------------- updates a Trip -------------------//
    public boolean updateContact(long rowId, String name_trip, String start_point, String end_point, String date_trip, String time_trip, String notes, String trip_type, String status) {
        ContentValues args = new ContentValues();
        args.put("name_of_trip", name_trip);
        args.put("start_point", start_point);
        args.put("end_point", end_point);
        args.put("date", date_trip);
        args.put("time", time_trip);
        args.put("notes", notes);
        args.put("trip_type", trip_type);
        args.put("status", status);

        return db.update(DATABASE_TABLE_OF_NOTIFICATION_ALL, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateStatusContact(long rowId, String status) {
        ContentValues args = new ContentValues();

        args.put("status", status);

        return db.update(DATABASE_TABLE_OF_NOTIFICATION_ALL, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //================= My Inner Class   Helper ===============================================//
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(NOTIFICATION_TABLE);
                db.execSQL(SENDER_NAMES_TABLE);
                db.execSQL(CONTACTS_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + "to"
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_OF_NOTIFICATION_ALL + "");
            onCreate(db);
        }

    }    // ========================  End of Helper Class ========================================//


}




