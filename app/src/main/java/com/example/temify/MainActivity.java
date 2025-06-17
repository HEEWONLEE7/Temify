package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btnRent, btnReturn, btnStock, btnInitUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Temi는 현재 충전 중 상태라고 간주하고 charging=1 업로드
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("callRequests");

        // ✅ action 값이 1이면 MovingToUserActivity로 이동
        ref.child("action").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Long actionValue = snapshot.getValue(Long.class);
                    if (actionValue != null && actionValue == 1) {
                        // ✅ 액티비티 전환
                        Intent intent = new Intent(MainActivity.this, MovingToUserActivity.class);
                        startActivity(intent);

                        // ✅ action 값을 0으로 초기화 (중복 실행 방지)
                        ref.child("action").setValue(0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // 오류 처리 가능 (예: 로그 출력)
            }
        });

        // ✅ 버튼 연결
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
