package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRent, btnReturn, btnStock, btnInitUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRent = findViewById(R.id.btnRent);
        btnReturn = findViewById(R.id.btnReturn);
        btnStock = findViewById(R.id.btnStock);
        btnInitUpload = findViewById(R.id.btnInitUpload); // 🔁 서버 초기화 버튼

        btnRent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReturnActivity.class);
            startActivity(intent);
        });

        btnStock.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StockActivity.class);
            startActivity(intent);
        });

        // ✅ Firebase에 초기값 업로드 (개발용 임시 버튼)
        btnInitUpload.setOnClickListener(v -> {
            FirebaseUploader.uploadInitialData();
        });
    }
}
