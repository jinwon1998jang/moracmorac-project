package com.example.moracmoracsignintest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class MapsEditActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    static final String TAG = MapsEditActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private GoogleMap mMap;
    private Marker currentMarker;
    private String currentUserId; // 현재 사용자의 Firebase 아이디

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        // Check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission is granted
            mMap.setMyLocationEnabled(true);

            // Move to current location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission is denied
                Toast.makeText(this, "위치 권한이 거부되어 현재 위치를 표시할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

// ...


    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsEditActivity.this);
        dialogBuilder.setTitle("푸드트럭 등록");

        LinearLayout layout = new LinearLayout(MapsEditActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameEditText = new EditText(MapsEditActivity.this);
        nameEditText.setHint("푸드트럭명");
        layout.addView(nameEditText);

        final EditText contentEditText = new EditText(MapsEditActivity.this);
        contentEditText.setHint("푸드트럭 설명");
        layout.addView(contentEditText);

        final EditText mondayEditText = new EditText(MapsEditActivity.this);
        mondayEditText.setHint("월요일 영업 시간");
        layout.addView(mondayEditText);

        final EditText tuesdayEditText = new EditText(MapsEditActivity.this);
        tuesdayEditText.setHint("화요일 영업 시간");
        layout.addView(tuesdayEditText);

        final EditText wendnesdayEditText = new EditText(MapsEditActivity.this);
        wendnesdayEditText.setHint("수요일 영업 시간");
        layout.addView(wendnesdayEditText);

        final EditText thursdayEditText = new EditText(MapsEditActivity.this);
        thursdayEditText.setHint("목요일 영업 시간");
        layout.addView(thursdayEditText);

        final EditText fridayEditText = new EditText(MapsEditActivity.this);
        fridayEditText.setHint("금요일 영업 시간");
        layout.addView(fridayEditText);

        final EditText saturdayEditText = new EditText(MapsEditActivity.this);
        saturdayEditText.setHint("토요일 영업 시간");
        layout.addView(saturdayEditText);

        final EditText sundayEditText = new EditText(MapsEditActivity.this);
        sundayEditText.setHint("일요일 영업 시간");
        layout.addView(sundayEditText);

        // 추가적인 요일 EditText 필드 추가

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String mondayHours = mondayEditText.getText().toString();
                String tuesdayHours = tuesdayEditText.getText().toString();
                String wendnesdayHours = wendnesdayEditText.getText().toString();
                String thursdayHours = thursdayEditText.getText().toString();
                String fridayHours = fridayEditText.getText().toString();
                String saturdayHours = saturdayEditText.getText().toString();
                String sundayHours = sundayEditText.getText().toString();
                // 추가적인 요일 영업 시간 값 가져오기

                HashMap<String, String> openingHours = new HashMap<>();
                openingHours.put("월요일", mondayHours);
                openingHours.put("화요일", tuesdayHours);
                openingHours.put("수요일", wendnesdayHours);
                openingHours.put("목요일", thursdayHours);
                openingHours.put("금요일", fridayHours);
                openingHours.put("토요일", saturdayHours);
                openingHours.put("일요일", sundayHours);

                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));

                marker.setTitle(name);
                marker.setSnippet(content);

                // Extract additional data from the marker as needed

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userEmail = user.getEmail();
                    String markerKey = userEmail.replace(".", "_");

                    MarkerData markerData = new MarkerData(markerKey, name, content, openingHours, latLng.latitude, latLng.longitude);

                    // Calculate the distance between the clicked location and the current location
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ContextCompat.checkSelfPermission(MapsEditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MapsEditActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (lastKnownLocation != null) {
                        float[] distance = new float[1];
                        Location.distanceBetween(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                                markerData.getLatitude(), markerData.getLongitude(), distance);

                        // Check if the distance is within 5km (5000 meters)
                        if (distance[0] <= 5000) {
                            // Location is within the allowed range, proceed with marker creation

                            // Save the MarkerData to Firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("markers");
                            databaseReference.child(markerData.getId()).setValue(markerData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MapsEditActivity.this, "마커가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MapsEditActivity.this, "마커 저장 중 오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Location is outside the allowed range, show a notification to the user
                            Toast.makeText(MapsEditActivity.this, "5km 이내에서만 마커를 생성할 수 있습니다.\n", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MapsEditActivity.this, MapsEditActivity.class));
                        }
                    } else {
                        // Current location not available, show a notification to the user
                        Toast.makeText(MapsEditActivity.this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapsEditActivity.this, "사용자가 로그인되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.show();
    }

    private void showEditDialog(final MarkerData markerData) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsEditActivity.this);
        dialogBuilder.setTitle("마커 수정");

        LinearLayout layout = new LinearLayout(MapsEditActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameEditText = new EditText(MapsEditActivity.this);
        nameEditText.setHint("푸드트럭명");
        layout.addView(nameEditText);

        final EditText contentEditText = new EditText(MapsEditActivity.this);
        contentEditText.setHint("푸드트럭 설명");
        layout.addView(contentEditText);

        final EditText openingHoursEditText = new EditText(MapsEditActivity.this);
        openingHoursEditText.setHint("영업 시간");
        layout.addView(openingHoursEditText);

        dialogBuilder.setView(layout);

//    dialogBuilder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            String name = nameEditText.getText().toString();
//            String content = contentEditText.getText().toString();
//            String openingHours = openingHoursEditText.getText().toString();
//
//            // Update marker data
//            markerData.setTitle(name);
//            markerData.setContent(content);
//            markerData.getOpeningHours().put("default", openingHours);
//
//            // Save the updated marker data to Firebase Realtime Database
//
//        }
//    });

        dialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.show();
    }
    private String getEmailPrefix(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        }
        return email;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_edit);

        // Get current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize Firebase SDK
        FirebaseApp.initializeApp(this);

        // Create and register SupportMapFragment to display the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create DatabaseReference object to access Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("markers");

        // Register ValueEventListener to retrieve marker information from Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear(); // Clear all existing markers


                // Implement OnMarkerClickListener to handle marker click events
                mMap.setOnMarkerClickListener(marker -> {
                    String title = marker.getTitle();
                    String snippet = marker.getSnippet();

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsEditActivity.this);
                    dialogBuilder.setTitle(title);

                    // Get user ID
                    String markerUserId = (String) marker.getTag();

                    if (markerUserId != null && markerUserId.equals(currentUserId)) {
                        // Show additional information if the marker belongs to the current user
                        snippet += "\n등록자: " + markerUserId;
                    }

                    MarkerData markerData = (MarkerData) marker.getTag();
                    if (markerData != null) {
                        HashMap<String, String> openingHours = markerData.getOpeningHours();
                        if (openingHours != null && !openingHours.isEmpty()) {
                            StringBuilder openingHoursText = new StringBuilder();
                            for (String day : openingHours.keySet()) {
                                String hours = openingHours.get(day);
                                openingHoursText.append(day).append(": ").append(hours).append("\n");
                            }
                            snippet += "\n\n영업 시간:\n" + openingHoursText.toString();
                        }
                    }

                    dialogBuilder.setMessage(snippet);
                    dialogBuilder.setPositiveButton("확인", null);
//                    dialogBuilder.setNeutralButton("수정", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            showEditDialog(markerData);
//                        }
//                    });
                    // Add delete button
                    // When the delete button is clicked, remove the marker from Firebase Realtime Database
                    dialogBuilder.setNegativeButton("삭제", (dialog, which) -> {
                        // 데이터베이스에서 마커를 찾아 제거
                        String markerTitle = marker.getTitle();
                        Query markerQuery = databaseReference.orderByChild("title").equalTo(markerTitle);

                        markerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // 마커가 있는 경우 제거
                                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                                    MarkerData markerData = markerSnapshot.getValue(MarkerData.class);
                                    if (markerData != null) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null && getEmailPrefix(user.getEmail()).equals(getEmailPrefix(markerData.getId()))) {
                                            markerSnapshot.getRef().removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // 마커 제거 성공
                                                            Log.d(TAG, "마커가 제거되었습니다");
                                                            marker.remove(); // 지도에서도 마커 제거
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // 마커 제거 실패
                                                            Log.e(TAG, "마커 제거에 실패했습니다: " + e.getMessage());
                                                        }
                                                    });
                                        } else {
                                            // 사용자가 마커를 삭제할 권한이 없음
                                            Toast.makeText(MapsEditActivity.this, "마커를 삭제할 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                                            String markerUserId = markerSnapshot.child("userId").getValue(String.class);
                                            Log.d(TAG, "Marker User ID: " + getEmailPrefix(markerData.getId()));
                                            Log.d(TAG, "Current User Email: " + user.getEmail());
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e(TAG, "Firebase Realtime Database에서 마커 데이터를 읽는 데 실패했습니다: " + databaseError.getMessage());
                            }
                        });
                    });


                    dialogBuilder.show();

                    return false; // Return false to maintain the default behavior of displaying only marker information
                });

                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                    Double latitude = markerSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = markerSnapshot.child("longitude").getValue(Double.class);
                    String title = markerSnapshot.child("title").getValue(String.class);
                    String content = markerSnapshot.child("content").getValue(String.class);
                    HashMap<String, String> openingHours = markerSnapshot.child("openingHours").getValue(new GenericTypeIndicator<HashMap<String, String>>() {});

                    String markerUserId = markerSnapshot.child("userId").getValue(String.class);

                    if (latitude != null && longitude != null) {
                        LatLng location = new LatLng(latitude, longitude);

                        StringBuilder openingHoursText = new StringBuilder();
                        String[] daysOfWeek = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
                        if (openingHours != null && !openingHours.isEmpty()) {
                            for (String day : daysOfWeek) {
                                if (openingHours.containsKey(day)) {
                                    String hours = openingHours.get(day);
                                    openingHoursText.append(day).append(": ").append(hours).append("\n");
                                }
                            }
                        }

                        String snippet = content;
                        if (openingHoursText.length() > 0) {
                            snippet += "\n\n영업 시간:\n" + openingHoursText.toString();
                        }

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(title)
                                .snippet(snippet));

                        // Set the user ID as the tag for the marker
                        marker.setTag(markerUserId);
                    } else {
                        Log.e(TAG, "Latitude 또는 longitude가 null입니다.");
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Firebase Realtime Database에서 마커 데이터를 읽는 데 실패했습니다: " + databaseError.getMessage());
            }
        });
    }
}