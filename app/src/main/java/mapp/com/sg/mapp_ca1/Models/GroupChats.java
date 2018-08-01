package mapp.com.sg.mapp_ca1.Models;

import java.io.Serializable;
import java.util.List;

public class GroupChats implements Serializable {
    // Chat id in Firebase
    private String chatId;
    // Chat name
    private String chatName;
    // Description of chat
    private String chatDesc;
    // count members list to get
    private int memCount;
    // Stores member ids
    private List<String> members;
    // Store id of each meetups, call them through another method
    //private List<String> meetups;
    private String picURL;

    // Constructor
    public GroupChats(String chatId, String chatName, String chatDesc, int memCount, List<String> members, String picURL) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.chatDesc = chatDesc;
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

    public String getChatName() { return chatName; }

    public void setChatName(String chatName) { this.chatName = chatName; }

    public String getChatDesc() {
        return chatDesc;
    }

    public void setChatDesc(String chatDesc) {
        this.chatDesc = chatDesc;
    }

    public int getMemCount() { return memCount; }

    public void setMemCount(int memCount) {
        this.memCount = memCount;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) { this.members = members; }

    public String getPicURL() { return picURL; }

    public void setPicURL(String picURL) { this.picURL = picURL; }
}
