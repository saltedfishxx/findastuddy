package mapp.com.sg.mapp_ca1.Models;

public class Users {

    public Users(String uid, String username, String education_lvl, String year, String stream) {
        this.uid = uid;
        this.username = username;
        this.education_lvl = education_lvl;
        this.year = year;
        this.stream = stream;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEducation_lvl() {
        return education_lvl;
    }

    public void setEducation_lvl(String education_lvl) {
        this.education_lvl = education_lvl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    private String uid;
    private String username;
    private String education_lvl;
    private String year;
    private String stream;
}
