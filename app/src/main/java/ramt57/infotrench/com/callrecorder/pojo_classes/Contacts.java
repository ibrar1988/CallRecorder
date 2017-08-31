package ramt57.infotrench.com.callrecorder.pojo_classes;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class Contacts {
    int id;
    String name;
    String number;
    Bitmap photo;
    Uri photoUri;
    String time;
    int fav;
    int state;
    String records;
    public Contacts(){

    }
    public Contacts(int id, String _phone_number,int fav,int state){
        this.id = id;
        this.number = _phone_number;
        this.fav=fav;
        this.state=state;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {

        return time;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTime(String time) {
        this.time = time;
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

    public int getFav() {
        return fav;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
