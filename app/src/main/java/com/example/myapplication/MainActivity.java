package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.lifecycle.Observer;
import androidx.room.Room;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private EditText mTodoEditText;
    private TextView mResultTextView;
    private BackPressedForFinish backPressedForFinish;
    private MainViewModel vModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BackPressedForFinish 객체 생성
        backPressedForFinish = new BackPressedForFinish(this);

        mTodoEditText = findViewById(R.id.textedit);
        mResultTextView = findViewById(R.id.result_text);


        // ViewModel Test... 11/1일
        // 11/3일 드디어 해결..
        vModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);

        vModel.test(this);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        vModel.getAll().observe(this, todos -> mResultTextView.setText(todos.toString()));

        //DB 데이터 불러오기 (SELECT)
        mResultTextView.setText(vModel.getAll().toString());

        //추가버튼시 DB에 데이터 INSERT
//        findViewById(R.id.add_button).setOnClickListener(view -> {
//            if (mTodoEditText.getText().toString().trim().length() <= 0) {
//                Toast.makeText(MainActivity.this, "한글자 이상입력해주세요.", Toast.LENGTH_SHORT).show();
//            } else {
//                vModel.insert(new KTodo(mTodoEditText.getText().toString()));
//                mTodoEditText.setText("");
//            }
//        });

        // Data Delete All
        findViewById(R.id.del_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vModel.delete(6);
            }
        });


        // txt File에 있는 내용을 Database에 입력 최초 1회만 실행 되도록...
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
//            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            ReadTextFile();
        }else{
//            Log.d("Is first Time?", "not first");
        }
    }

    //경로의 텍스트 파일읽기
    public String ReadTextFile(){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream is = getResources().openRawResource(R.raw.trash);
            InputStreamReader inputreader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputreader);

            StringTokenizer st;
            String[] arry_Massage;

            String line;
            while((line = reader.readLine()) != null){
                st = new StringTokenizer(line,":");
//                arry_Massage = new String[st.countTokens()];
                vModel.insert(new KTodo(st.nextToken(), st.nextToken()));
                strBuffer.append(line+"\n");
            }
//            Toast.makeText(this,"성공적으로 읽음",Toast.LENGTH_SHORT).show();
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
