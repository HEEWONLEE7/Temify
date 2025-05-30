package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        btn30min.setOnClickListener(v -> goToSuccess("30분", 30));
        btn60min.setOnClickListener(v -> goToSuccess("1시간", 60));
        btn90min.setOnClickListener(v -> goToSuccess("1시간 30분", 90));
    }

    private void goToSuccess(String timeLabel, int minutesToAdd) {
        String startTime = GlobalData.START_TIME;
        String endTime = GlobalData.END_TIME;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String newEndTime = endTime;

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(endTime)); // 기존 종료 시간
            cal.add(Calendar.MINUTE, minutesToAdd); // 연장
            newEndTime = sdf.format(cal.getTime()); // 새 종료 시간
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, ExtendSuccessActivity.class);
        intent.putExtra("extendedTime", timeLabel);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", newEndTime);
        startActivity(intent);
    }
}
