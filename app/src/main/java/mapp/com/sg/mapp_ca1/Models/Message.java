package mapp.com.sg.mapp_ca1.Models;

public class Message {
    private String uid;
    private String text;
    private String name;
    private String photoUrl;
    private String timestamp;
    private String profileUrl;
    private String chatid;
    private String chatName;

    public Message() {
    }

    //Construtor

    public Message(String uid, String text, String name, String photoUrl, String timestamp, String profileUrl, String chatid, String chatName) {
        this.uid = uid;
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.timestamp = timestamp;
        this.profileUrl = profileUrl;
        this.chatid = chatid;
        this.chatName = chatName;
    }

    // Getters() and Setters()
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}
