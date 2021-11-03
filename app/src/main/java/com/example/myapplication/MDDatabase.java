package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Todo.class}, version = 1)
public abstract class MDDatabase extends RoomDatabase {
    //데이터베이스를 매번 생성하는건 리소스를 많이사용하므로 싱글톤 권장.
    private static MDDatabase INSTANCE;

    public abstract TodoDao todoDao();

    //디비객체생성 가져오기
    public static MDDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, MDDatabase.class , "todo-db")
                    .build();

//            INSTANCE = Room.databaseBuilder(context, MDDatabase.class, "Sample.db")
//                    .createFromAsset("database/myapp.db")
//                    .build();
        }
        return  INSTANCE;
    }

    //디비객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
