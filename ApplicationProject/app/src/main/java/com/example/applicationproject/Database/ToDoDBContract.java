package com.example.applicationproject.Database;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ToDoDBContract {
    // Cấu trúc của Contract
    public static final String CONTENT_AUTHORITY = "com.example.contactsprovider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Định nghĩa đường dẫn của các bảng (và cũng là đường dẫn URI)
    public static final String PATH_MISSIONS = "mission";
    public static final String PATH_CATEGORIES = "category";
    public static final String PATH_USERS = "user";
    public static final String PATH_RINGTONES = "ringtone";
    public static final String PATH_STICKERS = "sticker";
    public static final String PATH_TASKBESIDES = "taskbeside";
    public static final String PATH_NOTIFICATIONS = "notification";

    // Bảng Contacts
    public static final class UserEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "user";

        // Các cột trong bảng contacts
        public static final String USER_ID = BaseColumns._ID;
        public static final String USER_NAME = "user_name";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_MOBILE = "phone_number";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_ROLE = "user_role";
    }

    public static final class MissionEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MISSIONS);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MISSIONS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MISSIONS;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "mission";

        // Các cột trong bảng contacts
        public static final String MISSION_ID = BaseColumns._ID;
        public static final String MISSION_TITLE = "title";
        public static final String MISSION_DATE = "date";
        public static final String MISSION_TIME = "time";
        public static final String MISSION_isREPEAT = "isRepeat";
        public static final String MISSION_REPEAT_NO = "repeat_no";
        public static final String MISSION_REPEAT_TYPE = "repeat_type";
        public static final String MISSION_DESCRIPTION = "description";
        public static final String MISSION_isNOTIFY = "isNotify";
        public static final String MISSION_REMINDER = "reminder";
        public static final String MISSION_REMINDER_TYPE = "reminder_type";
        public static final String MISSION_CATEGORY_ID = "category_id";
        public static final String MISSION_RINGTONE_ID = "ringtone_id";
        public static final String MISSION_STICKER_ID = "sticker_id";
        public static final String MISSION_isSTICKER = "isSticker";
        public static final String MISSION_isACTIVE = "isActive";
    }

    public static final class CategoryEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "category";

        // Các cột trong bảng contacts
        public static final String CATEGORY_ID = BaseColumns._ID;
        public static final String CATEGORY_TITLE = "title";
        public static final String CATEGORY_USER_ID = "user_id";
    }

    public static final class RingtoneEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RINGTONES);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RINGTONES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RINGTONES;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "ringtone";

        // Các cột trong bảng contacts
        public static final String RINGTONE_ID = BaseColumns._ID;
        public static final String RINGTONE_TITLE = "title";
        public static final String RINGTONE_PATH = "path";
    }

    public static final class StickerEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STICKERS);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STICKERS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STICKERS;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "sticker";

        // Các cột trong bảng contacts
        public static final String STICKER_ID = BaseColumns._ID;
        public static final String STICKER_TITLE = "title";
        public static final String STICKER_PATH = "path";
    }

    public static final class TaskbesideEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKBESIDES);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKBESIDES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKBESIDES;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "taskbeside";

        // Các cột trong bảng contacts
        public static final String TASKBESIDE_ID = BaseColumns._ID;
        public static final String TASKBESIDE_TITLE = "title";
        public static final String TASKBESIDE_MISSION_ID = "mission_id";
    }

    public static final class NotificationEntry implements BaseColumns {
        // URI để truy cập bảng Contacts
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTIFICATIONS);

        // Các loại MIME cho dữ liệu
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;

        // Tên bảng trong cơ sở dữ liệu
        public static final String TABLE_NAME = "notification";

        // Các cột trong bảng contacts
        public static final String NOTIFICATION_ID = BaseColumns._ID;
        public static final String NOTIFICATION_TIME = "notification_time";
        public static final String NOTIFICATION_DATE = "notification_date";
        public static final String NOTIFICATION_MISSION_ID = "mission_id";
    }

//    public static final class Mission_RingtoneEntry implements BaseColumns {
//        // URI để truy cập bảng Contacts
//        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MISSIONS_RINGTONES);
//
//        // Các loại MIME cho dữ liệu
//        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MISSIONS_RINGTONES;
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MISSIONS_RINGTONES;
//
//        // Tên bảng trong cơ sở dữ liệu
//        public static final String TABLE_NAME = "mission_ringtone";
//
//        // Các cột trong bảng contacts
//        public static final String MISSION_RINGTONE_ID = BaseColumns._ID;
//        public static final String MISSION_ID = "mission_id";
//        public static final String RINGTONE_ID = "ringtone_id";
//    }

    @SuppressLint("Range")
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
