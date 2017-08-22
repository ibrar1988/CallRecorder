package ramt57.infotrench.com.callrecorder.pojo_classes;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class Contacts {
    String name;
    String number;
    String profile_url;
    Boolean fav;

    public Contacts(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }
}
