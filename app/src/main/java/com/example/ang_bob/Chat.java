package com.example.ang_bob;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat {
    private String username;
    private String message;//작성 메시지
    private String currentTime;
    public Chat(){

    }

    public Chat(String username,String message){
        this.username=username;
        this.message=message;
        this.currentTime = getCurrentTime();
    }
    public void setMessage(String message){
        this.message=message;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getUsername(){
        return username;
    }

    public String getMessage(){
        return message;
    }

    public String getTime() {
        return currentTime;
    }

    public String getCurrentTime(){
       // 현재시간을 msec 으로 구한다.
       long now = System.currentTimeMillis();
       // 현재시간을 date 변수에 저장한다.
       Date date = new Date(now);
       // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
       SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       // nowDate 변수에 값을 저장한다.

       String formatDate = sdfNow.format(date);

       return formatDate.substring(11,16);
   }


}


