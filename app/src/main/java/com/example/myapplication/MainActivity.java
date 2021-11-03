package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mTodoEditText;
    private TextView mResultTextView;
    private BackPressedForFinish backPressedForFinish;

//    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"sampledata/trash.txt";
//    private InputStream in = getResources().openRawResource(R.raw.trash);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BackPressedForFinish 객체 생성
        backPressedForFinish = new BackPressedForFinish(this);

        mTodoEditText = findViewById(R.id.textedit);
        mResultTextView = findViewById(R.id.result_text);

        // ViewModel Test... 안됌 .. 11/1일
        // 11/3일 드디어 해결..
        MainViewModel vModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);
        
        vModel.test(this);


        //디비생성
//        MDDatabase db = MDDatabase.getAppDatabase(this);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        vModel.getAll().observe(this, todos -> mResultTextView.setText(todos.toString()));
//        db.todoDao().getAll().observe(this, todos -> mResultTextView.setText(todos.toString()));

        //DB 데이터 불러오기 (SELECT)
        mResultTextView.setText(vModel.getAll().toString());
//        mResultTextView.setText(db.todoDao().getAll().toString());

        //추가버튼시 DB에 데이터 INSERT
        findViewById(R.id.add_button).setOnClickListener(view -> {
            if(mTodoEditText.getText().toString().trim().length() <= 0) {
                Toast.makeText(MainActivity.this, "한글자 이상입력해주세요.", Toast.LENGTH_SHORT).show();
            }else{
                    vModel.insert(new Todo(mTodoEditText.getText().toString()));
//                new InsertAsyncTask(db.todoDao()).execute(new Todo(mTodoEditText.getText().toString()));
                mTodoEditText.setText("");
            }
        });

        ReadTextFile();

    }

    //경로의 텍스트 파일읽기
    public String ReadTextFile(){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream is = getResources().openRawResource(R.raw.trash);
            InputStreamReader inputreader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputreader);
            String line;
            while((line = reader.readLine()) != null){
                strBuffer.append(line+"\n");
            }
            Toast.makeText(this,"성공적으로 읽음",Toast.LENGTH_SHORT).show();
            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            return "Fail";
        }
        return strBuffer.toString();
    }

    @Override
    public void onBackPressed(){
        backPressedForFinish.onBackPressed();
    }

}
