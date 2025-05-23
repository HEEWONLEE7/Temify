package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReturnCompleteActivity extends AppCompatActivity {

    TextView textComplete, textUserInfo, textUsageTime;
    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_complete);

        textComplete = findViewById(R.id.textComplete);
        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // 예시 데이터 (Intent로 전달 가능)
        String seatNumber = getIntent().getStringExtra("seatNumber");
        String batteryNumber = getIntent().getStringExtra("batteryNumber");
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");

        if (seatNumber == null) seatNumber = "5번 자리";
        if (batteryNumber == null) batteryNumber = "3번 보조배터리";
        if (startTime == null) startTime = "14:00";
        if (endTime == null) endTime = "15:30";

        textUserInfo.setText("📌 " + seatNumber + " - " + batteryNumber);
        textUsageTime.setText("🕒 사용 시간: " + startTime + " ~ " + endTime);

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
