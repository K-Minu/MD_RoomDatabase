package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private MDDatabase db;

    public MainViewModel(@NonNull Application application) {
        super(application);

        db = MDDatabase.getAppDatabase(application);
    }

    public void test(Context context){
        Toast.makeText(context,"View모델까진 들와짐",Toast.LENGTH_SHORT).show();
    }

    public LiveData<List<Todo>> getAll(){
        return db.todoDao().getAll();
    }

    public void insert(Todo todo){
        new InsertAsyncTask(db.todoDao()).execute(todo);
    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    private static class InsertAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mTodoDao;

        public  InsertAsyncTask(TodoDao todoDao){
            this.mTodoDao = todoDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(Todo... todos) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            mTodoDao.insert(todos[0]);
            return null;
        }
    }
}
