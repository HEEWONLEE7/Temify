package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MovingToUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_to_user);

        // ⭐ Temi가 도착했다고 가정하고 3초 후 자동 이동 (실제론 Temi SDK 사용)
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MovingToUserActivity.this, UserAuthActivity.class);
            intent.putExtra("seatNumber", "5번 자리"); // 자리 정보 전달 예시
            startActivity(intent);
            finish(); // 뒤로가기 막기
        }, 3000); // 3000ms = 3초
    }
}