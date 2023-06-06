package com.example.moracmoracsignintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StoreReviewListActivity extends AppCompatActivity {

    private ListView reviewListView;
    private Button writeReviewButton;
    private Button favoriteButton;
    private Button goToFavoritesButton; // 추가된 버튼 참조

    private FirebaseFirestore firestore;
    private StoreReviewAdapter reviewAdapter;
    private boolean isFavorite = false;
    private FirebaseUser currentUser;

    private static final String PREF_IS_FAVORITE_KEY = "is_favorite";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean isButtonEnabled = false; // 찜하기 버튼 활성화/비활성화를 위한 플래그

    private DatabaseReference favoritesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_review_list);

        reviewListView = findViewById(R.id.review_list_view);
        writeReviewButton = findViewById(R.id.add_review_button);
        favoriteButton = findViewById(R.id.favorite_button);
        goToFavoritesButton = findViewById(R.id.go_to_favorites_button); // 버튼 초기화

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        isFavorite = sharedPreferences.getBoolean(PREF_IS_FAVORITE_KEY, false);
        updateFavoriteButton();

        loadReviews();

        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreReviewListActivity.this, StoreReviewActivity.class);
                startActivity(intent);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButtonEnabled) {
                    addToFavorites();
                }
            }
        });

        goToFavoritesButton.setOnClickListener(new View.OnClickListener() { // 버튼 클릭 리스너
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreReviewListActivity.this, FavoriteListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addToFavorites() {
        if (currentUser == null) {
            Toast.makeText(this, "사용자가 로그인되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getEmail(); // 사용자 이메일을 문서 ID로 사용
        String packageName = getPackageName(); // 패키지 이름(가게 이름) 가져오기
        String storeEmail = packageName + "@gmail.com"; // "@gmail.com"을 붙인 가게 이메일

        // 사용자 이메일을 Firebase Database 경로로 변환
        String firebasePath = userId.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_"); // '.', '#', '$', '[', ']'를 '_'로 대체

        DatabaseReference userRef = favoritesRef.child(firebasePath);

        if (isFavorite) {
            // 사용자의 찜 목록에서 가게 이메일 제거
            userRef.removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(StoreReviewListActivity.this, "찜 목록에서 가게가 제거되었습니다.", Toast.LENGTH_SHORT).show();
                                isFavorite = false; // isFavorite 업데이트
                                updateFavoriteButton(); // 버튼 텍스트 업데이트
                            } else {
                                Toast.makeText(StoreReviewListActivity.this, "찜 목록에서 가게를 제거하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // 사용자의 찜 목록에 패키지 이름 추가
            userRef.setValue(storeEmail) // storeEmail을 값으로 사용
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                // "users" 컬렉션에서 사용자 이메일 업데이트
                                userRef.child("email")
                                        .setValue(currentUser.getEmail())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(StoreReviewListActivity.this, "찜 목록에 가게가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                                    isFavorite = true; // isFavorite 업데이트
                                                    updateFavoriteButton(); // 버튼 텍스트 업데이트
                                                } else {
                                                    Toast.makeText(StoreReviewListActivity.this, "찜 목록에 가게를 추가하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(StoreReviewListActivity.this, "찜 목록에 가게를 추가하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateFavoriteButton() {
        editor.putBoolean(PREF_IS_FAVORITE_KEY, isFavorite);
        editor.apply();

        if (isFavorite) {
            favoriteButton.setText("찜 해제");
        } else {
            favoriteButton.setText("찜하기");
        }
        isButtonEnabled = true; // 찜하기 버튼 활성화
    }

    private void loadReviews() {
        firestore.collection("storereviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Review> reviewList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String content = document.getString("content");
                                float rating = document.getDouble("rating").floatValue();
                                Review review = new Review(title, content);
                                review.setRating(rating);
                                reviewList.add(review);
                            }

                            reviewAdapter = new StoreReviewAdapter(StoreReviewListActivity.this, reviewList);
                            reviewListView.setAdapter(reviewAdapter);
                        } else {
                            Toast.makeText(StoreReviewListActivity.this, "리뷰를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}