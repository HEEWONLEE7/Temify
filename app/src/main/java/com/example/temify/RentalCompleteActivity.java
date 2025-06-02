package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RentalCompleteActivity extends AppCompatActivity {

    TextView textUserInfo, textStartTime, textEndTime;
    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_complete);

        // XML 요소 연결
        textUserInfo = findViewById(R.id.textBatteryInfo);
        textStartTime = findViewById(R.id.textUsageTime);
        textEndTime = findViewById(R.id.textReturnTime);
        btnBackToMain = findViewById(R.id.btnGoHome);

        // ✅ GlobalData에서 값 읽어오기
        String batteryNumber = GlobalData.batteryNumber != null ? GlobalData.batteryNumber : "3번 보조배터리";
        String startTime = GlobalData.startTime != null ? GlobalData.startTime : "14:00";
        String endTime = GlobalData.endTime != null ? GlobalData.endTime : "15:30";

        // ✅ 화면 출력
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
