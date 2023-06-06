package com.example.moracmoracsignintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoreReviewActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private RatingBar ratingBar;
    private Button submitButton;

    private FirebaseFirestore firestore;
    private CollectionReference reviewCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_review);

        titleEditText = findViewById(R.id.review_title_text);
        contentEditText = findViewById(R.id.review_content_text);
        ratingBar = findViewById(R.id.review_rating_bar);
        submitButton = findViewById(R.id.submit_review_button);

        firestore = FirebaseFirestore.getInstance();
        reviewCollection = firestore.collection("storereviews");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered review information
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                float rating = ratingBar.getRating();

                // Save the review information
                saveReview(title, content, rating);
            }
        });
    }

    private void saveReview(String title, String content, float rating) {
        // Get the logged-in user's email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();

            // Create a new review object
            Review review = new Review(title, content, rating, "", email); // Empty value for markerTitle

            // Generate the ID based on the user's email
            String reviewId = email.replace("@", "@").replace(".", ".");

            // Set the review with the specified ID in the "storereviews" collection
            reviewCollection
                    .document(reviewId)
                    .set(review)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Display a success message when the review is successfully saved
                            Toast.makeText(StoreReviewActivity.this, "리뷰가 저장되었습니다.", Toast.LENGTH_SHORT).show();

                            // Create an Intent to move to the StoreReviewListActivity
                            Intent intent = new Intent(StoreReviewActivity.this, StoreReviewListActivity.class);
                            startActivity(intent);

                            // Finish the current activity
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Display an error message if there was an error while saving the review
                            Toast.makeText(StoreReviewActivity.this, "리뷰 저장 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case when the user is not logged in
            Toast.makeText(StoreReviewActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

}