package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReturnCompleteActivity extends AppCompatActivity {

    TextView textComplete, textUserInfo, textUsageTime, textReturnTime;
    Button btnBackToMain;

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

        // ✅ GlobalData에서 데이터 읽기
        String seatNumber = GlobalData.seatNumber != null ? GlobalData.seatNumber : "5번 자리";
        String batteryNumber = GlobalData.batteryNumber != null ? GlobalData.batteryNumber : "3번 보조배터리";
        String startTime = GlobalData.startTime != null ? GlobalData.startTime : "14:00";
        String endTime = GlobalData.endTime != null ? GlobalData.endTime : "15:30";

        // ✅ 현재 시간 = 반납 시간
        String returnTime = new SimpleDateFormat("a h:mm", Locale.KOREA).format(new Date());

        // ✅ UI 출력
        textComplete.setText("🎉 보조배터리가 반납되었습니다!");
        textUserInfo.setText("🔋 " + batteryNumber + " 반납 완료");
        textUsageTime.setText("🕒 사용 시간: " + startTime + " ~ " + endTime);
        textReturnTime.setText("📅 반납 시간: " + returnTime);

        // ✅ 메인으로 복귀
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
