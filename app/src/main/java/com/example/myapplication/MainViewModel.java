package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.StringTokenizer;

public class MainViewModel extends AndroidViewModel {
    private KDatabase db;

    private final String INSERT = "INSERT";
    private final String UPDATE = "UPDATE";
    private final String DELETE = "DELETE";
    private final String CLEAR = "CLEAR";

    public MainViewModel(@NonNull Application application) {
        super(application);

        db = KDatabase.getAppDatabase(application);
    }

    public LiveData<List<KTodo>> getAll(){
        return db.ktodoDao().getAll();
    }

    public void insert(KTodo ktodo){
        new DaoAsyncTask(db.ktodoDao(),INSERT, ktodo.getName(),ktodo.getKind()).execute(ktodo);
    }

    public void delete(int ss) {
        new DaoAsyncTask(db.ktodoDao(),DELETE, "", "").execute();
    }

    public void Destroydb(){db.destroyInstance();}


    private static class DaoAsyncTask extends AsyncTask<KTodo,Void,Void> {
        private KTodoDao mTodoDao;
        private String mType;
        private String mName;
        private String mKind;

        private DaoAsyncTask (KTodoDao ktodoDao, String type, String name, String kind) {
            this.mTodoDao = ktodoDao;
            this.mType = type;
            this.mName = name;
            this.mKind = kind;
        }

        @Override
        protected Void doInBackground(KTodo... ktodos) {

            if(mType.equals("INSERT")){
                mTodoDao.insert(ktodos[0]);
            }
            else if(mType.equals("UPDATE")){
                if(mTodoDao.getData(mName) != null){
                    mTodoDao.update(ktodos[0]);
                }
            }
            else if(mType.equals("DELETE")){
                if(mTodoDao.getData(mName) != null) {
                    mTodoDao.delete(mTodoDao.getData(mName));
                }
            }
            else if(mType.equals("CLEAR")){
                mTodoDao.deleteAll();
            }
            return null;
        }
    }
}
