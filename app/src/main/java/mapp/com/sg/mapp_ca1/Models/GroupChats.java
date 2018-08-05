package mapp.com.sg.mapp_ca1.Models;

import java.io.Serializable;
import java.util.List;

public class GroupChats implements Serializable {
    // Chat id in Firestore
    private String chatId;
    // Chat name
    private String chatName;
    // Description of chat
    private String chatDesc;
    // Subject
    private String chatSubject;
    // count members list to get
    private int memCount;
    // Stores member ids
    private List<String> members;
    // Store id of each meetups, call them through another method
    //private List<String> meetups;
    private String picURL;

    // Constructor
    public GroupChats(String chatId, String chatName, String chatDesc, String chatSubject, int memCount, List<String> members, String picURL) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.chatDesc = chatDesc;
        this.chatSubject = chatSubject;
        this.memCount = memCount;
        this.members = members;
        this.picURL = picURL;
    }

    // Getters() and Setters()
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatDesc() {
        return chatDesc;
    }

    public void setChatDesc(String chatDesc) {
        this.chatDesc = chatDesc;
    }

    public String getChatSubject() {return chatSubject;}

    public void setChatSubject(String chatSubject) {this.chatSubject = chatSubject;}

    public int getMemCount() {
        return memCount;
    }

    public void setMemCount(int memCount) {
        this.memCount = memCount;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
}
