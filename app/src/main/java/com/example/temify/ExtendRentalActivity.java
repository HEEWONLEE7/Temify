package com.example.temify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExtendRentalActivity extends AppCompatActivity {

    TextView textUserInfo, textUsageTime;
    Button btn30min, btn60min, btn90min;

    private String seat = "";
    private String battery = "";
    private String startTime = "";
    private String endTime = "";

    private DatabaseReference reservationRef;
    private DatabaseReference callRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_rental);

        textUserInfo = findViewById(R.id.textUserInfo);
        textUsageTime = findViewById(R.id.textUsageTime);
        btn30min = findViewById(R.id.btn30min);
        btn60min = findViewById(R.id.btn60min);
        btn90min = findViewById(R.id.btn90min);

        reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
        callRequestsRef = FirebaseDatabase.getInstance().getReference("callRequests");

        fetchReservationInfo();  // Firebase에서 데이터 불러오기

        btn30min.setOnClickListener(v -> extendTime("30분", 30));
        btn60min.setOnClickListener(v -> extendTime("1시간", 60));
        btn90min.setOnClickListener(v -> extendTime("1시간 30분", 90));
    }

    private void fetchReservationInfo() {
        // 🔹 callRequests/number → seat
        callRequestsRef.child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                seat = snapshot.getValue(String.class);

                // 🔹 동시에 reservation 값도 가져오기
                reservationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            battery = snapshot.child("battery").getValue(String.class);
                            startTime = snapshot.child("start_time").getValue(String.class);
                            endTime = snapshot.child("end_time").getValue(String.class);

                            textUserInfo.setText("📌 " + seat + "번 자리 - " + battery + "번 보조배터리");
                            textUsageTime.setText("🕒 현재 사용 시간: " + startTime + " ~ " + endTime);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        textUserInfo.setText("❌ 예약 정보 불러오기 실패");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textUserInfo.setText("❌ 좌석 정보 불러오기 실패");
            }
        });
    }

    private void extendTime(String timeLabel, int minutesToAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String newEndTime = endTime;

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(endTime));
            cal.add(Calendar.MINUTE, minutesToAdd);
            newEndTime = sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ✅ 서버에 종료 시간 반영
        reservationRef.child("end_time").setValue(newEndTime);

        // ✅ 결과 화면으로 이동
        Intent intent = new Intent(this, ExtendSuccessActivity.class);
        intent.putExtra("extendedTime", timeLabel);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", newEndTime);
        startActivity(intent);
    }
}
