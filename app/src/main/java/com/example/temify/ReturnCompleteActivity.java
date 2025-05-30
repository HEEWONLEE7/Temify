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
        textReturnTime = findViewById(R.id.textReturnTime);  // ← 새로 추가된 텍스트뷰
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // 전달받은 데이터
        String seatNumber = getIntent().getStringExtra("seatNumber");
        String batteryNumber = getIntent().getStringExtra("batteryNumber");
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");

        // 기본값 설정
        if (seatNumber == null) seatNumber = "5번 자리";
        if (batteryNumber == null) batteryNumber = "3번 보조배터리";
        if (startTime == null) startTime = "14:00";
        if (endTime == null) endTime = "15:30";

        // 현재 시간을 반납 완료 시각으로 사용
        String returnTime = new SimpleDateFormat("a h:mm", Locale.KOREA).format(new Date());

        // 화면 출력
        textComplete.setText("🎉 보조배터리가 반납되었습니다!");
        textUserInfo.setText("🔋 " + batteryNumber + " 반납 완료");
        textUsageTime.setText("🕒 사용 시간: " + startTime + " ~ " + endTime);
        textReturnTime.setText("📅 반납 시간: " + returnTime);

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
