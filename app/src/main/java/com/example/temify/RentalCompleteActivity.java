package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;

public class RentalCompleteActivity extends AppCompatActivity {

    TextView textUserInfo, textStartTime, textEndTime;
    Button btnBackToMain;
    private Robot robot;
    private final String returnStation = "3번 자리";  // Temi에 저장된 위치명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_complete);

        // Temi SDK 초기화
        robot = Robot.getInstance();

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
        textUserInfo.setText("🔋 " + batteryNumber + "번 보조배터리를 가져가세요!");
        textStartTime.setText("🕒 사용 시작 시간: " + startTime);
        textEndTime.setText("📅 반납 예정 시간: " + endTime);

        // ✅ 버튼 클릭 시: 충전 스테이션으로 이동 + 메인화면 전환
        btnBackToMain.setOnClickListener(v -> {
            if (robot != null && robot.getLocations().contains(returnStation)) {
                robot.goTo(returnStation);
                Toast.makeText(this, "Temi가 충전 스테이션으로 돌아갑니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Temi 위치 정보가 불안정하거나 '충전 스테이션' 위치가 없습니다.", Toast.LENGTH_LONG).show();
            }

            // 메인 화면으로 이동
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
