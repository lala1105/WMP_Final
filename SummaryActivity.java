package com.example.final_exam;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private TextView textViewSubjects, textViewCredits;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize UI components
        textViewSubjects = findViewById(R.id.textViewSubjects);
        textViewCredits = findViewById(R.id.textViewCredits);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fetchEnrollmentData();
    }

    private void fetchEnrollmentData() {
        // Get current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Fetch enrollment data from Firestore
        db.collection("Students").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve selected subjects and total credits
                        List<String> subjects = (List<String>) documentSnapshot.get("selectedSubjects");
                        Long totalCredits = documentSnapshot.getLong("totalCredits");

                        if (subjects != null && !subjects.isEmpty()) {
                            // Display enrolled subjects
                            textViewSubjects.setText("Enrolled Subjects:\n" + String.join("\n", subjects));
                        } else {
                            textViewSubjects.setText("No subjects enrolled yet.");
                        }

                        // Display total credits
                        if (totalCredits != null) {
                            textViewCredits.setText("Total Credits: " + totalCredits);
                        } else {
                            textViewCredits.setText("Total Credits: 0");
                        }
                    } else {
                        // If no enrollment data found
                        textViewSubjects.setText("No enrollment data found.");
                        textViewCredits.setText("Total Credits: 0");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle Firestore fetch failure
                    Toast.makeText(this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    textViewSubjects.setText("Failed to fetch enrollment data.");
                    textViewCredits.setText("Total Credits: 0");
                });
    }
}
