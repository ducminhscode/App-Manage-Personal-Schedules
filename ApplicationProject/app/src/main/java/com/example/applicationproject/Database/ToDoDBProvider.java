package com.example.applicationproject.Database;

import static com.example.applicationproject.View_Controller.DAO.insertMission;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class ToDoDBProvider extends ContentProvider {
    private static final int cUSER = 100;
    private static final int cUSER_ID = 101;
    private static final int cMISSION = 200;
    private static final int cMISSION_ID = 201;
    private static final int cCATEGORY = 300;
    private static final int cCATEGORY_ID = 301;
    private static final int cRINGTONE = 400;
    private static final int cRINGTONE_ID = 401;
    private static final int cSTICKER = 500;
    private static final int cSTICKER_ID = 501;
    private static final int cTASKBESIDE = 600;
    private static final int cTASKBESIDE_ID = 601;
    private static final int cNOTIFICATION = 700;
    private static final int cNOTIFICATION_ID = 701;


    // URI matcher để xác định các URI
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_USERS, cUSER);   // URI cho tất cả user
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_USERS + "/#", cUSER_ID); // URI cho một user cụ thể
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_MISSIONS, cMISSION);   // URI cho tất cả các mission
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_MISSIONS + "/#", cMISSION_ID); // URI cho một mission cụ thể
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_CATEGORIES, cCATEGORY);   // URI cho tất cả category
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_CATEGORIES + "/#", cCATEGORY_ID); // URI cho một category cụ thể
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_RINGTONES, cRINGTONE);   // URI cho tất cả ringTone
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_RINGTONES + "/#", cRINGTONE_ID); // URI cho một ringTone cụ thể
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_STICKERS, cSTICKER);   // URI cho tất cả mission_ringtone
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_STICKERS + "/#", cSTICKER_ID); // URI cho một mission_ringtone cụ thể
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_TASKBESIDES, cTASKBESIDE); // URI cho tất cả taskbeside
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_TASKBESIDES + "/#", cTASKBESIDE_ID); // URI cho một taskbeside cụ thể
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_NOTIFICATIONS, cNOTIFICATION); // URI cho tất cả notification
        sUriMatcher.addURI(ToDoDBContract.CONTENT_AUTHORITY, ToDoDBContract.PATH_NOTIFICATIONS + "/#", cNOTIFICATION_ID); // URI cho một notification cụ thể
    }

    private ToDoDBHelper toDoDBHelper;

    @Override
    public boolean onCreate() {
        toDoDBHelper = new ToDoDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = toDoDBHelper.getReadableDatabase();

        // khởi tạo cursor
        Cursor cursor = null;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case cUSER:
                cursor = db.query(ToDoDBContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cMISSION:
                cursor = db.query(ToDoDBContract.MissionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cMISSION_ID:
                cursor = db.query(ToDoDBContract.MissionEntry.TABLE_NAME, projection, ToDoDBContract.MissionEntry.MISSION_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            case cUSER_ID:
                cursor = db.query(ToDoDBContract.UserEntry.TABLE_NAME, projection, ToDoDBContract.UserEntry.USER_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            case cCATEGORY:
                cursor = db.query(ToDoDBContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cCATEGORY_ID:
                cursor = db.query(ToDoDBContract.CategoryEntry.TABLE_NAME, projection, ToDoDBContract.CategoryEntry.CATEGORY_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            case cRINGTONE:
                cursor = db.query(ToDoDBContract.RingtoneEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cRINGTONE_ID:
                cursor = db.query(ToDoDBContract.RingtoneEntry.TABLE_NAME, projection, ToDoDBContract.RingtoneEntry.RINGTONE_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            case cTASKBESIDE:
                cursor = db.query(ToDoDBContract.TaskbesideEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cTASKBESIDE_ID:
                cursor = db.query(ToDoDBContract.TaskbesideEntry.TABLE_NAME, projection, ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            case cSTICKER:
                cursor = db.query(ToDoDBContract.StickerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cSTICKER_ID:
                cursor = db.query(ToDoDBContract.StickerEntry.TABLE_NAME, projection, ToDoDBContract.StickerEntry.STICKER_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            case cNOTIFICATION:
                cursor = db.query(ToDoDBContract.NotificationEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case cNOTIFICATION_ID:
                cursor = db.query(ToDoDBContract.NotificationEntry.TABLE_NAME, projection, ToDoDBContract.NotificationEntry.NOTIFICATION_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case cUSER:
                return ToDoDBContract.UserEntry.CONTENT_LIST_TYPE;
            case cUSER_ID:
                return ToDoDBContract.UserEntry.CONTENT_ITEM_TYPE;
            case cMISSION:
                return ToDoDBContract.MissionEntry.CONTENT_LIST_TYPE;
            case cMISSION_ID:
                return ToDoDBContract.MissionEntry.CONTENT_ITEM_TYPE;
            case cCATEGORY:
                return ToDoDBContract.CategoryEntry.CONTENT_LIST_TYPE;
            case cCATEGORY_ID:
                return ToDoDBContract.CategoryEntry.CONTENT_ITEM_TYPE;
            case cRINGTONE:
                return ToDoDBContract.RingtoneEntry.CONTENT_LIST_TYPE;
            case cRINGTONE_ID:
                return ToDoDBContract.RingtoneEntry.CONTENT_ITEM_TYPE;
            case cSTICKER:
                return ToDoDBContract.StickerEntry.CONTENT_LIST_TYPE;
            case cSTICKER_ID:
                return ToDoDBContract.StickerEntry.CONTENT_ITEM_TYPE;
            case cTASKBESIDE:
                return ToDoDBContract.TaskbesideEntry.CONTENT_LIST_TYPE;
            case cTASKBESIDE_ID:
                return ToDoDBContract.TaskbesideEntry.CONTENT_ITEM_TYPE;
            case cNOTIFICATION:
                return ToDoDBContract.NotificationEntry.CONTENT_LIST_TYPE;
            case cNOTIFICATION_ID:
                return ToDoDBContract.NotificationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case cUSER:
                return insertUser(uri, contentValues);
            case cMISSION:
                return insertMission(uri, contentValues);
            case cCATEGORY:
                return insertCategory(uri, contentValues);
            case cRINGTONE:
                return insertRingtone(uri, contentValues);
            case cSTICKER:
                return insertSticker(uri, contentValues);
            case cTASKBESIDE:
                return insertTaskbeside(uri, contentValues);
            case cNOTIFICATION:
                return insertNotification(uri, contentValues);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private Uri insertMission(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.MissionEntry.TABLE_NAME, null, contentValues);
        if (id == -1){
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertNotification(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.NotificationEntry.TABLE_NAME, null, contentValues);
        if (id == -1){
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertTaskbeside(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.TaskbesideEntry.TABLE_NAME, null, contentValues);
        if (id == -1){
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertSticker(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.StickerEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertRingtone(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.RingtoneEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertCategory(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.CategoryEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertUser(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        long id = db.insert(ToDoDBContract.UserEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case cUSER:
                rowsDeleted = db.delete(ToDoDBContract.UserEntry.TABLE_NAME, s, strings);
                break;
            case cUSER_ID:
                rowsDeleted = db.delete(ToDoDBContract.UserEntry.TABLE_NAME, ToDoDBContract.UserEntry.USER_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case cMISSION:
                rowsDeleted = db.delete(ToDoDBContract.MissionEntry.TABLE_NAME, s, strings);
                break;
            case cMISSION_ID:
                rowsDeleted = db.delete(ToDoDBContract.MissionEntry.TABLE_NAME, ToDoDBContract.MissionEntry.MISSION_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case cCATEGORY:
                rowsDeleted = db.delete(ToDoDBContract.CategoryEntry.TABLE_NAME, s, strings);
                break;
            case cCATEGORY_ID:
                rowsDeleted = db.delete(ToDoDBContract.CategoryEntry.TABLE_NAME, ToDoDBContract.CategoryEntry.CATEGORY_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case cRINGTONE:
                rowsDeleted = db.delete(ToDoDBContract.RingtoneEntry.TABLE_NAME, s, strings);
                break;
            case cRINGTONE_ID:
                rowsDeleted = db.delete(ToDoDBContract.RingtoneEntry.TABLE_NAME, ToDoDBContract.RingtoneEntry.RINGTONE_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case cSTICKER:
                rowsDeleted = db.delete(ToDoDBContract.StickerEntry.TABLE_NAME, s, strings);
                break;
            case cSTICKER_ID:
                rowsDeleted = db.delete(ToDoDBContract.StickerEntry.TABLE_NAME, ToDoDBContract.StickerEntry.STICKER_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case cTASKBESIDE:
                rowsDeleted = db.delete(ToDoDBContract.TaskbesideEntry.TABLE_NAME, s, strings);
                break;
            case cTASKBESIDE_ID:
                rowsDeleted = db.delete(ToDoDBContract.TaskbesideEntry.TABLE_NAME, ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case cNOTIFICATION:
                rowsDeleted = db.delete(ToDoDBContract.NotificationEntry.TABLE_NAME, s, strings);
                break;
            case cNOTIFICATION_ID:
                rowsDeleted = db.delete(ToDoDBContract.NotificationEntry.TABLE_NAME, ToDoDBContract.NotificationEntry.NOTIFICATION_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowsDeleted != 0){
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (contentValues == null){
            return 0;
        }
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();

        int rowsUpdate = 0;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case cUSER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.UserEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
                break;
            case cUSER_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.UserEntry.TABLE_NAME, contentValues, ToDoDBContract.UserEntry.USER_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
                break;
            case cMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.MissionEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
            case cMISSION_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.MissionEntry.TABLE_NAME, contentValues, ToDoDBContract.MissionEntry.MISSION_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
            case cCATEGORY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return  rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.CategoryEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
                break;
            case cCATEGORY_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.CategoryEntry.TABLE_NAME, contentValues, ToDoDBContract.CategoryEntry.CATEGORY_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
                break;
            case cRINGTONE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.RingtoneEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
                break;
            case cRINGTONE_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.RingtoneEntry.TABLE_NAME, contentValues, ToDoDBContract.RingtoneEntry.RINGTONE_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
                break;
            case cSTICKER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.StickerEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
                break;
            case cSTICKER_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.StickerEntry.TABLE_NAME, contentValues, ToDoDBContract.StickerEntry.STICKER_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
                break;
            case cTASKBESIDE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.TaskbesideEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
                break;
            case cTASKBESIDE_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.TaskbesideEntry.TABLE_NAME, contentValues, ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
                break;
            case cNOTIFICATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.NotificationEntry.TABLE_NAME, contentValues, s, strings);
                    }
                }
                break;
            case cNOTIFICATION_ID:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (contentValues.isEmpty()) {
                        return rowsUpdate;
                    }else{
                        rowsUpdate = db.update(ToDoDBContract.NotificationEntry.TABLE_NAME, contentValues, ToDoDBContract.NotificationEntry.NOTIFICATION_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowsUpdate != 0){
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdate;
    }
}
