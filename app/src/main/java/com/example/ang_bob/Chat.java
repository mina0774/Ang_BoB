package com.example.ang_bob;

public class Chat {
    public String room_title; //채팅방 제목
    public String message;//작성 메시지
    public String username;

    public Chat(){

    }

    public Chat(String room_title,String message,String username){
        this.room_title=room_title;
        this.message=message;
        this.username=username;
    }

    public Chat (String message, String username){
        this.message=message;
        this.username=username;
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


}


