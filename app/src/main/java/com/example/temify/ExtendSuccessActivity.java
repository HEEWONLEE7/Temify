package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

        // ✅ GlobalData로부터 값 가져오기
        String extendedTime = GlobalData.extendedTime;
        String originalStart = GlobalData.startTime;
        String newEnd = GlobalData.endTime;

        if (extendedTime == null) extendedTime = "알 수 없음";
        if (originalStart == null) originalStart = "??:??";
        if (newEnd == null) newEnd = "??:??";

        textSuccess.setText("✅ 대여가 " + extendedTime + " 연장되었습니다!");
        textUsageInfo.setText("🕒 사용 시간: " + originalStart + " ~ " + newEnd +
                "\n📈 최종 반납 시간: " + newEnd);

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
