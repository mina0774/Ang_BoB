package com.example.ang_bob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MakeAppointmentActivity extends AppCompatActivity {
    private TextView tv_date;
    private TextView tv_time;
    private List<String> shop_list;
    private EditText et_shop;
    private ListView shop_listview;
    private SearchAdapter searchAdapter;
    private ArrayList<String> arrayList;
    private Button appointment_final;
    private String year;
    private String month;
    private String day;
    private String appointment_date;
    private String appointment_time;

    private DatePickerDialog.OnDateSetListener date_callback;
    private TimePickerDialog.OnTimeSetListener time_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        et_shop=(EditText)findViewById(R.id.et_shop);
        shop_listview=(ListView)findViewById(R.id.shop_listview);
        appointment_final=(Button)findViewById(R.id.appointment_final);

        shop_list=new ArrayList<String>();
        arrayList=new ArrayList<String>();
        set_shopList();
        arrayList.addAll(shop_list);
        searchAdapter=new SearchAdapter(shop_list,this);
        shop_listview.setAdapter(searchAdapter);

        shop_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_shop.setText(shop_list.get(i));
            }
        });

        et_shop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=et_shop.getText().toString();
                search(text);
            }
        });


        Date currentTime = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        year = yearFormat.format(currentTime);
        month = monthFormat.format(currentTime);
        day = dayFormat.format(currentTime);


        tv_date=(TextView)findViewById(R.id.date);
        tv_time=(TextView)findViewById(R.id.time);

        this.InitializeListener();

        appointment_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_date.getText().toString().equals("")||tv_time.getText().toString().equals("")||et_shop.getText().toString().equals("")){
                    Toast.makeText(MakeAppointmentActivity.this,"값을 모두 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("time22",""+Limit_date_time());

                if(!Limit_date_time()){
                    Toast.makeText(MakeAppointmentActivity.this,"현재 시간보다 이후인 시간을 설정해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent=new Intent(MakeAppointmentActivity.this, ChatActivity.class);
                intent.putExtra("date",tv_date.getText().toString());
                intent.putExtra("time",tv_time.getText().toString());
                intent.putExtra("shop",et_shop.getText().toString());
                intent.putExtra("room_title",tv_date.getText().toString()+"-"+tv_time.getText().toString()+"-"+et_shop.getText().toString());
                startActivity(intent);
            }
        });

    }

    public void search(String findText){
        Log.d("shoplist",""+shop_list);
        shop_list.clear(); // 문자 입력 시에 리스트를 지우고 다시 새로 보여줌
        if(findText.length()==0){
            shop_list.addAll(arrayList);
        }
        else{
            for(int i=0;i<arrayList.size();i++){
                if(arrayList.get(i).toLowerCase().contains(findText)){
                    shop_list.add(arrayList.get(i));
                }
            }
        }
        Log.d("arraylist",""+arrayList);
       searchAdapter.notifyDataSetChanged();
    }

    public Boolean Limit_date_time()  {
        //현재 시간 가져오기
        long now =System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", java.util.Locale.getDefault());
        String getTime = simpleDate.format(mDate);
        Date currentTime= null;
        Date appointmentTIme = null;
        try {
            currentTime = simpleDate.parse(getTime);
            appointmentTIme = simpleDate.parse(appointment_date+appointment_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return appointmentTIme.after(currentTime);
    }

    public void InitializeListener(){
        date_callback=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tv_date.setText(i+"년 "+(i1+1)+"월 "+i2+"일");
                appointment_date=i+"-"+(i1+1)+"-"+i2+" ";
                Log.d("data1111",appointment_date);
            }
        };
        time_callback=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                tv_time.setText(i+"시 "+i1+"분 ");
                appointment_time=i+":"+i1;
                Log.d("time1111",appointment_time);
            }
        };

    }

    public void dateHandler(View view){
        DatePickerDialog date_dialog=new DatePickerDialog(MakeAppointmentActivity.this,date_callback,Integer.parseInt(year),Integer.parseInt(month) -1,Integer.parseInt(day));
        date_dialog.show();

    }
    public void timeHandler(View view){
        TimePickerDialog time_dialog=new TimePickerDialog(this,time_callback,12,0,false);
        time_dialog.show();
    }


    private void set_shopList(){
        shop_list.add("308관 기숙사 식당");
        shop_list.add("309관 기숙사 식당");
        shop_list.add("310관 학생 식당");
        shop_list.add("법학관 식당");
        shop_list.add("University Club");
        shop_list.add("310 쌀국수");
        shop_list.add("카우버거");
        shop_list.add("고봉 삼계탕");
        shop_list.add("고씨네");
        shop_list.add("곱창의 전설");
        shop_list.add("구공탄 불고기");
        shop_list.add("권가네 항아리 된장족발");
        shop_list.add("김가네");
        shop_list.add("김밥천국");
        shop_list.add("노랑통닭");
        shop_list.add("니뽕내뽕");
        shop_list.add("단비분식");
        shop_list.add("더 마니 치킨");
        shop_list.add("더 진국");
        shop_list.add("도스마스");
        shop_list.add("등촌샤브칼국수&청기와뼈다귀해장국");
        shop_list.add("라이스&포테이토");
        shop_list.add("라화쿵부");
        shop_list.add("롯데리아");
        shop_list.add("리얼후라이");
        shop_list.add("마포최대포숯불갈매기");
        shop_list.add("모로미");
        shop_list.add("모모치");
        shop_list.add("미묘");
        shop_list.add("미소야");
        shop_list.add("미니 자이언트");
        shop_list.add("미스터피자");
        shop_list.add("밀플랜비");
        shop_list.add("본도시락");
        shop_list.add("본죽");
        shop_list.add("봉구스 밥버거");
        shop_list.add("부어치킨");
        shop_list.add("북촌순두부");
        shop_list.add("붐바타");
        shop_list.add("BHC");
        shop_list.add("사과나무");
        shop_list.add("삼곱식당");
        shop_list.add("스시초이");
        shop_list.add("스시톡톡");
        shop_list.add("신전떡볶이");
        shop_list.add("써브웨이");
        shop_list.add("아빠곰탕");
        shop_list.add("안동장");
        shop_list.add("양셰프");
        shop_list.add("양푼이 김치찌개");
        shop_list.add("엉터리 생고기");
        shop_list.add("옹골진 치킨");
        shop_list.add("왕돈까스 왕냉면");
        shop_list.add("우뇽파스타뚝배기스파게티");
        shop_list.add("육쌈냉면");
        shop_list.add("은행골");
        shop_list.add("이모네 떡볶이");
        shop_list.add("이탈리안 돈까스");
        shop_list.add("일이삼식당");
        shop_list.add("정부대찌개");
        shop_list.add("정직한 김치찌개");
        shop_list.add("죠스떡볶이");
        shop_list.add("죽이야기");
        shop_list.add("준호가 만드는 즉석 떡볶이");
        shop_list.add("준호가 만드는 부대찌개");
        shop_list.add("준호네 돈가스");
        shop_list.add("중문 닭갈비");
        shop_list.add("중앙 돼지 마을");
        shop_list.add("진상천");
        shop_list.add("철판 목장");
        shop_list.add("치폴레옹");
        shop_list.add("칠기 마라샹궈 마라탕");
        shop_list.add("쿵푸");
        shop_list.add("퀴즈노스");
        shop_list.add("큰맘할매순대국");
        shop_list.add("타누키돈부리");
        shop_list.add("투고샐러드");
        shop_list.add("포마토김밥");
        shop_list.add("피자보이시나");
        shop_list.add("하꼬멘");
        shop_list.add("하노이별");
        shop_list.add("학교종이 땡땡땡");
        shop_list.add("해오름 갈비");
        shop_list.add("허가네 김밥");
        shop_list.add("해달비");
        shop_list.add("해랑초밥");
        shop_list.add("호치킨");
        shop_list.add("호밀 떡볶이");
        shop_list.add("홍당무");
        shop_list.add("홍천닭갈비");
        shop_list.add("홍콩반점0410");
        shop_list.add("화르르");
        shop_list.add("흑수돈");
        shop_list.add("흥부네닭강정");
    }

}
