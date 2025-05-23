package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExtendSuccessActivity extends AppCompatActivity {

    TextView textSuccess, textUsageInfo;
    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_success);

        textSuccess = findViewById(R.id.textSuccess);
        textUsageInfo = findViewById(R.id.textUsageInfo);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // 전달받은 정보
        String extendedTime = getIntent().getStringExtra("extendedTime"); // 예: "30분"
        String originalStart = getIntent().getStringExtra("startTime");   // 예: "14:00"
        String originalEnd = getIntent().getStringExtra("endTime");       // 예: "15:30"

        if (extendedTime == null) extendedTime = "알 수 없음";
        if (originalStart == null) originalStart = "??:??";
        if (originalEnd == null) originalEnd = "??:??";

        // 텍스트 구성
        textSuccess.setText("✅ 대여가 " + extendedTime + " 연장되었습니다!");
        textUsageInfo.setText("🕒 사용 시간: " + originalStart + " ~ " + originalEnd +
                "\n📈 최종 반납 시간까지 연장됨");

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    public static class ReturnActivity extends AppCompatActivity {

        EditText batteryNumberInput;
        Button btnSubmitReturn;
        Button btnBackToMain;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_return);

            // XML과 연결
            batteryNumberInput = findViewById(R.id.batteryNumberInput);
            btnSubmitReturn = findViewById(R.id.btnSubmitReturn);
            btnBackToMain = findViewById(R.id.btnBackToMain);

            // 반납 버튼 클릭 처리
            btnSubmitReturn.setOnClickListener(v -> {
                String number = batteryNumberInput.getText().toString().trim();
                if (number.isEmpty()) {
                    Toast.makeText(this, "보조배터리 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "🔋 보조배터리 " + number + "번 반납됨!", Toast.LENGTH_SHORT).show();
                    finish(); // 또는 메인으로 이동해도 됨
                }
            });

            // 메인화면으로 돌아가기
            btnBackToMain.setOnClickListener(v -> {
                Intent intent = new Intent(ReturnActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }
    }
}
