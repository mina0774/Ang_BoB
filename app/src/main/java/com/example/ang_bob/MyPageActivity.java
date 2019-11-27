package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    private ListView myroom_list;
    private TextView tv_room;
    String username;
    private RoomAdapter roomAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("사용자");
    private DatabaseReference chat_databaseReference = firebaseDatabase.getReference("채팅");
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        myroom_list=(ListView)findViewById(R.id.myroom_list);
        tv_room=(TextView)findViewById(R.id.tv_room);
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        if (userAuth != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userAuth.getEmail().equals(ds.child("email").getValue().toString())) {
                            username = ds.child("username").getValue().toString();
                            tv_room.setText(username+"님이 예약하신 방");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        /*
        roomAdapter=new RoomAdapter();
        adapter=new ArrayAdapter<String>(this, R.layout.room_listview, R.id.tv_title);
        myroom_list.setAdapter(adapter);
        chat_databaseReference.addChildEventListener(new ChildEventListener() { //채팅
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot ds: dataSnapshot.child("user").getChildren()) {
                        if (userAuth.getEmail().equals(ds.getValue().toString())){
                            Integer number=(int)dataSnapshot.child("user").getChildrenCount();
                            //ArrayList<String> arrayList = new ArrayList<>();
                            //여긴 다에
                            roomAdapter.addItem(dataSnapshot.getKey(),number.toString()+"명");
                            adapter.add(dataSnapshot.getKey() );
                            Log.d("roomadapter",""+dataSnapshot.getKey().toString()+" "+number.toString());
                            myroom_list.setAdapter(roomAdapter);
                        }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //showRoomList();
        myroom_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Intent intent = new Intent(MyPageActivity.this, MyRoomActivity.class);
                intent.putExtra("room_title", adapter.getItem(i));
                startActivity(intent);
            }

        });

         */
    }


}

