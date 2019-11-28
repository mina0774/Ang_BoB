package com.example.ang_bob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class SignUpActivity extends AppCompatActivity {

    private static final Pattern PWD_RULE=Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");
    private static final Pattern EMAIL_RULE=Pattern.compile("^[a-zA-Z0-9]+@cau.ac.kr+$");
    private EditText email_e,pwd_e,check_pwd,name_e,age_e;
    private TextView check_show;
    private Button signup_btn;
    private CheckBox male_box,female_box;
    long count=0;
    private String email="";
    private String pwd="";
    private String name="";
    private String gender="";
    private String age="";

    public static int TIME_OUT = 1001;
    ProgressDialog dialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email_e=(EditText)findViewById(R.id.emailIn);
        pwd_e=(EditText)findViewById(R.id.passwordIn);
        check_pwd=(EditText)findViewById(R.id.checkPassword);
        name_e=(EditText)findViewById(R.id.name);
        age_e=(EditText)findViewById(R.id.age);
        check_show=(TextView)findViewById(R.id.checkText);
        signup_btn=(Button)findViewById(R.id.sign_up);
        male_box=(CheckBox)findViewById(R.id.male);
        female_box=(CheckBox)findViewById(R.id.female);
        //비밀번호 일치하는지 확인, 일치 시에 회원가입 가능
        check_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp1=pwd_e.getText().toString();
                String temp2=check_pwd.getText().toString();

                if(temp1.equals(temp2)){
                    check_show.setText("비밀번호가 일치합니다.");
                    signup_btn.setEnabled(true);
                }else{
                    check_show.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

        //회원가입을 누르면 유저를 등록
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                email_e.setText(null);
                pwd_e.setText(null);
                check_pwd.setText(null);
                name_e.setText(null);
                age_e.setText(null);
                male_box.setChecked(false);
                female_box.setChecked(false);
            }
        });

        //여성일 때,
        female_box.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="여자";
                male_box.setChecked(false);
                female_box.setChecked(true);
            }
        });

        //남성일 때,
        male_box.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="남자";
                female_box.setChecked(false);
                male_box.setChecked(true);
            }
        });
    }
    //이메일 인증 메일 요청 기다리기 위함
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == TIME_OUT) { // 타임아웃이 발생하면
                dialog.dismiss(); // ProgressDialog를 종료시킴
            }
        }
    };
    //이메일 유효성 검사 @cau.ac.kr 형식만 허용
    private boolean isValidEmail() {
        if(email.isEmpty()){
            return false;
        }else {
            return EMAIL_RULE.matcher(email).matches();
        }
    }

    //비밀번호 유효성 검사
    private boolean isValidPwd(){
        if(pwd.isEmpty()){
            return false;
        }else return PWD_RULE.matcher(pwd).matches();
    }

    //회원 가입, firebase 이메일 인증 방식
    private void signupUser(final String email,String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mFirebaseUser = firebaseAuth.getCurrentUser();
                            if (mFirebaseUser != null){
                                mFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "인증 메일 전송:" + mFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                            dialog = ProgressDialog.show(SignUpActivity.this, "회원가입이 완료되었습니다.", mFirebaseUser.getEmail() + "으로 인증메일이 전송되었습니다.", true);
                                            mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);

                                            //데이터베이스에 유저를 등록
                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        count++;
                                                    }

                                                    User user = new User(email, name, gender, age);
                                                    StringTokenizer stringTokenizer = new StringTokenizer(email, "@");
                                                    if (count >= 9) {
                                                        myRef.child("user0" + (count + 1) + ":" + stringTokenizer.nextToken()).setValue(user);
                                                    } else {
                                                        myRef.child("user00" + (count + 1) + ":" + stringTokenizer.nextToken()).setValue(user);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "인증 메일 전송을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

        });
    }

    public void registerUser(){
        email=email_e.getText().toString();
        pwd=pwd_e.getText().toString();
        name=name_e.getText().toString();
        age=age_e.getText().toString();
        if(isValidEmail()==false){
            Toast.makeText(SignUpActivity.this,"중앙대학교 이메일을 입력해주세요",Toast.LENGTH_SHORT).show();
        }
        if(isValidEmail() && isValidPwd()){
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.useAppLanguage();
            signupUser(email,pwd);
        }
    }

}
