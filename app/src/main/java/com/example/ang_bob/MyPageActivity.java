package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class MyPageActivity extends AppCompatActivity {

    private RecyclerView myroom_list;
    private TextView tv_room;
    String username;
    private RoomAdapter roomAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("사용자");
    private DatabaseReference chat_databaseReference = firebaseDatabase.getReference("채팅");
    private List<RoomListItem> roomListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        myroom_list=(RecyclerView) findViewById(R.id.myroom_list);
        roomAdapter=new RoomAdapter(getApplicationContext(), roomListItems, R.layout.room_listview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myroom_list.setHasFixedSize(true);
        myroom_list.setLayoutManager(layoutManager);
        myroom_list.setAdapter(roomAdapter);
        roomAdapter.notifyDataSetChanged();

        tv_room=(TextView)findViewById(R.id.tv_room);
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        if (userAuth != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

        chat_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomListItems.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    int number = 0;
                    for(DataSnapshot ds2 : ds.getChildren()) {
                        if(ds2.getKey().equals("user")){
                            number =(int)ds2.getChildrenCount();
                            RoomListItem a = new RoomListItem(ds.getKey(), number+"명");
                            roomListItems.add(a);
                            roomAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position ) {
                final Intent intent = new Intent(MyPageActivity.this, MyRoomActivity.class);
                intent.putExtra("room_title", roomListItems.get(position).getRoom_title());
                startActivity(intent);
            }
        });
    }/*
    final Intent intent = new Intent(MyPageActivity.this, MyRoomActivity.class);
                intent.putExtra("room_title", adapter.getItem(i));
    startActivity(intent);
    */

}

