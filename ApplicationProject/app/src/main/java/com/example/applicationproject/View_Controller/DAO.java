package com.example.applicationproject.View_Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.View_Controller.Service.AlarmScheduler;

public class DAO {

    //Đăng ký người dùng
    public static void addUser(Context context, String name, String email, String mobile, String password, String role) {

        // Băm mật khẩu an toàn hơn sử dụng SHA-256
        if (password == null) {
            password = "1";
        }
        String passwordHash = String.valueOf(password.hashCode());

        // Chuẩn bị dữ liệu cho ContentValues
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.UserEntry.USER_NAME, name);
        values.put(ToDoDBContract.UserEntry.USER_EMAIL, email);
        values.put(ToDoDBContract.UserEntry.USER_MOBILE, mobile);
        values.put(ToDoDBContract.UserEntry.USER_PASSWORD, passwordHash);
        values.put(ToDoDBContract.UserEntry.USER_ROLE, role);

        // Chèn người dùng vào cơ sở dữ liệu
        Uri newUri = context.getContentResolver().insert(ToDoDBContract.UserEntry.CONTENT_URI, values);

        // Kiểm tra và xử lý kết quả trả về (newUri)
        if (newUri == null) {
            Log.e("UserDB", "Failed to insert user into the database");
        } else {
            Log.d("UserDB", "User successfully inserted with URI: " + newUri);
        }
    }

    @SuppressLint("Range")
    public static int getUserId(Context context, String username) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_NAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = null;
        int userId = -1;
        try {
            cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null);
            Log.e("UserDB", "Column USER_ID not found: " + cursor);
            // Kiểm tra nếu cursor không null và di chuyển tới dòng đầu tiên
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(ToDoDBContract.UserEntry.USER_ID);
                if (columnIndex != -1) {
                    userId = cursor.getInt(columnIndex);
                } else {
                    Log.e("UserDB", "Column USER_ID not found");
                }
            } else {
            Log.e("UserDB", "Username is null");
            }
        } catch (Exception e) {
            Log.e("UserDB", "Error fetching user ID", e);
        } finally {
            // Đảm bảo cursor luôn được đóng
            if (cursor != null) {
                cursor.close();
            }
        }

        return userId;
    }


    //Kiểm tra Login
    public static boolean checkUser(Context context, String username, String password) {
        String passWord_hash = String.valueOf(password.hashCode());
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_NAME + " = ? AND " + ToDoDBContract.UserEntry.USER_PASSWORD + " = ?";
        String[] selectionArgs = { username, passWord_hash };

        // Thực hiện truy vấn
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null);

            // Kiểm tra nếu cursor là null
            if (cursor != null) {
                return cursor.getCount() > 0;
            } else {
                Log.e("checkUser", "Cursor is null");
                return false;
            }
        } catch (Exception e) {
            Log.e("checkUser", "Error querying database: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //Kiểm tra nguoi dung va email
    public static boolean checkUsernameAndEmailExists(Context context, String username, String email) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_NAME + " = ? AND " + ToDoDBContract.UserEntry.USER_EMAIL + " = ?";
        String[] selectionArgs = { username, email };

        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {

            // Kiểm tra cursor có null không
            if (cursor != null) {
                return cursor.getCount() > 0;  // Kiểm tra xem có kết quả nào không
            } else {
                Log.e("checkUsernameAndEmailExists", "Cursor is null. No data found.");
                return false;
            }
        } catch (Exception e) {
            Log.e("checkUsernameAndEmailExists", "Error querying database: " + e.getMessage());
            return false;
        }
        // Đảm bảo đóng cursor
    }


    //Kiểm tra người dùng có tồn tại hay chưa
    public static boolean checkUsernameExists(Context context, String username) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_NAME + " = ?";
        String[] selectionArgs = { username };

        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {

            if (cursor != null) {
                Log.e("checkUsernameExists", "Cursor is not null." + cursor.getCount());
                return cursor.getCount() > 0;
            } else {
                Log.e("checkUsernameExists", "Cursor is null. No data found.");
                return false;
            }
        } catch (Exception e) {
            Log.e("checkUsernameExists", "Error querying database: " + e.getMessage());
            return false;
        }
    }


    //Kiểm tra email có tồn tại hay chưa
    public static boolean checkEmailExists(Context context, String email) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_EMAIL + " = ? ";
        String[] selectionArgs = { email };

        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {

            // Kiểm tra nếu cursor không null và thực hiện kiểm tra số lượng dòng
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // Xử lý lỗi (nếu có)
            Log.e("checkEmailExists", "Error checking email existence: " + e.getMessage());
            return false;
        }
        // Đảm bảo cursor được đóng dù có lỗi hay không
    }


    //Kiểm tra số điện thoại có tồn tại hay chưa
    public static boolean checkMobileExists(Context context, String mobile) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_MOBILE + " = ?";
        String[] selectionArgs = { mobile };

        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            // Truy vấn dữ liệu từ ContentProvider

            // Kiểm tra nếu cursor không null
            if (cursor != null) {
                return cursor.getCount() > 0;  // Kiểm tra xem số lượng bản ghi có lớn hơn 0 không
            } else {
                Log.e("checkMobileExists", "Cursor is null. No data found.");
                return false;
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có lỗi xảy ra
            Log.e("checkMobileExists", "Error querying database: " + e.getMessage());
            return false;
        }
        // Đảm bảo cursor luôn được đóng
    }


    //Thay đổi mật khẩu
    public static boolean updatePassword(Context context, String email, String newPassword) {
        String passWord_hash = String.valueOf(newPassword.hashCode());  // Sử dụng phương thức băm mạnh mẽ

        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.UserEntry.USER_PASSWORD, passWord_hash);

        String selection = ToDoDBContract.UserEntry.USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        int rows = 0;
        try {
            // Cập nhật mật khẩu trong cơ sở dữ liệu
            rows = context.getContentResolver().update(newUri, values, selection, selectionArgs);
        } catch (Exception e) {
            Log.e("updatePassword", "Error updating password: " + e.getMessage());
        }

        return rows > 0;  // Trả về true nếu có ít nhất 1 dòng được cập nhật
    }

    //Kiểm tra có user nào chưa
    public static boolean hasUsers(Context context) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        Cursor cursor = null;
        boolean hasUser = false;
        try {
            // Chỉ lấy số lượng người dùng, tiết kiệm tài nguyên
            String[] projection = {"COUNT(*)"};
            cursor = context.getContentResolver().query(newUri, projection, null, null, null);

            // Kiểm tra nếu cursor không phải null và di chuyển đến dòng đầu tiên
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                hasUser = count > 0;
            }
        } catch (Exception e) {
            // Ghi lỗi vào log để dễ dàng debug
            Log.e("Database Error", "Error checking if users exist", e);
        } finally {
            // Đảm bảo đóng cursor khi kết thúc
            if (cursor != null) {
                cursor.close();
            }
        }
        return hasUser;
    }


    public static boolean checkAdmin(Context context, String userName) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_NAME + " = ? AND " + ToDoDBContract.UserEntry.USER_ROLE + " = ?";
        String[] selectionArgs = { userName, "admin" };

        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {

            // Kiểm tra nếu cursor không null và thực hiện kiểm tra số lượng dòng
            if (cursor != null && cursor.getCount() > 0) {
                return true; // Tồn tại user với tên và vai trò "admin"
            } else {
                return false; // Không có user với tên và vai trò "admin"
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            Log.e("checkAdmin", "Error checking admin status: " + e.getMessage());
            return false;
        }
        // Đảm bảo đóng cursor sau khi sử dụng
    }


    public static void upgradeUser(Context context, String currentUser) {
        if (!checkAdmin(context, currentUser)) {
            Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
            ContentValues values = new ContentValues();
            values.put(ToDoDBContract.UserEntry.USER_ROLE, "admin");

            String selection = ToDoDBContract.UserEntry.USER_NAME + " = ?";
            String[] selectionArgs = { currentUser };

            int rowsUpdated = context.getContentResolver().update(newUri, values, selection, selectionArgs);

            if (rowsUpdated > 0) {
                Toast.makeText(context, "User upgraded to admin successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to upgrade user", Toast.LENGTH_SHORT).show();
            }
        } else {
            Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
            ContentValues values = new ContentValues();
            values.put(ToDoDBContract.UserEntry.USER_ROLE, "user");
            String selection = ToDoDBContract.UserEntry.USER_NAME + " = ?";
            String[] selectionArgs = { currentUser };
            int rowsUpdated = context.getContentResolver().update(newUri, values, selection, selectionArgs);
            if (rowsUpdated > 0) {
                Toast.makeText(context, "Admin down to user to user successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Failed to down to user", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static void deleteUser(Context context, int id) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        String selection = ToDoDBContract.UserEntry.USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        deleteALLCategory(context, id);
        // Thực hiện xóa người dùng
        int rowsDeleted = context.getContentResolver().delete(newUri, selection, selectionArgs);

        if (rowsDeleted > 0) {
            Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No user found with the specified ID", Toast.LENGTH_SHORT).show();
        }
    }



    public static void updateProfileUser(Context context, int id, String name, String email, String mobile, String password) {
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.UserEntry.USER_NAME, name);
        values.put(ToDoDBContract.UserEntry.USER_EMAIL, email);
        values.put(ToDoDBContract.UserEntry.USER_MOBILE, mobile);
        values.put(ToDoDBContract.UserEntry.USER_PASSWORD, password);
        String selection = ToDoDBContract.UserEntry.USER_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        // Cập nhật người dùng và kiểm tra số lượng dòng bị ảnh hưởng
        int rowsUpdated = context.getContentResolver().update(newUri, values, selection, selectionArgs);

        if (rowsUpdated > 0) {
            Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No user found with the specified ID", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean insertMission(Context context, int sticker_id, int ringTone_id, String date,
                                        String describe, String isNotify, String isRepeat, String repeatType,
                                        String time, String title, int category_id,
                                        String repeatNo, String reminder, String reminderType,
                                        String isSticker, String isActive) {

        // Tạo đối tượng ContentValues để chứa dữ liệu cần chèn
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.MissionEntry.MISSION_TITLE, title);
        values.put(ToDoDBContract.MissionEntry.MISSION_DATE, date);
        values.put(ToDoDBContract.MissionEntry.MISSION_TIME, time);
        values.put(ToDoDBContract.MissionEntry.MISSION_DESCRIPTION, describe);
        values.put(ToDoDBContract.MissionEntry.MISSION_isREPEAT, isRepeat);
        values.put(ToDoDBContract.MissionEntry.MISSION_REPEAT_NO, repeatNo);
        values.put(ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE, repeatType);
        values.put(ToDoDBContract.MissionEntry.MISSION_isNOTIFY, isNotify);
        values.put(ToDoDBContract.MissionEntry.MISSION_REMINDER, reminder);
        values.put(ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID, category_id);
        values.put(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID, ringTone_id);
        values.put(ToDoDBContract.MissionEntry.MISSION_STICKER_ID, sticker_id);
        values.put(ToDoDBContract.MissionEntry.MISSION_isSTICKER, isSticker);
        values.put(ToDoDBContract.MissionEntry.MISSION_isACTIVE, isActive);
        values.put(ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE, reminderType);



        // Địa chỉ URI để chèn dữ liệu vào cơ sở dữ liệu
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);

        // Thực hiện chèn vào cơ sở dữ liệu và kiểm tra kết quả
        Uri insertedUri = context.getContentResolver().insert(newUri, values);

        if (insertedUri != null) {
            // Chèn thành công, trả về true
            return true;
        } else {
            // Chèn thất bại, hiển thị thông báo
            Toast.makeText(context, "Failed to insert mission", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public static boolean insertCategory(Context context, String category_title, int category_user_id) {
        // Kiểm tra giá trị đầu vào để đảm bảo category_title không phải là null hoặc chuỗi rỗng
        if (category_title == null || category_title.isEmpty()) {
            Toast.makeText(context, "Category title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Tạo đối tượng ContentValues để chứa dữ liệu cần chèn
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.CategoryEntry.CATEGORY_TITLE, category_title);
        values.put(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID, category_user_id);

        // URI để thực hiện thao tác chèn vào cơ sở dữ liệu
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_CATEGORIES);

        // Thực hiện chèn vào cơ sở dữ liệu và kiểm tra kết quả
        Uri insertedUri = context.getContentResolver().insert(newUri, values);

        if (insertedUri != null) {
            // Chèn thành công, trả về true
            return true;
        } else {
            // Chèn thất bại, hiển thị thông báo lỗi
            Toast.makeText(context, "Failed to insert category", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public static boolean insertRingtone(Context context, String ringtone_title, String ringtone_path) {
        // Kiểm tra giá trị đầu vào để đảm bảo các giá trị không phải là null hoặc chuỗi rỗng
        if (ringtone_title == null || ringtone_title.isEmpty()) {
            Toast.makeText(context, "Ringtone title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ringtone_path == null || ringtone_path.isEmpty()) {
            Toast.makeText(context, "Ringtone path cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Tạo đối tượng ContentValues để lưu trữ các giá trị sẽ chèn vào cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.RingtoneEntry.RINGTONE_TITLE, ringtone_title);
        values.put(ToDoDBContract.RingtoneEntry.RINGTONE_PATH, ringtone_path);

        // Xây dựng URI để thực hiện thao tác chèn vào cơ sở dữ liệu
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_RINGTONES);

        try {
            // Thực hiện chèn và kiểm tra kết quả
            Uri insertedUri = context.getContentResolver().insert(newUri, values);

            if (insertedUri != null) {
                // Nếu chèn thành công, trả về true
                return true;
            } else {
                // Nếu chèn thất bại, hiển thị thông báo
                Toast.makeText(context, "Failed to insert ringtone", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            // Bắt lỗi nếu có vấn đề xảy ra khi thao tác với cơ sở dữ liệu
            Log.e("InsertRingtone", "Error inserting ringtone", e);
            Toast.makeText(context, "Error inserting ringtone", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean insertNotification(Context context, String notification_time, String notification_date, int mission_id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_NOTIFICATIONS);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.NotificationEntry.NOTIFICATION_TIME, notification_time);
        values.put(ToDoDBContract.NotificationEntry.NOTIFICATION_DATE, notification_date);
        values.put(ToDoDBContract.NotificationEntry.NOTIFICATION_MISSION_ID, mission_id);
        Uri insertedUri = context.getContentResolver().insert(newUri, values);
        return insertedUri != null;
    }

    public static boolean insertTaskbeside(Context context, String taskbeside_title, int mission_id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_TASKBESIDES);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.TaskbesideEntry.TASKBESIDE_TITLE, taskbeside_title);
        values.put(ToDoDBContract.TaskbesideEntry.TASKBESIDE_MISSION_ID, mission_id);
        Uri insertedUri = context.getContentResolver().insert(newUri, values);
        return insertedUri != null;
    }

    public static int getStickerId(Context context, String sticker_path){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_STICKERS);
        String selection = ToDoDBContract.StickerEntry.STICKER_PATH + " = ? ";
        String[] selectionArgs = { sticker_path };
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_ID));
            }else{
                return -1;
            }
        }
    }

    public static int getRingtoneId(Context context, String ringtone_path){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_RINGTONES);
        String selection = ToDoDBContract.RingtoneEntry.RINGTONE_PATH + " = ? ";
        String[] selectionArgs = { ringtone_path };
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_ID));
            }else{
                return -1;
            }
        }
    }

    public static boolean checkRingtone(Context context, String ringtone_path){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_RINGTONES);
        String selection = ToDoDBContract.RingtoneEntry.RINGTONE_PATH + " = ? ";
        String[] selectionArgs = { ringtone_path };
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            return cursor != null && cursor.moveToFirst();
        }
    }

    public static boolean checkSticker(Context context, String sticker_path){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_STICKERS);
        String selection = ToDoDBContract.StickerEntry.STICKER_PATH + " = ? ";
        String[] selectionArgs = { sticker_path };
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            return cursor != null && cursor.moveToFirst();
        }
    }

    public static boolean insertSticker(Context context, String sticker_path, String sticker_title) {
        // Tạo đối tượng ContentValues để lưu trữ giá trị cần chèn vào cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.StickerEntry.STICKER_PATH, sticker_path);
        values.put(ToDoDBContract.StickerEntry.STICKER_TITLE, sticker_title);

        // Xây dựng URI để thực hiện thao tác chèn vào cơ sở dữ liệu
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_STICKERS);

        try {
            // Thực hiện thao tác chèn vào cơ sở dữ liệu
            Uri insertedUri = context.getContentResolver().insert(newUri, values);

            if (insertedUri != null) {
                // Nếu chèn thành công, trả về true
                return true;
            } else {
                // Nếu chèn thất bại, thông báo cho người dùng
                Toast.makeText(context, "Failed to insert sticker", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            // Bắt lỗi nếu có sự cố khi thao tác với cơ sở dữ liệu
            Log.e("InsertSticker", "Error inserting sticker", e);
            Toast.makeText(context, "Error inserting sticker", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


//    public boolean insertMissionRingtone(Context context, int mission_id, int ringtone_id){
//        Uri newUri = Uri.withAppendedPath(ToDoDBContract.Mission_RingtoneEntry.CONTENT_URI, "mission_ringtone");
//        ContentValues values = new ContentValues();
//        values.put(ToDoDBContract.Mission_RingtoneEntry.MISSION_ID, mission_id);
//        values.put(ToDoDBContract.Mission_RingtoneEntry.RINGTONE_ID, ringtone_id);
//        context.getContentResolver().insert(newUri, values);
//        return true;
//    }

    public static int findnotificationbyTimeDate(Context context, String time, String date){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_NOTIFICATIONS);
        String selection = ToDoDBContract.NotificationEntry.NOTIFICATION_TIME + " = ? AND " + ToDoDBContract.NotificationEntry.NOTIFICATION_DATE + " = ? ";
        String[] selectionArgs = { time, date };
        int id = -1;
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.NotificationEntry.NOTIFICATION_ID));
            }
            return id;
        } catch (Exception e) {
            Log.e("findnotificationbyTimeDate", "Error querying database: " + e.getMessage());
            return -1;
        }
    }

    public static int findmissionbytitle(Context context, String title){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);
        String selection = ToDoDBContract.MissionEntry.MISSION_TITLE + " = ? ";
        String[] selectionArgs = { title };
        int id = -1;
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.MissionEntry.MISSION_ID));
            }
            return id;
        } catch (Exception e) {
            Log.e("findmissionbytitle", "Error querying database: " + e.getMessage());
            return -1;
        }
    }

    public static int findmissionbyDateTime(Context context, String date, String time){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);
        String selection = ToDoDBContract.MissionEntry.MISSION_DATE + " = ? AND " + ToDoDBContract.MissionEntry.MISSION_TIME + " = ? ";
        String[] selectionArgs = { date, time };
        int id = -1;
        try (Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.MissionEntry.MISSION_ID));
            }
            return id;
        } catch (Exception e) {
            Log.e("findmissionbyDateTime", "Error querying database: " + e.getMessage());
            return -1;
        }

    }

    public static boolean updateMission(Context context, int sticker_id, int ringTone_id, String date,
                                        String describe, String isNotify, String isRepeat, String repeatType,
                                        String time, String title, int category_id,
                                        String repeatNo, String reminder, String reminderType,
                                        String isSticker, String isActive, int id) {

        // Tạo đối tượng ContentValues để lưu trữ giá trị cần cập nhật
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.MissionEntry.MISSION_TITLE, title);
        values.put(ToDoDBContract.MissionEntry.MISSION_DATE, date);
        values.put(ToDoDBContract.MissionEntry.MISSION_TIME, time);
        values.put(ToDoDBContract.MissionEntry.MISSION_DESCRIPTION, describe);
        values.put(ToDoDBContract.MissionEntry.MISSION_isREPEAT, isRepeat);
        values.put(ToDoDBContract.MissionEntry.MISSION_REPEAT_NO, repeatNo);
        values.put(ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE, repeatType);
        values.put(ToDoDBContract.MissionEntry.MISSION_isNOTIFY, isNotify);
        values.put(ToDoDBContract.MissionEntry.MISSION_REMINDER, reminder);
        values.put(ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID, category_id);
        values.put(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID, ringTone_id);
        values.put(ToDoDBContract.MissionEntry.MISSION_STICKER_ID, sticker_id);
        values.put(ToDoDBContract.MissionEntry.MISSION_isSTICKER, isSticker);
        values.put(ToDoDBContract.MissionEntry.MISSION_isACTIVE, isActive);
        values.put(ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE, reminderType);

        // Xây dựng URI để thực hiện thao tác cập nhật vào cơ sở dữ liệu
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);


        // Cập nhật dữ liệu
        String selection = ToDoDBContract.MissionEntry.MISSION_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        try {
            int rowsUpdated = context.getContentResolver().update(newUri, values, selection, selectionArgs);

            if (rowsUpdated > 0) {
                // Nếu cập nhật thành công
                Toast.makeText(context, "Mission updated successfully", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                // Nếu không có dòng nào bị ảnh hưởng
                Toast.makeText(context, "Failed to update mission", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            // Bắt lỗi nếu có sự cố khi thao tác với cơ sở dữ liệu
            Log.e("UpdateMission", "Error updating mission", e);
            Toast.makeText(context, "Error updating mission", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


//    public static boolean updateMission_RingTone(Context context, int mission_ringtone_id, String mission_reminder_type){
//        Uri newUri = Uri.withAppendedPath(ToDoDBContract.MissionEntry.CONTENT_URI, "mission");
//        ContentValues values = new ContentValues();
//        values.put(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID, mission_ringtone_id);
//        String selection = ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE + " = ? ";
//        String[] selectionArgs = { mission_reminder_type };
//        int row = context.getContentResolver().update(newUri, values, selection, selectionArgs);
//        return row > 0;
//    }

        public static boolean updateCategory(Context context, int id, String category_title, int category_user_id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_CATEGORIES);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.CategoryEntry.CATEGORY_TITLE, category_title);
        values.put(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID, category_user_id);
        String selection = ToDoDBContract.CategoryEntry.CATEGORY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().update(newUri, values, selection, selectionArgs);
        return row > 0;
    }

    public static int getCategoryId(Context context, String category_title){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_CATEGORIES);
        String selection = ToDoDBContract.CategoryEntry.CATEGORY_TITLE + " = ? ";
        String[] selectionArgs = { category_title };
        Cursor cursor = context.getContentResolver().query(newUri, null, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_ID));
            cursor.close();
            return id;
        }else{
            return -1;
        }
    }

    public static boolean checkCategory(Context context, String category_title){
        return getCategoryId(context, category_title) != -1;
    }


    public static boolean updateRingtone(Context context, int id, String ringtone_title, String ringtone_path){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_RINGTONES);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.RingtoneEntry.RINGTONE_TITLE, ringtone_title);
        values.put(ToDoDBContract.RingtoneEntry.RINGTONE_PATH, ringtone_path);
        String selection = ToDoDBContract.RingtoneEntry.RINGTONE_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().update(newUri, values, selection, selectionArgs);
        return row > 0;
    }

    public static boolean updateSticker(Context context, int id, int sticker_path, String sticker_title){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_STICKERS);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.StickerEntry.STICKER_PATH, sticker_path);
        values.put(ToDoDBContract.StickerEntry.STICKER_TITLE, sticker_title);
        String selection = ToDoDBContract.StickerEntry.STICKER_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().update(newUri, values, selection, selectionArgs);
        return row > 0;
    }

//    public boolean updateMissionRingtone(Context context, int id, int mission_id, int ringtone_id){
//        Uri newUri = Uri.withAppendedPath(ToDoDBContract.Mission_RingtoneEntry.CONTENT_URI, "mission_ringtone");
//        ContentValues values = new ContentValues();
//        values.put(ToDoDBContract.Mission_RingtoneEntry.MISSION_ID, mission_id);
//        values.put(ToDoDBContract.Mission_RingtoneEntry.RINGTONE_ID, ringtone_id);
//        String selection = ToDoDBContract.Mission_RingtoneEntry.MISSION_RINGTONE_ID + " = ? ";
//        String[] selectionArgs = { String.valueOf(id) };
//        context.getContentResolver().update(newUri, values, selection, selectionArgs);
//        return true;
//    }

    public static boolean deleteMission(Context context, int id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);
        String selection = ToDoDBContract.MissionEntry.MISSION_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        // Xóa tất cả taskbeside và notification liên quan đến mission trước khi xóa mission
        deleteALLTaskbeside(context, id);
        deleteALLNotification(context, id);

        // Xóa mission chính
        int row = context.getContentResolver().delete(newUri, selection, selectionArgs);
        return row > 0;
    }

    public static boolean deleteCategory(Context context, int id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_CATEGORIES);
        String selection = ToDoDBContract.CategoryEntry.CATEGORY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        // Xóa tất cả mission liên quan đến category trước khi xóa category
        deleteALLMission(context, id);

        // Xóa category chính
        int row = context.getContentResolver().delete(newUri, selection, selectionArgs);
        return row > 0;
    }

    public static boolean deleteRingtone(Context context, int id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_RINGTONES);
        String selection = ToDoDBContract.RingtoneEntry.RINGTONE_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().delete(newUri, selection, selectionArgs);
        return row > 0;
    }

    public static boolean deleteSticker(Context context, int id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_STICKERS);
        String selection = ToDoDBContract.StickerEntry.STICKER_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().delete(newUri, selection, selectionArgs);
        return row > 0;
    }

    public static boolean deleteTaskbeside(Context context, int id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_TASKBESIDES);
        String selection = ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().delete(newUri, selection, selectionArgs);
        return row > 0;
    }

    public static boolean deleteNotification(Context context, int id){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_NOTIFICATIONS);
        String selection = ToDoDBContract.NotificationEntry.NOTIFICATION_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int row = context.getContentResolver().delete(newUri, selection, selectionArgs);
        return row > 0;
    }

    public static Cursor getCursorCategory(Context context, int userId){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_CATEGORIES);
        String selection = ToDoDBContract.CategoryEntry.CATEGORY_USER_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(userId) };
        return context.getContentResolver().query(newUri, null, selection, selectionArgs, null);
    }

    public static void deleteALLCategory(Context context, int userId){
        try (Cursor cursor = getCursorCategory(context, userId)) {
            if (cursor != null && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_ID));
                    deleteCategory(context, categoryId);
                }
            }
        }catch (Exception e){
            Log.e("deleteALLCategory", "Error querying database: " + e.getMessage());
        }
        // Đảm bảo đóng Cursor sau khi sử dụng
    }

    public static Cursor getCursorMission(Context context, int categoryId){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);
        String selection = ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(categoryId) };
        return context.getContentResolver().query(newUri, null, selection, selectionArgs, null);
    }

    public static void deleteALLMission(Context context, int categoryId){
        try (Cursor cursor = getCursorMission(context, categoryId)) {
            if (cursor != null && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    int missionId = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.MissionEntry.MISSION_ID));
                    deleteMission(context, missionId);
                }
            }
        }catch (Exception e){
            Log.e("deleteALLMission", "Error querying database: " + e.getMessage());
        }
        // Đảm bảo đóng Cursor sau khi sử dụng
    }

    public static Cursor getCursorRingtone(Context context){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_RINGTONES);
        return context.getContentResolver().query(newUri, null, null, null, null);
    }

    public static Cursor getCursorSticker(Context context){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_STICKERS);
        return context.getContentResolver().query(newUri, null, null, null, null);
    }

    public static Cursor getCursorTaskbeside(Context context, int missionId){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_TASKBESIDES);
        String selection = ToDoDBContract.TaskbesideEntry.TASKBESIDE_MISSION_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(missionId) };
        return context.getContentResolver().query(newUri, null, selection, selectionArgs, null);
    }

    public static void deleteALLTaskbeside(Context context, int missionId) {
        try (Cursor cursor = getCursorTaskbeside(context, missionId)) {
            if (cursor != null && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    int taskbesideId = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID));
                    deleteTaskbeside(context, taskbesideId);
                }
            }
        }catch (Exception e){
            Log.e("deleteALLTaskbeside", "Error querying database: " + e.getMessage());
        }
        // Đảm bảo đóng Cursor sau khi sử dụng
    }

    public static Cursor getCursorNotification(Context context, int missionId){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_NOTIFICATIONS);
        String selection = ToDoDBContract.NotificationEntry.NOTIFICATION_MISSION_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(missionId) };
        return context.getContentResolver().query(newUri, null, selection, selectionArgs, null);
    }

    public static void deleteALLNotification(Context context, int missionId) {
        try (Cursor cursor = getCursorNotification(context, missionId)) {
            if (cursor != null && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    int notificationId = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.NotificationEntry.NOTIFICATION_ID));
                    deleteNotification(context, notificationId);
                    new AlarmScheduler().cancelAlarm(context, missionId, notificationId);
                }
            }
        }catch (Exception e){
            Log.e("deleteALLNotification", "Error querying database: " + e.getMessage());
        }
    }

    public static Cursor getCursorUser(Context context){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_USERS);
        return context.getContentResolver().query(newUri, null, null, null, null);
    }

    public static void updateActiveMission(Context context, int id, String isActive){
        Uri newUri = Uri.withAppendedPath(ToDoDBContract.BASE_CONTENT_URI, ToDoDBContract.PATH_MISSIONS);
        ContentValues values = new ContentValues();
        values.put(ToDoDBContract.MissionEntry.MISSION_isACTIVE, isActive);
        String selection = ToDoDBContract.MissionEntry.MISSION_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        int insertedUri = context.getContentResolver().update(newUri, values, selection, selectionArgs);
        if (insertedUri > 0){
            Toast.makeText(context, "Mission updated successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Failed to update mission", Toast.LENGTH_SHORT).show();
        }
    }
}
