package com.example.final_exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class EnrollmentActivity extends AppCompatActivity {

    CheckBox cbSubject1, cbSubject2, cbSubject3, cbSubject4, cbSubject5, cbSubject6, cbSubject7, cbSubject8, cbSubject9;
    Button buttonSubmit;
    ArrayList<String> selectedSubjects = new ArrayList<>();
    int totalCredits = 0, maxCredits = 24;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        cbSubject1 = findViewById(R.id.cbSubject1);
        cbSubject2 = findViewById(R.id.cbSubject2);
        cbSubject3 = findViewById(R.id.cbSubject3);
        cbSubject4 = findViewById(R.id.cbSubject4);
        cbSubject5 = findViewById(R.id.cbSubject5);
        cbSubject6 = findViewById(R.id.cbSubject6);
        cbSubject7 = findViewById(R.id.cbSubject7);
        cbSubject8 = findViewById(R.id.cbSubject8);
        cbSubject9 = findViewById(R.id.cbSubject9);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> calculateCredits());
    }

    private void calculateCredits() {
        totalCredits = 0;
        selectedSubjects.clear();

        if (cbSubject1.isChecked()) { totalCredits += 3; selectedSubjects.add("Computer Vision"); }
        if (cbSubject2.isChecked()) { totalCredits += 3; selectedSubjects.add("Deep Learning"); }
        if (cbSubject3.isChecked()) { totalCredits += 3; selectedSubjects.add("Robotics"); }
        if (cbSubject4.isChecked()) { totalCredits += 3; selectedSubjects.add("NLP"); }
        if (cbSubject5.isChecked()) { totalCredits += 3; selectedSubjects.add("NLUG"); }
        if (cbSubject6.isChecked()) { totalCredits += 3; selectedSubjects.add("Intelligent Robotics"); }
        if (cbSubject7.isChecked()) { totalCredits += 3; selectedSubjects.add("Image Processing and Recognition"); }
        if (cbSubject8.isChecked()) { totalCredits += 3; selectedSubjects.add("Game Programming"); }
        if (cbSubject9.isChecked()) { totalCredits += 3; selectedSubjects.add("Embedded System"); }


        if (totalCredits > maxCredits) {
            Toast.makeText(this, "Credit limit exceeded! Max: " + maxCredits, Toast.LENGTH_SHORT).show();
        } else {
            saveEnrollmentToFirestore();
        }
    }

    private void saveEnrollmentToFirestore() {
        String userId = mAuth.getCurrentUser().getUid();


        HashMap<String, Object> enrollmentData = new HashMap<>();
        enrollmentData.put("selectedSubjects", selectedSubjects);
        enrollmentData.put("totalCredits", totalCredits);

        db.collection("Students").document(userId)
                .update(enrollmentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Enrollment saved successfully", Toast.LENGTH_SHORT).show();
                    navigateToSummary();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void navigateToSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putStringArrayListExtra("subjects", selectedSubjects);
        intent.putExtra("credits", totalCredits);
        startActivity(intent);
    }
}
