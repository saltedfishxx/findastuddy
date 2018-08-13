package mapp.com.sg.mapp_ca1.Models;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Meetup implements Serializable{
    //Meetup ID in firestore
    private String meetId;
    //meetup name
    private String meetupName;
    //meetup date
    //meetup date and time
    private Date dateTime;
    //Selected chat id for meetup
    private String groupChatID;
    //selected location
    private GeoPoint location;
    //number of people going for meetup
    private int noPpl;
    // userid of users going
    private List<String> userids;


    // Constructor
    public Meetup(String meetId, String meetupName, Date dateTime, String groupChatID, GeoPoint location, int noPpl, List<String> userids) {
        this.meetId = meetId;
        this.meetupName = meetupName;
        this.dateTime = dateTime;
        this.groupChatID = groupChatID;
        this.location = location;
        this.noPpl = noPpl;
        this.userids = userids;
    }

    // Getter () and Setter()
    public String getMeetId() {return meetId;}

    public void setMeetId(String meetId) {this.meetId = meetId;}

    public String getMeetupName() {return meetupName;}

    public void setMeetupName(String meetupName) {this.meetupName = meetupName;}

    public Date getDateTime() {return dateTime;}

    public void setDateTime(Date dateTime) {this.dateTime = dateTime;}

    public String getGroupChatID() {return groupChatID;}

    public void setGroupChatID(String groupChatID) {this.groupChatID = groupChatID;}

    public GeoPoint getLocation() {return location;}

    public void setLocation(GeoPoint location) {this.location = location;}

    public int getNoPpl() {return noPpl;}

    public void setNoPpl(int noPpl) {this.noPpl = noPpl;}

    public List<String> getUserids() {return userids;}

    public void setUserids(List<String> userids) {this.userids = userids;}
}
