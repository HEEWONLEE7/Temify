package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReturnPromptActivity extends AppCompatActivity {

    Button btnReturn, btnExtend;
    TextView textUserInfo, textUsageTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_prompt);

        btnReturn = findViewById(R.id.btnReturn);
        btnExtend = findViewById(R.id.btnExtend);
        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);

        // 예시 데이터 (Intent로 받아올 수도 있음)
        String seatNumber = "5번 자리";
        String batteryNumber = "3번 보조배터리";
        String usageTime = "14:00 ~ 15:30";

        textUserInfo.setText("📌 " + seatNumber + " - " + batteryNumber);
        textUsageTime.setText("🕒 사용 시간: " + usageTime);

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReturnCompleteActivity.class);
            startActivity(intent);
        });

        btnExtend.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExtendRentalActivity.class);
            startActivity(intent);
        });
    }
}
