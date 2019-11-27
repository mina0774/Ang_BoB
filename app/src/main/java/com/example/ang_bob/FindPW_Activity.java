package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPW_Activity extends AppCompatActivity {

    private EditText et_UserEmail;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String emailID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_);

        et_UserEmail=(EditText)findViewById(R.id.et_UserEmail);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void sendMail(View view){
        progressDialog.setMessage("이메일 전송 중입니다, 잠시만 기다려주세요.");
        progressDialog.show();
        emailID=et_UserEmail.getText().toString().trim();

        firebaseAuth.sendPasswordResetEmail(emailID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(FindPW_Activity.this,"이메일이 전송되었습니다.",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                }else{
                    Toast.makeText(FindPW_Activity.this,"이메일 전송을 실패했습니다.",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });

    }
}
