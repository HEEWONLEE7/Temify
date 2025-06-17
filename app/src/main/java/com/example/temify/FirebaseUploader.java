package com.example.temify;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FirebaseUploader {

    public static void uploadInitialData() {
        DatabaseReference callRef = FirebaseDatabase.getInstance().getReference("callRequests");
        DatabaseReference resRef = FirebaseDatabase.getInstance().getReference("reservation");

        // 🔹 callRequests/time 가져오기
        callRef.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String baseTime = snapshot.getValue(String.class);  // ex: "13:40"

                if (baseTime != null && !baseTime.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(baseTime));  // 📌 callRequests/time 기준 시간

                        String start = sdf.format(cal.getTime()); // 시작 시간
                        cal.add(Calendar.HOUR_OF_DAY, 2);         // 2시간 후
                        String end = sdf.format(cal.getTime());   // 종료 시간

                        // 🔹 reservation 경로에 업로드
                        resRef.child("battery").setValue("3");
                        resRef.child("start_time").setValue(start);
                        resRef.child("end_time").setValue(end);
                        resRef.child("password").setValue("1111");
                        resRef.child("open").setValue(false);
                        resRef.child("rentalStatus").setValue(0);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // 오류 처리 필요 시 여기에 작성
            }
        });
    }
}
