package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExtendRentalActivity extends AppCompatActivity {

    TextView textUserInfo, textUsageTime;
    Button btn30min, btn60min, btn90min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_rental);

        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);
        btn30min = findViewById(R.id.btn30min);
        btn60min = findViewById(R.id.btn60min);
        btn90min = findViewById(R.id.btn90min);

        // 예시 정보
        String seat = GlobalData.SEAT_NUMBER;
        String battery = GlobalData.BATTERY_NUMBER;
        String usage = GlobalData.START_TIME + " ~ " + GlobalData.END_TIME;

        textUserInfo.setText("📌 " + seat + " - " + battery);
        textUsageTime.setText("🕒 현재 사용 시간: " + usage);

        // 버튼 클릭 시 바로 확정 및 화면 이동
        btn30min.setOnClickListener(v -> {
            goToSuccess("30분");
        });

        btn60min.setOnClickListener(v -> {
            goToSuccess("1시간");
        });

        btn90min.setOnClickListener(v -> {
            goToSuccess("1시간 30분");
        });
    }

    private void goToSuccess(String time) {
        Intent intent = new Intent(this, ExtendSuccessActivity.class);
        intent.putExtra("extendedTime", time);
        startActivity(intent);
    }
}
