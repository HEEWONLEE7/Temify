package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;

import java.util.List;

public class ExtendSuccessActivity extends AppCompatActivity {

    TextView textSuccess, textUsageInfo;
    Button btnBackToMain;

    private Robot robot;
    private final String returnStation = "홈베이스";  // Temi에 저장된 위치 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_success);

        textSuccess = findViewById(R.id.textSuccess);
        textUsageInfo = findViewById(R.id.textUsageInfo);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        robot = Robot.getInstance();

        // ✅ Intent로부터 전달받은 연장 정보 사용
        Intent intent = getIntent();
        String extendedTime = intent.getStringExtra("extendedTime");
        String originalStart = intent.getStringExtra("startTime");
        String newEnd = intent.getStringExtra("endTime");

        // ✅ null 체크
        if (extendedTime == null) extendedTime = "알 수 없음";
        if (originalStart == null) originalStart = "??:??";
        if (newEnd == null) newEnd = "??:??";

        // ✅ UI 출력
        textSuccess.setText("✅ 대여가 " + extendedTime + " 연장되었습니다!");
        textUsageInfo.setText("🕒 사용 시간: " + originalStart + " ~ " + newEnd +
                "\n📈 최종 반납 시간: " + newEnd);

        btnBackToMain.setOnClickListener(v -> {
            // ✅ Temi가 홈베이스로 이동
            List<String> locations = robot.getLocations();
            if (locations.contains(returnStation)) {
                robot.goTo(returnStation);
                Toast.makeText(this, "Temi가 홈베이스로 이동합니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "⚠️ Temi에 '홈베이스' 위치가 저장되어 있지 않습니다.", Toast.LENGTH_LONG).show();
            }

            // ✅ 메인 화면으로 이동
            Intent backIntent = new Intent(this, MainActivity.class);
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backIntent);
        });
    }
}
