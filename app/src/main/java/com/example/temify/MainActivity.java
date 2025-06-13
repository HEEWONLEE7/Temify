package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnRent, btnReturn, btnStock, btnInitUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Temi는 현재 충전 중 상태라고 간주하고 charging=1 업로드
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");
        ref.child("charging").setValue(1);

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
