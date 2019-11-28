package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class ChatActivity extends AppCompatActivity {
    private String room_name = "";
    private String username = "";

    private TextView room_title;
    private EditText edit_message;
    private Button btn_send;
    private ListView list_message;
    private TextView number;
    private Button btn_out;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("사용자");
    private DatabaseReference chat_databaseReference=firebaseDatabase.getReference("채팅");
    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private String user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        room_title = (TextView) findViewById(R.id.room_title);
        edit_message = (EditText) findViewById(R.id.edit_message);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_out=(Button)findViewById(R.id.button);
        list_message = (ListView) findViewById(R.id.list_message);
        number=(TextView)findViewById(R.id.number);
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        user_email=userAuth.getEmail().toString();

        room_name= getIntent().getStringExtra("room_title");
        room_title.setText(room_name);

        chat_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Integer i=(int)dataSnapshot.child(room_name).child("user").getChildrenCount();
                number.setText("현재인원 : " + i.toString()+"명");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (userAuth != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userAuth.getEmail().equals(ds.child("email").getValue().toString())) {
                            username = ds.child("username").getValue().toString();
                            Chat chat = new Chat(username ,username + "님이 들어오셨습니다.");
                            chat_databaseReference.child(room_name).child("chatting history").push().setValue(chat);
                            chat_databaseReference.child(room_name).child("user").child(username).setValue(userAuth.getEmail());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        openChat(room_name);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_message.getText().toString().equals(""))
                    return;
                Chat chat = new Chat( username,edit_message.getText().toString());
                chat_databaseReference.child(room_name).child("chatting history").push().setValue(chat);
                chat_databaseReference.child(room_name).child("user").child(username).setValue(userAuth.getEmail());
                edit_message.setText("");
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chat_databaseReference.child(room_name).child("user").child(username).setValue(null);

                chat_databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        Integer i=(int)dataSnapshot.child(room_name).child("user").getChildrenCount();
                        if(i==0){
                            chat_databaseReference.child(room_name).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(ChatActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        Chat chat = dataSnapshot.getValue(Chat.class);
        if(chat.getMessage().equals(username + "님이 들어오셨습니다.")){
            adapter.add(chat.getMessage() + " " + "("+chat.getTime()+")");
        }
        else {
            adapter.add(chat.getUsername() + " : " + chat.getMessage() + " " + "("+chat.getTime()+")");
        }
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        Chat chat = dataSnapshot.getValue(Chat.class);
        adapter.remove(chat.getUsername() + " : " + chat.getMessage());
    }
    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        list_message.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        chat_databaseReference.child(chatName).child("chatting history").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:" + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}