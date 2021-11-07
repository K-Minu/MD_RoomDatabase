package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Message;
import android.util.Log;
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

    private TextView mResultTextView;
    private BackPressedForFinish backPressedForFinish;
    private MainViewModel vModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BackPressedForFinish 객체 생성
        backPressedForFinish = new BackPressedForFinish(this);

        mResultTextView = findViewById(R.id.result_text);
        
        // ViewModel Test... 11/1일
        // 11/3일 드디어 해결..
        vModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);

//        vModel.test(this);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        vModel.getAll().observe(this, new Observer<List<KTodo>>() {
            @Override
            public void onChanged(List<KTodo> ktodos) {
                mResultTextView.setText(ktodos.toString());
            }
        });

        //DB 데이터 불러오기 (SELECT)
        mResultTextView.setText(vModel.getAll().toString());

        // txt File에 있는 내용을 Database에 입력 최초 1회만 실행 되도록...
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            ReadTextFile();
        }else{
            Log.d("Is first Time?", "not first");
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

            String line;
            while((line = reader.readLine()) != null){
                st = new StringTokenizer(line,":");
                String st1 = st.nextToken();
                String st2 = st.nextToken();
                vModel.insert(new KTodo(st2,st1));
                strBuffer.append(line+"\n");
            }
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
