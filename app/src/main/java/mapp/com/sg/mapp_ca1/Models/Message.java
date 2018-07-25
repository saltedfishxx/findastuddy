package mapp.com.sg.mapp_ca1.Models;

import java.util.Date;

public class Message {
    private String uid;
    private String text;
    private String name;
    private String photoUrl;
    private String timestamp;
    private String profileUrl;

    public Message() {
    }

    public Message(String uid, String text, String name, String photoUrl, String timestamp, String profileUrl) {
        this.uid = uid;
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.timestamp = timestamp;
        this.profileUrl = profileUrl;
    }

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
}
