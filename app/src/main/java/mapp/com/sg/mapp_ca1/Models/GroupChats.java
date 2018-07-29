package mapp.com.sg.mapp_ca1.Models;

import java.util.List;

public class GroupChats {
    // Chat id in Firebase
    private String chatId;
    // Description of chat
    private String chatDesc;
    // count members list to get
    private int chatMems;
    // Stores member ids
    private List<String> members;
    // Store id of each meetups, call them through another method
    //private List<String> meetups;
    private String picURL;

    // Constructor
    public GroupChats(String chatId, String chatDesc, int chatMems, List<String> members, String picURL) {
        this.chatId = chatId;
        this.chatDesc = chatDesc;
        this.chatMems = chatMems;
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

    public String getChatDesc() {
        return chatDesc;
    }

    public void setChatDesc(String chatDesc) {
        this.chatDesc = chatDesc;
    }

    public int getChatMems() {
        return chatMems;
    }

    public void setChatMems(int chatMems) {
        this.chatMems = chatMems;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
