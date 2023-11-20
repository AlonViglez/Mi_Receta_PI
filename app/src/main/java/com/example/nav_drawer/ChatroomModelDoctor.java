package com.example.nav_drawer;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatroomModelDoctor {
    String chatroomid;
    List<String> doctorIds;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderId;

    public ChatroomModelDoctor() {
    }

    public ChatroomModelDoctor(String chatroomid, List<String> doctorIds, Timestamp lastMessageTimestamp, String lastMessageSenderId) {
        this.chatroomid = chatroomid;
        this.doctorIds = doctorIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getChatroomid() {
        return chatroomid;
    }

    public void setChatroomid(String chatroomid) {
        this.chatroomid = chatroomid;
    }

    public List<String> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<String> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }
}
