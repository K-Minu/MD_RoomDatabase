package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {KTodo.class}, version = 1)
public abstract class KDatabase extends RoomDatabase {
    // 싱글톤
    private static KDatabase INSTANCE;

    public abstract KTodoDao ktodoDao();

    // 디비 객체생성
    public static KDatabase getAppDatabase(Context context) {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, KDatabase.class, "ktodo-db").build();
        }
        return INSTANCE;
    }

    // 디비 객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
