package ramt57.infotrench.com.callrecorder.pojo_classes;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class Contacts {
    String name;
    String number;
    Bitmap photo;
    Boolean fav;
    Uri photoUri;
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

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
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
