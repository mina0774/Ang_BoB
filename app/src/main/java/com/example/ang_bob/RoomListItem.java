package com.example.ang_bob;

import java.util.ArrayList;
import java.util.List;

public class RoomListItem {
    private String room_title;
    private String total_people_number;
    private List<String> users_Email;

    public RoomListItem(String room_title, String total_people_number, ArrayList<String> users_Email) {
        this.room_title = room_title;
        this.total_people_number = total_people_number;
        this.users_Email = users_Email;
    }
    public RoomListItem(String room_title, String total_people_number) {
        this.room_title = room_title;
        this.total_people_number = total_people_number;
    }

    public List<String> getUsers_Email() {
        return users_Email;
    }

    public void setUsers_Email(ArrayList<String> users_Email) {
        this.users_Email = users_Email;
    }

    public void setRoom_title(String room_title){
        this.room_title=room_title;
    }
    public void setTotal_people_number(String total_people_number){
        this.total_people_number=total_people_number;
    }

    public String getRoom_title(){
        return this.room_title;
    }

    public String getTotal_people_number(){
        return this.total_people_number;
    }
}

