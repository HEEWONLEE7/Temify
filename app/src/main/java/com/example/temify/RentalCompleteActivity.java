package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RentalCompleteActivity extends AppCompatActivity {

    TextView textComplete, textUserInfo, textStartTime, textEndTime;
    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_complete);

        // XML 요소 연결
        textUserInfo = findViewById(R.id.textBatteryInfo); // ✅ 수정됨
        textStartTime = findViewById(R.id.textUsageTime);
        textEndTime = findViewById(R.id.textReturnTime);
        btnBackToMain = findViewById(R.id.btnGoHome); // ✅ XML에 맞게 버튼 ID도 확인
        // textComplete는 현재 XML에 없으므로 주석 처리 또는 제거
        // textComplete = findViewById(R.id.textComplete);

        // 전달받은 데이터
        String batteryNumber = getIntent().getStringExtra("batteryNumber");
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");

        if (batteryNumber == null) batteryNumber = "3번 보조배터리";
        if (startTime == null) startTime = "14:00";
        if (endTime == null) endTime = "15:30";

        textUserInfo.setText("🔋 " + batteryNumber + "를 가져가세요!");
        textStartTime.setText("🕒 사용 시작 시간: " + startTime);
        textEndTime.setText("📅 반납 예정 시간: " + endTime);

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
