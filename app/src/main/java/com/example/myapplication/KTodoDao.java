package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KTodoDao {
    @Query("SELECT * FROM KTodo")
    LiveData<List<KTodo>> getAll(); //LiveData => Todo테이블에 있는 모든 객체를 계속 관찰하고있다가 변경이 일어나면 그것을 자동으로 업데이트하도록한다.
    //getAll() 은 관찰 가능한 객체가 된다.(즉 디비변경시 반응하는)

    @Insert
    void insert(KTodo ktodo);

    @Update
    void update(KTodo ktodo);

    @Query("SELECT * FROM KTodo WHERE name = :mname")
    KTodo getData(String mname);

    @Delete
    void delete(KTodo ktodo);

    @Query("DELETE FROM KTodo")
    void deleteAll();
}
