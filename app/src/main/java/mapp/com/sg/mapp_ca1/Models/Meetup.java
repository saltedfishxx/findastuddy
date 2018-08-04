package mapp.com.sg.mapp_ca1.Models;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

public class Meetup implements Serializable{
    private String meetId;
    private Date dateTime;
    private String groupChatID;
    private GeoPoint location;
    private String meetupName;
    private int noPpl;

    public Meetup() {

    }

    public Meetup(String meetId, Date dateTime, String groupChatID, GeoPoint location, String meetupName, int noPpl) {
        this.meetId = meetId;

        this.dateTime = dateTime;
        this.groupChatID = groupChatID;
        this.location = location;
        this.meetupName = meetupName;
        this.noPpl = noPpl;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getGroupChatID() {
        return groupChatID;
    }

    public void setGroupChatID(String groupChatID) {
        this.groupChatID = groupChatID;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getMeetupName() {
        return meetupName;
    }

    public void setMeetupName(String meetupName) {
        this.meetupName = meetupName;
    }

    public int getNoPpl() {
        return noPpl;
    }

    public void setNoPpl(int noPpl) {
        this.noPpl = noPpl;
    }

    public String getMeetId() {
        return meetId;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }

}
