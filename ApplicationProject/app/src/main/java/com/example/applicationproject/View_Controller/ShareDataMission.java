package com.example.applicationproject.View_Controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.Notification;
import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.Model.Sticker;
import com.example.applicationproject.Model.Taskbeside;
import com.example.applicationproject.Model.User;
import com.example.applicationproject.View_Controller.ClassTemp.NotificationTemp;
import com.example.applicationproject.View_Controller.ClassTemp.TaskString;

import java.io.Closeable;
import java.util.List;

public class ShareDataMission extends ViewModel {

    private final MutableLiveData<List<String>> datataskList;
    private final MutableLiveData<List<TaskString>> datataskBesideList;
    private final MutableLiveData<List<User>> dataUserList;

    private final MutableLiveData<String> dataCheckFirsTimeIN;
    private final MutableLiveData<String> decription;
    private final MutableLiveData<String> title;

    private final MutableLiveData<List<Sticker>> stickerList;
    private final MutableLiveData<List<Ringtone>> ringtoneList;
    private final MutableLiveData<List<Taskbeside>> taskbesideList;
    private final MutableLiveData<List<Notification>> notificationList;

    public final MutableLiveData<List<Mission>> mainList;
    public final MutableLiveData<List<Category>> categoryList;
    public final MutableLiveData<String> dataRemindType;

    private final MutableLiveData<String> dataTime;
    private final MutableLiveData<Integer> dataTimeHour;
    private final MutableLiveData<Integer> dataTimeMinute;

    private final MutableLiveData<String> dataDate;
    private final MutableLiveData<Integer> dataTaskBesideBool;

    private final MutableLiveData<Integer> dataCategory;
    private final MutableLiveData<Integer> dataRingTone;
    private final MutableLiveData<Integer> dataSticker;

    private final MutableLiveData<String> dataNotice;
    private final MutableLiveData<Integer> dataNoticeBool;

    private final MutableLiveData<String> dataRepeatType;
    private final MutableLiveData<Integer> dataRepeatNo;
    private final MutableLiveData<String> dataRepeat;
    private final MutableLiveData<Integer> dataRepeatBool;

    private final MutableLiveData<String> reminder1;
    private final MutableLiveData<String> reminder2;
    private final MutableLiveData<String> reminder3;
    private final MutableLiveData<String> reminder4;
    private final MutableLiveData<String> reminder5;
    private final MutableLiveData<String> reminder6;
    private final MutableLiveData<String> reminder7;
    private final MutableLiveData<List<String>> datareminders;
    private final MutableLiveData<List<NotificationTemp>> dataNotificationTemp;

    private final MutableLiveData<Integer> dataOrdering;
    private final MutableLiveData<Integer> dataChangeLanguage;


    public ShareDataMission() {
        dataNoticeBool = new MutableLiveData<>();
        dataNotice = new MutableLiveData<>();
        dataSticker = new MutableLiveData<>();
        dataRingTone = new MutableLiveData<>();
        dataCategory = new MutableLiveData<>();
        ringtoneList = new MutableLiveData<>();
        decription = new MutableLiveData<>();
        datataskList = new MutableLiveData<>();
        datataskBesideList = new MutableLiveData<>();
        dataUserList = new MutableLiveData<>();
        dataCheckFirsTimeIN = new MutableLiveData<>();
        title = new MutableLiveData<>();
        stickerList = new MutableLiveData<>();
        taskbesideList = new MutableLiveData<>();
        notificationList = new MutableLiveData<>();
        mainList = new MutableLiveData<>();
        categoryList = new MutableLiveData<>();
        dataRemindType = new MutableLiveData<>();
        dataTime = new MutableLiveData<>();
        dataTimeHour = new MutableLiveData<>();
        dataTimeMinute = new MutableLiveData<>();
        dataDate = new MutableLiveData<>();
        dataTaskBesideBool = new MutableLiveData<>();
        dataRepeatType = new MutableLiveData<>();
        dataRepeatNo = new MutableLiveData<>();
        dataRepeat = new MutableLiveData<>();
        dataRepeatBool = new MutableLiveData<>();
        reminder1 = new MutableLiveData<>();
        reminder2 = new MutableLiveData<>();
        reminder3 = new MutableLiveData<>();
        reminder4 = new MutableLiveData<>();
        reminder5 = new MutableLiveData<>();
        reminder6 = new MutableLiveData<>();
        reminder7 = new MutableLiveData<>();
        datareminders = new MutableLiveData<>();
        dataNotificationTemp = new MutableLiveData<>();
        dataOrdering = new MutableLiveData<>();
        dataChangeLanguage = new MutableLiveData<>();
    }

    //    private final MutableLiveData<String>
    public LiveData<Integer> getDataOrdering() {
        return dataOrdering;
    }

    public void setDataOrdering(int value) {
        dataOrdering.setValue(value);
    }

    public LiveData<Integer> getDataChangeLanguage() {
        return dataChangeLanguage;
    }

    public void setDataChangeLanguage(int value) {
        dataChangeLanguage.setValue(value);
    }

    public LiveData<List<User>> getUserList() {
        return dataUserList;
    }
    public void setUserList(List<User> value) {
        dataUserList.setValue(value);
    }

    public LiveData<String> getTitle() {
        return title;
    }
    public void setTitle(String value) {
        title.setValue(value);
    }

    public LiveData<String> getDecription() {
        return decription;
    }
    public void setDecription(String value) {
        decription.setValue(value);
    }

    public LiveData<String> getDataCheckFirsTimeIN() {
        return dataCheckFirsTimeIN;
    }
    public void setDataCheckFirsTimeIN(String value) {
        dataCheckFirsTimeIN.setValue(value);
    }

    public LiveData<List<Sticker>> getStickerList() {
        return stickerList;
    }
    public void setStickerList(List<Sticker> value) {
        stickerList.setValue(value);
    }
    public LiveData<List<Ringtone>> getRingtoneList() {
        return ringtoneList;
    }
    public void setRingtoneList(List<Ringtone> value) {
        ringtoneList.setValue(value);
    }
    public LiveData<List<Taskbeside>> getTaskbesideList() {
        return taskbesideList;
    }
    public void setTaskbesideList(List<Taskbeside> value) {
        taskbesideList.setValue(value);
    }
    public LiveData<List<Notification>> getNotificationList() {
        return notificationList;
    }
    public void setNotificationList(List<Notification> value) {
        notificationList.setValue(value);
    }

    public LiveData<String> getDataRemindType() {
        return dataRemindType;
    }
    public void setDataRemindType(String value) {
        dataRemindType.setValue(value);
    }

    public LiveData<List<Category>> getCategoryList(){
        return categoryList;
    }
    public void setCategoryList(List<Category> value){
        categoryList.setValue(value);
    }

    public LiveData<List<Mission>> getMainList() {
        return mainList;
    }
    public void setMainList(List<Mission> value) {
        mainList.setValue(value);
    }

    public LiveData<List<TaskString>> getDatataskBesideList() {
        return datataskBesideList;
    }
    public void setDatataskBesideList(List<TaskString> value) {
        datataskBesideList.setValue(value);
    }

    public LiveData<String> getReminder2() {
        return reminder2;
    }
    public void setReminder2(String value) {
        reminder2.setValue(value);
    }
    public LiveData<String> getReminder3() {
        return reminder3;
    }
    public void setReminder3(String value) {
        reminder3.setValue(value);
    }
    public LiveData<String> getReminder4() {
        return reminder4;
    }
    public void setReminder4(String value) {
        reminder4.setValue(value);
    }
    public LiveData<String> getReminder5() {
        return reminder5;
    }
    public void setReminder5(String value) {
        reminder5.setValue(value);
    }
    public LiveData<String> getReminder6() {
        return reminder6;
    }
    public void setReminder6(String value) {
        reminder6.setValue(value);
    }
    public LiveData<String> getReminder7() {
        return reminder7;
    }
    public void setReminder7(String value) {
        reminder7.setValue(value);
    }

    public MutableLiveData<List<String>> getDatareminders() {
        return datareminders;
    }

    public void setDatareminders(List<String> value) {
        datareminders.setValue(value);
    }

    public LiveData<List<String>> getDatataskList() {
        return datataskList;
    }
    public void setDatataskList(List<String> value) {
        datataskList.setValue(value);
    }

    public LiveData<Integer> getDataSticker() {
        return dataSticker;
    }
    public void setDataSticker(int value) {
        dataSticker.setValue(value);
    }

    public LiveData<String> getDataDate() {
        return dataDate;
    }
    public void setDataDate(String value) {
        dataDate.setValue(value);
    }

    public LiveData<Integer> getDataTaskBesideBool() {
        return dataTaskBesideBool;
    }
    public void setDataTaskBesideBool(int value) {
        dataTaskBesideBool.setValue(value);
    }

    public LiveData<Integer> getDataRingTone() {
        return dataRingTone;
    }
    public void setDataRingTone(int value) {
        dataRingTone.setValue(value);
    }

    public LiveData<Integer> getDataCategory() {
        return dataCategory;
    }
    public void setDataCategory(int value) {
        dataCategory.setValue(value);
    }

    public LiveData<Integer> getDataRepeatNo() {
        return dataRepeatNo;
    }
    public void setDataRepeatNo(Integer value) {
        dataRepeatNo.setValue(value);
    }

    public LiveData<String> getDataRepeatType() {
        return dataRepeatType;
    }
    public void setDataRepeatType(String value) {
        dataRepeatType.setValue(value);
    }

    public LiveData<String> getReminder() {
        return reminder1;
    }
    public void setReminder(String value) {
        reminder1.setValue(value);
    }

    public void setDataTime(String value) {
        dataTime.setValue(value);
    }
    public LiveData<String> getDataTime() {
        return dataTime;
    }

    public void setDataNotice(String value) {
        dataNotice.setValue(value);
    }
    public LiveData<String> getDataNotice()
    {
        return dataNotice;
    }

    public void setDataRepeat(String value) {
        dataRepeat.setValue(value);
    }
    public LiveData<String> getDataRepeat()
    {
        return dataRepeat;
    }

    public void setDataTimeHour(int value) {
        dataTimeHour.setValue(value);
    }
    public LiveData<Integer> getDataTimeHour() {
        return dataTimeHour;
    }

    public void setDataTimeMinute(int value) {
        dataTimeMinute.setValue(value);
    }
    public LiveData<Integer> getDataTimeMinute() {
        return dataTimeMinute;
    }

    public void setDataNoticeBool(int value) {
        dataNoticeBool.setValue(value);
    }
    public LiveData<Integer> getDataNoticeBool() {
        return dataNoticeBool;
    }

    public void setDataRepeatBool(int value) {
        dataRepeatBool.setValue(value);
    }
    public LiveData<Integer> getDataRepeatBool() {
        return dataRepeatBool;
    }
    public LiveData<List<NotificationTemp>> getDataNotificationTemp() {
        return dataNotificationTemp;
    }
    public void setDataNotificationTemp(List<NotificationTemp> value) {
        dataNotificationTemp.setValue(value);
    }

}
