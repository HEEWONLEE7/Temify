package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RentalCompleteActivity extends AppCompatActivity {

    TextView textComplete, textReturnTime;
    Button btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_complete);

        textComplete = findViewById(R.id.textComplete);
        textReturnTime = findViewById(R.id.textReturnTime);
        btnGoHome = findViewById(R.id.btnGoHome);

        // 기본 반납 시각: 17:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);

        // 전달받은 연장 시간 (분 단위) 적용
        int extendedMinutes = getIntent().getIntExtra("extendedMinutes", 0);
        calendar.add(Calendar.MINUTE, extendedMinutes);

        // 시간 포맷 지정
        SimpleDateFormat sdf = new SimpleDateFormat("a h:mm", Locale.KOREA);
        String returnTime = sdf.format(calendar.getTime());

        // 화면에 출력
        textReturnTime.setText("반납 예정 시간: " + returnTime);

        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
