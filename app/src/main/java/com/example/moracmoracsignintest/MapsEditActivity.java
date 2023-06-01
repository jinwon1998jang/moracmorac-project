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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MapsEditActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final String TAG = MapsEditActivity.class.getSimpleName();
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

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        // Dialog or input fields to get name and content from the user
        // Using AlertDialog as an example to get name and content input
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsEditActivity.this);
        dialogBuilder.setTitle("푸드트럭 등록");

        // Add views to input name and content from the user
        LinearLayout layout = new LinearLayout(MapsEditActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameEditText = new EditText(MapsEditActivity.this);
        nameEditText.setHint("푸드트럭명");
        layout.addView(nameEditText);

        final EditText contentEditText = new EditText(MapsEditActivity.this);
        contentEditText.setHint("푸드트럭 설명");
        layout.addView(contentEditText);

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                String content = contentEditText.getText().toString();

                // Add marker at clicked location with name and content
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name)
                        .snippet(content));

                // Save marker information to Realtime Database with the current user's ID
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("markers");
                DatabaseReference newMarkerRef = databaseReference.push();
                newMarkerRef.child("latitude").setValue(latLng.latitude);
                newMarkerRef.child("longitude").setValue(latLng.longitude);
                newMarkerRef.child("title").setValue(name);
                newMarkerRef.child("content").setValue(content);
                newMarkerRef.child("userId").setValue(currentUserId); // 현재 사용자의 Firebase 아이디 저장

                Toast.makeText(MapsEditActivity.this, "푸드트럭 등록", Toast.LENGTH_SHORT).show();

                // Move camera to clicked location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            }
        });

        dialogBuilder.setNegativeButton("취소", null);
        dialogBuilder.show();
    }

// ...

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

                    dialogBuilder.setMessage(snippet);
                    dialogBuilder.setPositiveButton("확인", null);

                    // Add delete button
                    // When the delete button is clicked, remove the marker from Firebase Realtime Database
                    dialogBuilder.setNegativeButton("삭제", (dialog, which) -> {
                        // Find the marker in the database and remove it
                        String markerTitle = marker.getTitle();
                        Query markerQuery = databaseReference.orderByChild("title").equalTo(markerTitle);

                        markerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Remove the marker if found
                                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                                    markerSnapshot.getRef().removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Marker removed successfully
                                                    Log.d(TAG, "마커가 제거되었습니다");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to remove marker
                                                    Log.e(TAG, "마커 제거에 실패했습니다: " + e.getMessage());
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e(TAG, "Firebase Realtime Database에서 마커 데이터를 읽는 데 실패했습니다: " + databaseError.getMessage());
                            }
                        });

                        // Remove the marker from the map
                        marker.remove();
                    });

                    dialogBuilder.show();

                    return false; // Return false to maintain the default behavior of displaying only marker information
                });

                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                    Double latitude = markerSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = markerSnapshot.child("longitude").getValue(Double.class);
                    String title = markerSnapshot.child("title").getValue(String.class);
                    String content = markerSnapshot.child("content").getValue(String.class);
                    String markerUserId = markerSnapshot.child("userId").getValue(String.class);

                    if (latitude != null && longitude != null) {
                        LatLng location = new LatLng(latitude, longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(title)
                                .snippet(content));

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
