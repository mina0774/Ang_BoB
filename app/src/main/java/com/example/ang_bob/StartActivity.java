package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("사용자");
    private TextView tv_customer_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        tv_customer_name=(TextView)findViewById(R.id.tv_customer_name);

        if (userAuth != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userAuth.getEmail().equals(ds.child("email").getValue().toString())) {
                           String username = ds.child("username").getValue().toString();
                           tv_customer_name.setText(username+"님, 환영합니다!");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void gotoMyPage(View view){
        Intent intent = new Intent(StartActivity.this,MyPageActivity.class);
        startActivity(intent);
    }

    public void gotoAppointmentPage(View view){
        Intent intent = new Intent(StartActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
