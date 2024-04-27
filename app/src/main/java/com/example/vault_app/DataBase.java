package com.example.vault_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase {

    // Data base name used
    final String DATABASE_NAME = "NotesDB";

    // Version of our database
    private final int DATABASE_VERSION = 1;


    // Tables created in our database
    final String DB_LOGIN_TABLE = "Login_Table";
    final String DB_NOTES_TABLE = "Notes_Table";
    final String DB_BIN_TABLE = "Bin_Table";


    // login table to control the number of users
    private final String KEY_ID_LOGIN = "_id";
    private final String KEY_FIRSTNAME_LOGIN = "_firstname";
    private final String KEY_LASTNAME_LOGIN = "_lastname";
    private final String KEY_EMAIL_LOGIN = "_mail";
    private final String KEY_GENDER_LOGIN = "_gender";
    private final String KEY_PASSWORD_LOGIN = "_password";
    private final String KEY_ISLOGIN = "_islogin";


    //Columns of Notes Table of a logged in user
    private final String KEY_ID_NOTE = "_id";
    private final String KEY_NAME_NOTE = "_name";
    private final String KEY_PASSWORD_NOTE = "_password";
    private final String KEY_URL_NOTE = "_url";
    private final String KEY_USER_ID = "_user";

    Context context;
    MyDatabaseHelper helper;
    SQLiteDatabase sqLiteDatabase;

    // constructor
    public DataBase(Context c)
    {
        context = c;
    }

    public void open() {
        helper = new MyDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
        helper.close();
    }

    // Database Helper class
    class MyDatabaseHelper extends SQLiteOpenHelper {
        public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + DB_LOGIN_TABLE + "(" +
                    KEY_ID_LOGIN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_FIRSTNAME_LOGIN + " TEXT NOT NULL," +
                    KEY_LASTNAME_LOGIN + " TEXT NOT NULL," +
                    KEY_EMAIL_LOGIN + " TEXT NOT NULL," +
                    KEY_PASSWORD_LOGIN + " TEXT NOT NULL," +
                    KEY_GENDER_LOGIN + " TEXT NOT NULL," +
                    KEY_ISLOGIN + " INTEGER NOT NULL)");


            db.execSQL("CREATE TABLE " + DB_NOTES_TABLE + "(" +
                    KEY_ID_NOTE + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NAME_NOTE + "TEXT NOT NULL," +
                    KEY_PASSWORD_NOTE + "TEXT NOT NULL," +
                    KEY_URL_NOTE + "TEXT NOT NULL," +
                    KEY_USER_ID + "INTEGER NOT NULL," +
                    "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + DB_LOGIN_TABLE + "(" + KEY_ID_LOGIN + "));"
                    );


            //db.execSQL("CREATE TABLE " + DB_BIN_TABLE + "(" +);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // if ever want to backup data, write code here


            db.execSQL("DROP TABLE "+DB_LOGIN_TABLE+" IF EXISTS");
            db.execSQL("DROP TABLE "+DB_NOTES_TABLE+" IF EXISTS");
            db.execSQL("DROP TABLE "+DB_BIN_TABLE+" IF EXISTS");
            onCreate(db);
        }
    }

    public void insertLoginUser(String fname, String lname, String email, String pass, String gender) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FIRSTNAME_LOGIN, fname);
        cv.put(KEY_LASTNAME_LOGIN, lname);
        cv.put(KEY_EMAIL_LOGIN, email);
        cv.put(KEY_PASSWORD_LOGIN, pass);
        cv.put(KEY_GENDER_LOGIN, gender);
        cv.put(KEY_ISLOGIN, 0);

        long temp = sqLiteDatabase.insert(DB_LOGIN_TABLE, null, cv);
        if (temp == -1)
            Toast.makeText(context, "Can not Register User", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Registered User", Toast.LENGTH_SHORT).show();

    }

    public int LoginRegisteredUser(String email, String pass) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISLOGIN, 1);

        return sqLiteDatabase.update(DB_LOGIN_TABLE, cv, KEY_EMAIL_LOGIN + " =? and " + KEY_PASSWORD_LOGIN + " =?", new String[]{email, pass});
    }

    public String getLoggedInUser() {
        String fname = null, lname = null;
        Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT " + KEY_FIRSTNAME_LOGIN + " FROM " + DB_LOGIN_TABLE + " WHERE " +
                KEY_ISLOGIN + " = 1", null);

        Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT " + KEY_LASTNAME_LOGIN + " FROM " + DB_LOGIN_TABLE + " WHERE " +
                KEY_ISLOGIN + " = 1", null);

        if (cursor1.moveToFirst()) {
            int index = cursor1.getColumnIndex(KEY_FIRSTNAME_LOGIN);
            fname = cursor1.getString(index);
        }
        if (cursor2.moveToFirst()) {
            int index = cursor2.getColumnIndex(KEY_LASTNAME_LOGIN);
            lname = cursor2.getString(index);
        }
        cursor1.close();
        cursor2.close();
        return fname + " " + lname;

    }

    public boolean isUserLoggedIn() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ISLOGIN + " FROM " + DB_LOGIN_TABLE + " WHERE " +
                KEY_ISLOGIN + " = 1", null);

        boolean res = cursor.getCount() > 0;
        cursor.close();

        return res;
    }

    public void logoutUser() {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISLOGIN, 0);

        sqLiteDatabase.update(DB_LOGIN_TABLE, cv, null, new String[]{});
    }

    public int getLoggedInUserId() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ID_LOGIN + " FROM " + DB_LOGIN_TABLE + " WHERE " +
                KEY_ISLOGIN + " = 1", null);
        int id = 0;
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(KEY_ID_LOGIN);
            id = cursor.getInt(index);
        }
        cursor.close();
        return id;
    }


    // For Notes

    public ArrayList<NoteClass> readAllNotes() {
        String loggedInUserID = getLoggedInUserId() + "";

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DB_NOTES_TABLE + " WHERE " +
                KEY_USER_ID + " =?", new String[]{loggedInUserID});

        int index_id = cursor.getColumnIndex(KEY_ID_NOTE);
        int index_name = cursor.getColumnIndex(KEY_NAME_NOTE);
        int index_password = cursor.getColumnIndex(KEY_PASSWORD_NOTE);
        int index_url = cursor.getColumnIndex(KEY_URL_NOTE);

        ArrayList<NoteClass> allnotes = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                NoteClass note = new NoteClass();
                note.setId(cursor.getInt(index_id));
                note.setName(cursor.getString(index_name));
                note.setPassword(cursor.getString(index_password));
                note.setUrl(cursor.getString(index_url));

                allnotes.add(note);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return allnotes;
    }

    public void insertNote(String name, String password, String url) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME_NOTE, name);
        cv.put(KEY_PASSWORD_NOTE, password);
        cv.put(KEY_URL_NOTE, url);
        cv.put(KEY_USER_ID, getLoggedInUserId());

        long res = sqLiteDatabase.insert(DB_NOTES_TABLE, null, cv);
        if (res == -1)
            Toast.makeText(context, "Note not added", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Note Added", Toast.LENGTH_SHORT).show();
    }

}
