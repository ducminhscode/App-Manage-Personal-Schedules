package com.example.applicationproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ManagePersonalSchedule.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_MOBILE = "phone_number";
    private static final String USER_PASSWORD = "user_password";

    /*public static final String TABLE_CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_DESCRIPTION = "category_description";

    public static final String TABLE_SCHEDULE = "schedule";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String SCHEDULE_TITLE = "schedule_title";
    public static final String SCHEDULE_DESCRIPTION = "schedule_description";
    public static final String SCHEDULE_START_TIME = "start_time";
    public static final String SCHEDULE_END_TIME = "end_time";
    public static final String SCHEDULE_CATEGORY = "at_category";
    public static final String SCHEDULE_SOUND = "have_sound";


    public static final String TABLE_SOUND = "sound";
    public static final String SOUND_ID = "sound_id";
    public static final String SOUND_NAME = "sound_name";
    public static final String Sound_Path = "path_sound";*/


    public CreateDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user = "CREATE TABLE " + TABLE_USER + " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT, " + USER_EMAIL + " TEXT, " + USER_MOBILE + " TEXT, " + USER_PASSWORD + " TEXT)";

        /*String category = "CREATE TABLE " + TABLE_CATEGORY + " (" + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_NAME + " TEXT, " + CATEGORY_DESCRIPTION + " TEXT)";

        String schedule = "CREATE TABLE " + TABLE_SCHEDULE + " (" + SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SCHEDULE_TITLE + " TEXT, " + SCHEDULE_DESCRIPTION + " TEXT, " + SCHEDULE_START_TIME + " TEXT, " +
                SCHEDULE_END_TIME + " TEXT, " + SCHEDULE_CATEGORY + " TEXT, " + SCHEDULE_SOUND + " TEXT)";

        String sound = "CREATE TABLE " + TABLE_SOUND + " (" + SOUND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SOUND_NAME + " TEXT, " + Sound_Path + " TEXT)";*/

        db.execSQL(user);
        /*db.execSQL(category);
        db.execSQL(schedule);
        db.execSQL(sound);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    //Đăng ký người dùng
    public void addUser(String name, String email, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_NAME, name);
        cv.put(USER_EMAIL, email);
        cv.put(USER_MOBILE, mobile);
        cv.put(USER_PASSWORD, password);
        long result = db.insert(TABLE_USER, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //Kiểm tra Login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE "
                + USER_NAME + " = ? AND " + USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return userExists;
    }

    //Kiểm tra đổi mật khẩu
    public boolean checkPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE "
                + USER_EMAIL + " = ? AND " + USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean passExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return passExists;
    }

    //Kiểm tra người dùng có tồn tại hay chưa
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean usernameExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return usernameExists;
    }

    //Kiểm tra email có tồn tại hay chưa
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean emailExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return emailExists;
    }

    //Kiểm tra số điện thoại có tồn tại hay chưa
    public boolean checkExists(String mobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_MOBILE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{mobile});
        boolean mobileExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return mobileExists;
    }

    //Thay đổi mật khẩu
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PASSWORD, newPassword);

        int rows = db.update(TABLE_USER, values, USER_EMAIL + " = ?", new String[]{email});
        db.close();
        return rows > 0;
    }

}
