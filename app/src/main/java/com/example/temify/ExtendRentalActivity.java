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

        // ✅ 동적으로 GlobalData 값 사용
        String seat = GlobalData.seatNumber;
        String battery = GlobalData.batteryNumber;
        String usage = GlobalData.startTime + " ~ " + GlobalData.endTime;

        textUserInfo.setText("📌 " + seat + "번 자리 - " + battery + "번 보조배터리");
        textUsageTime.setText("🕒 현재 사용 시간: " + usage);

        btn30min.setOnClickListener(v -> extendTime("30분", 30));
        btn60min.setOnClickListener(v -> extendTime("1시간", 60));
        btn90min.setOnClickListener(v -> extendTime("1시간 30분", 90));
    }

    private void extendTime(String timeLabel, int minutesToAdd) {
        String startTime = GlobalData.startTime;
        String endTime = GlobalData.endTime;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String newEndTime = endTime;

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(endTime)); // 기존 종료 시간 기준
            cal.add(Calendar.MINUTE, minutesToAdd); // 연장 시간 추가
            newEndTime = sdf.format(cal.getTime()); // 새로운 종료 시간 포맷팅
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ✅ GlobalData의 종료 시간을 갱신
        GlobalData.endTime = newEndTime;

        // ✅ 서버에 변경된 값 업로드
        GlobalData.uploadReservationToFirebase();

        // ✅ 결과 화면으로 이동
        Intent intent = new Intent(this, ExtendSuccessActivity.class);
        intent.putExtra("extendedTime", timeLabel);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", newEndTime);
        startActivity(intent);
    }
}
