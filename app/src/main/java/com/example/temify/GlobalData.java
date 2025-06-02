package com.example.temify;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GlobalData {
    public static String seatNumber = null;
    public static String batteryNumber = null;
    public static String startTime = null;
    public static String endTime = null;
    public static String userId = null;
    public static String extendedTime = null;
    public static String password = "1234"; // 기본값

    public interface Callback {
        void onComplete(boolean success);
    }

    public static void autoAssignBattery(Callback callback) {
        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("status");

        statusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean assigned = false;

                for (DataSnapshot batt : snapshot.getChildren()) {
                    String isBattery = batt.child("isBattery").getValue(String.class);
                    Double level = batt.child("BatLevel1").getValue(Double.class);

                    if ("True".equalsIgnoreCase(isBattery) && level != null && level >= 101500.0) {
                        batteryNumber = batt.getKey(); // 예: Batt2
                        seatNumber = "자동 배정석";

                        // 현재 시간 + 2시간
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        startTime = sdf.format(cal.getTime());
                        cal.add(Calendar.HOUR_OF_DAY, 2);
                        endTime = sdf.format(cal.getTime());

                        Log.d("GlobalData", "배터리 자동 할당 완료: " + batteryNumber);

                        uploadReservationToFirebase();
                        assigned = true;
                        break;
                    }
                }

                callback.onComplete(assigned);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("GlobalData", "DB 에러: " + error.getMessage());
                callback.onComplete(false);
            }
        });
    }

    // ✅ 조건 만족한 후 reservation에 정보 저장
    public static void uploadReservationToFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");

        ref.child("battery").setValue(batteryNumber);
        ref.child("seat").setValue(seatNumber);
        ref.child("start_time").setValue(startTime);
        ref.child("end_time").setValue(endTime);
        ref.child("password").setValue(password);
    }

}
