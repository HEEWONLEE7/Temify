package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRent, btnReturn, btnStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // XML 화면 연결

        // XML 버튼과 자바 변수 연결
        btnRent = findViewById(R.id.btnRent);
        btnReturn = findViewById(R.id.btnReturn);
        btnStock = findViewById(R.id.btnStock);

        // 대여 버튼 → LoginActivity로 이동
        btnRent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExtendRentalActivity.LoginActivity.class);
            startActivity(intent);
        });

        // 반납 버튼 → ReturnActivity로 이동
        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExtendSuccessActivity.ReturnActivity.class);
            startActivity(intent);
        });

        // 재고 확인 버튼 → Toast 메시지만 표시
        btnStock.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GlobalData.StockActivity.class);
            startActivity(intent);
        });

    }
}
