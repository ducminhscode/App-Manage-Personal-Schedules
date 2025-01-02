package com.example.applicationproject.Database;

import static com.example.applicationproject.Database.ToDoDBContract.MissionEntry.MISSION_DESCRIPTION;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

public class ToDoDBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ManagePersonalmission.db";
    private static final int DATABASE_VERSION = 1;


//    private static final String TABLE_MISSION = "mission";
//    private static final String MISSION_ID = "mission_id";
//    private static final String MISSION_TITLE = "mission_title";
//    private static final String MISSION_TYPE = "mission_type";
//    private static final String MISSION_DATE = "mission_date";
//    private static final String MISSION_TIME = "mission_time";
//    private static final String MISSION_DESCRIPTION = "mission_description";
//    private static final String MISSION_NOTIFICATION = "mission_notification";
//    private static final String MISSION_USER_ID = "user_id";


    public ToDoDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user = "CREATE TABLE " + ToDoDBContract.UserEntry.TABLE_NAME
                + " (" + ToDoDBContract.UserEntry.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoDBContract.UserEntry.USER_NAME + " TEXT, " + ToDoDBContract.UserEntry.USER_EMAIL
                + " TEXT, " + ToDoDBContract.UserEntry.USER_MOBILE + " TEXT, " + ToDoDBContract.UserEntry.USER_PASSWORD
                + " TEXT, " + ToDoDBContract.UserEntry.USER_ROLE + " TEXT)";

        String mission = "CREATE TABLE " + ToDoDBContract.MissionEntry.TABLE_NAME
                + " (" + ToDoDBContract.MissionEntry.MISSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoDBContract.MissionEntry.MISSION_TITLE + " TEXT, " + ToDoDBContract.MissionEntry.MISSION_DATE
                + " TEXT, " + ToDoDBContract.MissionEntry.MISSION_TIME + " TEXT, " + ToDoDBContract.MissionEntry.MISSION_isREPEAT + " TEXT, " +
                ToDoDBContract.MissionEntry.MISSION_DESCRIPTION + " TEXT, " + ToDoDBContract.MissionEntry.MISSION_isNOTIFY
                + " TEXT, " + ToDoDBContract.MissionEntry.MISSION_REPEAT_NO
                + " TEXT, " + ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE + " TEXT, "
                + ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE + " TEXT, "
                + ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID + " INTEGER, "
                + ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID + " INTEGER, "
                + ToDoDBContract.MissionEntry.MISSION_STICKER_ID + " INTEGER, "
                + ToDoDBContract.MissionEntry.MISSION_isSTICKER + " TEXT, "
                + ToDoDBContract.MissionEntry.MISSION_isACTIVE + " TEXT, "
                + "FOREIGN KEY (" + ToDoDBContract.MissionEntry.MISSION_STICKER_ID + ") REFERENCES "
                + ToDoDBContract.StickerEntry.TABLE_NAME + "(" + ToDoDBContract.StickerEntry.STICKER_ID + "), "
                + "FOREIGN KEY (" + ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID + ") REFERENCES "
                + ToDoDBContract.RingtoneEntry.TABLE_NAME + "(" + ToDoDBContract.RingtoneEntry.RINGTONE_ID + "), "
                + "FOREIGN KEY (" + ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID + ") REFERENCES "
                + ToDoDBContract.CategoryEntry.TABLE_NAME + "(" + ToDoDBContract.CategoryEntry.CATEGORY_ID + "))";

        String category = "CREATE TABLE " + ToDoDBContract.CategoryEntry.TABLE_NAME
                + " (" + ToDoDBContract.CategoryEntry.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToDoDBContract.CategoryEntry.CATEGORY_TITLE + " TEXT, " + ToDoDBContract.CategoryEntry.CATEGORY_USER_ID + " INTEGER, "
                + "FOREIGN KEY (" + ToDoDBContract.CategoryEntry.CATEGORY_USER_ID + ") REFERENCES "
                + ToDoDBContract.CategoryEntry.TABLE_NAME + "(" + ToDoDBContract.UserEntry.USER_ID + "))";;

        String ringtone = "CREATE TABLE " + ToDoDBContract.RingtoneEntry.TABLE_NAME
                + " (" + ToDoDBContract.RingtoneEntry.RINGTONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToDoDBContract.RingtoneEntry.RINGTONE_TITLE + " TEXT, " + ToDoDBContract.RingtoneEntry.RINGTONE_PATH + " TEXT)";

        String sticker = "CREATE TABLE " + ToDoDBContract.StickerEntry.TABLE_NAME + " (" + ToDoDBContract.StickerEntry.STICKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToDoDBContract.StickerEntry.STICKER_TITLE + " TEXT, " + ToDoDBContract.StickerEntry.STICKER_PATH + " TEXT)";

        String taskbeside = "CREATE TABLE " + ToDoDBContract.TaskbesideEntry.TABLE_NAME + " (" + ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToDoDBContract.TaskbesideEntry.TASKBESIDE_TITLE + " TEXT, " + ToDoDBContract.TaskbesideEntry.TASKBESIDE_MISSION_ID + " INTEGER, "
                + "FOREIGN KEY (" + ToDoDBContract.TaskbesideEntry.TASKBESIDE_MISSION_ID + ") REFERENCES " + ToDoDBContract.MissionEntry.TABLE_NAME + "(" + ToDoDBContract.MissionEntry.MISSION_ID + "))";

        String notification = "CREATE TABLE " + ToDoDBContract.NotificationEntry.TABLE_NAME + " (" + ToDoDBContract.NotificationEntry.NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToDoDBContract.NotificationEntry.NOTIFICATION_TIME + " TEXT, " + ToDoDBContract.NotificationEntry.NOTIFICATION_DATE + " TEXT, " + ToDoDBContract.NotificationEntry.NOTIFICATION_MISSION_ID + " INTEGER, "
                + "FOREIGN KEY (" + ToDoDBContract.NotificationEntry.NOTIFICATION_MISSION_ID + ") REFERENCES " + ToDoDBContract.MissionEntry.TABLE_NAME + "(" + ToDoDBContract.MissionEntry.MISSION_ID + "))";

//        String mission_ringtone = "CREATE TABLE " + ToDoDBContract.Mission_RingtoneEntry.TABLE_NAME
//                + " (" + ToDoDBContract.Mission_RingtoneEntry.MISSION_RINGTONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + ToDoDBContract.Mission_RingtoneEntry.MISSION_ID + " INTEGER, " + ToDoDBContract.Mission_RingtoneEntry.RINGTONE_ID + " INTEGER, "
//                + "FOREIGN KEY (" + ToDoDBContract.Mission_RingtoneEntry.MISSION_ID + ") REFERENCES "
//                + ToDoDBContract.MissionEntry.TABLE_NAME + "(" + ToDoDBContract.MissionEntry.MISSION_ID + "), "
//                + "FOREIGN KEY (" + ToDoDBContract.Mission_RingtoneEntry.RINGTONE_ID + ") REFERENCES "
//                + ToDoDBContract.RingtoneEntry.TABLE_NAME + "(" + ToDoDBContract.RingtoneEntry.RINGTONE_ID + "))";

        db.execSQL(user);
        db.execSQL(mission);
        db.execSQL(category);
        db.execSQL(ringtone);
        db.execSQL(sticker);
        db.execSQL(taskbeside);
        db.execSQL(notification);
//        db.execSQL(mission_ringtone);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.RingtoneEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.Mission_RingtoneEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.MissionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.StickerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.TaskbesideEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoDBContract.NotificationEntry.TABLE_NAME);
        onCreate(db);
    }


//    //Đăng ký người dùng
//    public void addUser(String name, String email, String mobile, String password, String role) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(ToDoDBContract.UserEntry.USER_NAME, name);
//        cv.put(ToDoDBContract.UserEntry.USER_EMAIL, email);
//        cv.put(ToDoDBContract.UserEntry.USER_MOBILE, mobile);
//        cv.put(ToDoDBContract.UserEntry.USER_PASSWORD, password);
//        cv.put(ToDoDBContract.UserEntry.USER_ROLE, role);
//        long result = db.insert(TABLE_USER, null, cv);
//        if (result == -1) {
//            Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    //Kiểm tra Login
//    public boolean checkUser(String username, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE "
//                + USER_NAME + " = ? AND " + USER_PASSWORD + " = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{username, password});
//        boolean userExists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//
//        return userExists;
//    }
//
//    //Kiểm tra nguoi dung va email
//    public boolean checkUsernameAndEmail(String username, String email) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE "
//                + USER_NAME + " = ? AND " + USER_EMAIL + " = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{username, email});
//        boolean nameExists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//
//        return nameExists;
//    }
//
//    //Kiểm tra người dùng có tồn tại hay chưa
//    public boolean checkUsernameExists(String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_NAME + " = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{username});
//        boolean usernameExists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//
//        return usernameExists;
//    }
//
//    //Kiểm tra email có tồn tại hay chưa
//    public boolean checkEmailExists(String email) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_EMAIL + " = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{email});
//        boolean emailExists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//
//        return emailExists;
//    }
//
//    //Kiểm tra số điện thoại có tồn tại hay chưa
//    public boolean checkMobileExists(String mobile) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_MOBILE + " = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{mobile});
//        boolean mobileExists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//
//        return mobileExists;
//    }
//
//    //Thay đổi mật khẩu
//    public boolean updatePassword(String email, String newPassword) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(USER_PASSWORD, newPassword);
//
//        int rows = db.update(TABLE_USER, values, USER_EMAIL + " = ?", new String[]{email});
//        db.close();
//        return rows > 0;
//    }
//
//    //Kiểm tra có user nào chưa
//    public boolean hasUsers() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = null;
//        boolean hasUser = false;
//
//        try {
//            String query = "SELECT COUNT(*) FROM user";
//            cursor = db.rawQuery(query, null);
//
//            if (cursor.moveToFirst()) {
//                int count = cursor.getInt(0);
//                hasUser = count > 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            db.close();
//        }
//        return hasUser;
//    }
//
//    public boolean checkAdmin(String userName) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE "
//                + USER_NAME + " = ? AND " + USER_ROLE + " = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{userName, "admin"});
//        if (cursor != null && cursor.moveToFirst()) {
//            cursor.close();
//            db.close();
//            return true;
//        } else {
//            cursor.close();
//            db.close();
//            return false;
//        }
//    }
//
//    public void upgradeUser(String currentUser, String user) {
//        if (checkAdmin(currentUser)) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String query = "UPDATE " + TABLE_USER +
//                    " SET " + USER_ROLE + " = ? WHERE " + USER_NAME + " = ?";
//            db.execSQL(query, new String[]{"admin", user});
//            db.close();
//            Toast.makeText(context, "Update sucessfull", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "You don't have permission", Toast.LENGTH_SHORT).show();
//        }
//    }


//
//    //Thêm nhiệm vụ
//    public void addMission(int type, String title, String date, String time,int notification,String description, int user_id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(MISSION_TITLE, title);
//        cv.put(MISSION_TYPE, type);
//        cv.put(MISSION_DATE, date);
//        cv.put(MISSION_TIME, time);
//        cv.put(MISSION_DESCRIPTION, description);
//        cv.put(MISSION_NOTIFICATION,notification);
//        cv.put(USER_ID, user_id);
//
//        long result = db.insert(TABLE_MISSION, null, cv);
//        if (result == -1) {
//            Toast.makeText(context, "Failed to add mission", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Mission added successfully", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public List<Mission> getAllMissions() {
//        List<Mission> missionList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Truy vấn tất cả nhiệm vụ
//        String query = "SELECT * FROM " + TABLE_MISSION;
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                // Kiểm tra xem các chỉ số cột có hợp lệ không trước khi lấy giá trị
//                int missionIdIndex = cursor.getColumnIndex(MISSION_ID);
//                int titleIndex = cursor.getColumnIndex(MISSION_TITLE);
//                int typeIndex = cursor.getColumnIndex(MISSION_TYPE);
//                int dateIndex = cursor.getColumnIndex(MISSION_DATE);
//                int timeIndex = cursor.getColumnIndex(MISSION_TIME);
//                int descriptionIndex = cursor.getColumnIndex(MISSION_DESCRIPTION);
//                int notificationIndex = cursor.getColumnIndex(MISSION_NOTIFICATION);
//                int userIdIndex = cursor.getColumnIndex(MISSION_USER_ID);
//
//                if (missionIdIndex != -1 && titleIndex != -1 && typeIndex != -1 && dateIndex != -1 &&
//                        timeIndex != -1 && descriptionIndex != -1 && notificationIndex != -1 && userIdIndex != -1) {
//
//                    // Lấy dữ liệu từ cursor
//                    int missionId = cursor.getInt(missionIdIndex);
//                    String title = cursor.getString(titleIndex);
//                    int type = cursor.getInt(typeIndex);
//                    String date = cursor.getString(dateIndex);
//                    String time = cursor.getString(timeIndex);
//                    String description = cursor.getString(descriptionIndex);
//                    int notification = cursor.getInt(notificationIndex);
//                    int userId = cursor.getInt(userIdIndex);
//
//                    // Tạo đối tượng Mission và thêm vào danh sách
//                    Mission mission = new Mission(
//                            type,  // Chuyển đổi loại từ String sang resource ID nếu cần
//                            title,
//                            date,
//                            time,
//                            notification,  // Chuyển đổi thông báo nếu cần
//                            description
//                    );
//                    missionList.add(mission);
//                }
//                while (cursor.moveToNext()) ;
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return missionList;
//    }

}
