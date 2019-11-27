package com.example.ang_bob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button appointment_btn;
    private Button search_btn;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appointment_btn=(Button)findViewById(R.id.appointment_btn);
        search_btn=(Button)findViewById(R.id.search_btn);
        image=(ImageView)findViewById(R.id.image);
    }

    public void makeAppointment(View view){
        Intent intent = new Intent(MainActivity.this, MakeAppointmentActivity.class);
        startActivity(intent);
    }

    public void searchRoom(View view){
        Intent intent = new Intent(MainActivity.this, RoomsActivity.class);
        startActivity(intent);
    }
}
