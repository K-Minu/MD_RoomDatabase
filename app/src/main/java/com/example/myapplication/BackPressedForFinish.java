package com.example.myapplication;

import android.app.Activity;
import android.widget.Toast;

public class BackPressedForFinish {

    private long backKeyPressedTime = 0;    // Last Back button click time save
    private Toast toast;                    // first back button click press
    private Activity activity;              // 종료할 액티비티

    public BackPressedForFinish(Activity _activity){
        this.activity = _activity;
    }

    // 종료할 액티비티에서 호출할 함수
    public void onBackPressed() {
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Re Show
        // 2000 milliseconds = 2 sec
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }
}