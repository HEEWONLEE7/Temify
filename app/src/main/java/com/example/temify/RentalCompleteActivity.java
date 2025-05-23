package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        // 예시로 반납 예정 시간 하드코딩 (실제로는 intent로 전달)
        String returnTime = "오후 3:30";
        textReturnTime.setText("반납 예정 시간: " + returnTime);

        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
