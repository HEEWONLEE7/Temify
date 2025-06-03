package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReturnCompleteActivity extends AppCompatActivity {

    TextView textComplete, textUserInfo, textUsageTime, textReturnTime;
    Button btnBackToMain;
    private Robot robot;
    private final String returnStation = "충전 스테이션";  // Temi에 저장된 위치 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_complete);

        // XML 요소 연결
        textComplete = findViewById(R.id.textComplete);
        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);
        textReturnTime = findViewById(R.id.textReturnTime);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // Temi SDK 초기화
        robot = Robot.getInstance();

        // ✅ GlobalData에서 데이터 읽기
        String seatNumber = GlobalData.seatNumber != null ? GlobalData.seatNumber : "5번 자리";
        String batteryNumber = GlobalData.batteryNumber != null ? GlobalData.batteryNumber : "3번 보조배터리";
        String startTime = GlobalData.startTime != null ? GlobalData.startTime : "14:00";
        String endTime = GlobalData.endTime != null ? GlobalData.endTime : "15:30";

        // ✅ 현재 시간 = 반납 시간
        String returnTime = new SimpleDateFormat("HH:mm", Locale.KOREA).format(new Date());

        // ✅ UI 출력
        textComplete.setText("🎉 보조배터리가 반납되었습니다!");
        textUserInfo.setText("🔋 " + batteryNumber + "번 보조배터리 반납 완료");
        textUsageTime.setText("🕒 사용 시간: " + startTime + " ~ " + endTime);
        textReturnTime.setText("📅 반납 시간: " + returnTime);

        // ✅ 버튼 클릭 시: 충전 스테이션으로 이동 + 메인화면 전환
        btnBackToMain.setOnClickListener(v -> {
            if (robot != null) {
                List<String> locations = robot.getLocations();
                if (locations.contains(returnStation)) {
                    robot.goTo(returnStation);
                    Toast.makeText(this, "Temi가 충전 스테이션으로 이동합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "⚠️ '충전 스테이션' 위치가 Temi에 저장되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }

            // 메인 화면으로 이동
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
