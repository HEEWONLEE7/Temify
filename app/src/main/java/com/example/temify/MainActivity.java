package com.example.temify;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRent, btnReturn, btnStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // XML 화면을 연결하는 부분

        // XML의 버튼과 연결
        btnRent = findViewById(R.id.btnRent);
        btnReturn = findViewById(R.id.btnReturn);
        btnStock = findViewById(R.id.btnStock);

        // 버튼 클릭 시 메시지 표시
        btnRent.setOnClickListener(v ->
                Toast.makeText(this, "📦 대여 요청됨!", Toast.LENGTH_SHORT).show());

        btnReturn.setOnClickListener(v ->
                Toast.makeText(this, "↩️ 반납 요청됨!", Toast.LENGTH_SHORT).show());

        btnStock.setOnClickListener(v ->
                Toast.makeText(this, "📊 재고 확인 중!", Toast.LENGTH_SHORT).show());
    }
}