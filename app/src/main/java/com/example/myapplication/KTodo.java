package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KTodo {
    @PrimaryKey
    @NonNull
    private String name;

    private String kind;

    public KTodo(String name, String kind){
        this.name = name;
        this.kind = kind;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getKind(){
        return kind;
    }

    public void setKind(String kind){
        this.kind = kind;
    }

    @Override
    public String toString(){
        return " kind = " + this.kind + ", name = " + this.name;
    }
}
