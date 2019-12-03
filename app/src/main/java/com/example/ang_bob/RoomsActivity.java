package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class RoomsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("채팅");

    //FCM 구현 변수
    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey =
            "key=" +  " AIzaSyA2gi_Un1Y9jjRiB2t5poYVb3jGZwokxKw"; //띄어쓰기해야함
    private String contentType = "application/json";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    AlertDialog.Builder alert;

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    //현재하는거 주제메세징 시스템 -  구독한 사람에게 알림
    // 나 -- 다른사용자 2명이있을 때
    // 로그인하면 rnjsdnfkacau.ac.kr 이런 이름을 구독함
    // mina0774 구독자에게 메세지 전송이 가능함 그래서 서로 쪽지 주고받을수있음
    // 그럼 구독처리는 로그인 시 해야함


    private List<RoomListItem> roomListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        roomAdapter=new RoomAdapter(getApplicationContext(), roomListItems, R.layout.room_listview);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        recyclerView=(RecyclerView) findViewById(R.id.recyclerview);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(roomAdapter);
        roomAdapter.notifyDataSetChanged();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomListItems.clear();
                int number = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    for(DataSnapshot ds2 : ds.getChildren()) {
                        if(ds2.getKey().equals("user")){
                            ArrayList arrayList = new ArrayList<>();
                            number =(int)ds2.getChildrenCount();
                           for(DataSnapshot ds3: ds2.getChildren()){
                               arrayList.add(ds3.getValue().toString());//이메일 저장 부분 / 사용자들
                           }
                            RoomListItem a = new RoomListItem(ds.getKey(), number+"명", arrayList);
                            roomListItems.add(a);
                            roomAdapter.notifyDataSetChanged();
                            number = 0;
                        }
                    }
                }
                Log.d("number",number+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //방 입장 부분에 상대방에게 알림
        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position ) {
                final int fi = position;
                alert = new AlertDialog.Builder(RoomsActivity.this);
                alert  .setTitle("알림")
                        .setMessage(roomListItems.get(fi).getRoom_title()+"\n 예약에 참여하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //여기서부터 채팅방
                                jsonObjectInput(roomListItems.get(fi).getUsers_Email(), fi);

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alert.show();
            }

        });

    }

    private void jsonObjectInput(List<String> arrayList, int idx){
        for(int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).equals(firebaseUser.getEmail()))
                continue;
            StringTokenizer st = new StringTokenizer(arrayList.get(i), "@");
            TOPIC = "/topics/" + st.nextToken()+ st.nextToken();
            NOTIFICATION_TITLE = "알림";
            NOTIFICATION_MESSAGE = firebaseUser.getEmail() + "님이 방을 들어왔어요";

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE);
                notifcationBody.put("message", NOTIFICATION_MESSAGE);

                notification.put("to", TOPIC);
                notification.put("data", notifcationBody);
            } catch (JSONException e) {
                Log.e("TAG", "onCreate: " + e.getMessage());
            }
            sendNotification(notification);
        }

        final Intent intent=new Intent(RoomsActivity.this, ChatActivity.class);
        intent.putExtra("room_title", roomListItems.get(idx).getRoom_title());
        startActivity(intent);
        finish();
    }

    //알림 전송부분
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,

                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        PushAlarm.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


}
