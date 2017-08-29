package ramt57.infotrench.com.callrecorder.contacts;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.MainActivity;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * Created by sandhya on 23-Aug-17.
 */

public class ContactProvider {
    static refresh itemrefresh;
    static deleterefresh itemdelete;
    public static  void deletelistener(deleterefresh list){ itemdelete=list;}
    public static void setItemrefresh(refresh listener){
        itemrefresh=listener;
    }
    public static ArrayList<Contacts> getContacts(Context ctx) {
        ArrayList<Contacts> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        Contacts info = new Contacts();
                        info.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        info.setNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        info.setPhoto(photo);
                        info.setPhotoUri(pURI);
                        list.add(info);
                    }
                    cursorInfo.close();
                }
            }
        }
        return list;
    }

    public static String getCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    public static String getrelative(long time) {
        long d = (System.currentTimeMillis() / 1000) - time;
        String remainingTime ="";
        if (d < 60) {
            //seconds
            remainingTime = ((((d % 31536000) % 86400) % 3600) % 60) + "s";
        } else if (d > 60 && d < 3600) {
            //in minutes
            remainingTime = Math.round((((d % 31536000) % 86400) % 3600) / 60) + "m";
        } else if (d > 3600 && d < 86400) {
            //in hours
            remainingTime = Math.round(((d % 31536000) % 86400) / 3600) + "h";
        } else if (d > 86400 && d < 31536000) {
            //in days
            remainingTime = Math.round((d % 31536000) / 86400) + "d";
        } else {
            //in years
            remainingTime = Math.round(d / 31536000) + "y";
        }
        return remainingTime;
    }

    public static ArrayList<Contacts> getCallList(Context ctx, ArrayList<String> recording, String type) {
        ArrayList<Contacts> allContactList = new ArrayList<>();
        allContactList = ContactProvider.getContacts(ctx);
        ArrayList<Contacts> recordedContacts = new ArrayList<>();
        boolean hascontact = false;
        if (type.equals("IN")) {
            //incoming list
            recordedContacts.clear();
            for (String filename : recording) {
                String recordedfilearray[] = filename.split("__");      //recorded file_array
                if (recordedfilearray[2].equals("IN")) {
                    //incoming
                    for (Contacts people : allContactList) {
                        if (StringUtils.prepareContacts(ctx, people.getNumber()).equalsIgnoreCase(recordedfilearray[0])) {
                            long timestamp = new Long(recordedfilearray[1]).longValue();
                            String relative_time = ContactProvider.getrelative(timestamp);
                            Contacts contacts=new Contacts();
                            contacts.setName(people.getName());
                            contacts.setNumber(people.getNumber());
                            contacts.setTime(relative_time);
                            contacts.setPhoto(people.getPhoto());
                            recordedContacts.add(contacts);
                            hascontact = true;
                            break;
                        }
                    }

                    if (!hascontact) {
                        //no contact show them
                        long timestamp = new Long(recordedfilearray[1]).longValue();
                        String relative_time = ContactProvider.getrelative(timestamp);
                        Contacts nocontact = new Contacts();
                        nocontact.setNumber(recordedfilearray[0]);
                        nocontact.setTime(relative_time);
                        recordedContacts.add(nocontact);
                    } else {
                        hascontact = false;
                    }
                }
            }
        } else if (type.equals("OUT")) {
            recordedContacts.clear();
            for (String filename : recording) {
                String recordedfilearray[] = filename.split("__");      //recorded file_array
                if (recordedfilearray[2].equals("OUT")) {
                    //incoming
                    for (Contacts people : allContactList) {
                        if (StringUtils.prepareContacts(ctx, people.getNumber()).equalsIgnoreCase(recordedfilearray[0])) {
                            long timestamp = new Long(recordedfilearray[1]).longValue();
                            String relative_time = ContactProvider.getrelative(timestamp);
                            Contacts contacts=new Contacts();
                            contacts.setName(people.getName());
                            contacts.setNumber(people.getNumber());
                            contacts.setTime(relative_time);
                            contacts.setPhoto(people.getPhoto());
                            recordedContacts.add(contacts);
                            hascontact = true;
                            break;
                        }
                    }

                    if (!hascontact) {
                        //no contact show them
                        long timestamp = new Long(recordedfilearray[1]).longValue();
                        ContactProvider.getrelative(timestamp);
                        String relative_time = ContactProvider.getrelative(timestamp);
                        Contacts nocontact = new Contacts();
                        nocontact.setNumber(recordedfilearray[0]);
                        nocontact.setTime(relative_time);
                        recordedContacts.add(nocontact);
                    } else {
                        hascontact = false;
                    }
                }
            }
        } else {
            recordedContacts.clear();
            for (String filename : recording) {
                String recordedfilearray[] = filename.split("__");      //recorded file_array
                for (Contacts people : allContactList) {
                    if (StringUtils.prepareContacts(ctx, people.getNumber()).equalsIgnoreCase(recordedfilearray[0])) {
                        long timestamp = new Long(recordedfilearray[1]).longValue();
                        String relative_time = ContactProvider.getrelative(timestamp);
                        Contacts contacts=new Contacts();
                        contacts.setName(people.getName());
                        contacts.setNumber(people.getNumber());
                        contacts.setTime(relative_time);
                        contacts.setPhoto(people.getPhoto());
                        recordedContacts.add(contacts);
                        hascontact = true;
                        break;
                    }
                }

                if (!hascontact) {
                    //no contact show them
                    long timestamp = new Long(recordedfilearray[1]).longValue();
                    ContactProvider.getrelative(timestamp);
                    String relative_time = ContactProvider.getrelative(timestamp);
                    Contacts nocontact = new Contacts();
                    nocontact.setNumber(recordedfilearray[0]);
                    nocontact.setTime(relative_time);
                    recordedContacts.add(nocontact);
                } else {
                    hascontact = false;
                }
            }
        }
        if(!recordedContacts.isEmpty()){
            addToDatabase(ctx,recordedContacts);
        }
        return recordedContacts;
    }


    public static void sendnotification(Context ctx) {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(ctx);
        notifyBuilder.setContentTitle("Call recording in progress...");
        notifyBuilder.setSmallIcon(R.drawable.record);
        notifyBuilder.setTicker("New message");

        Intent notificationIntent = new Intent(ctx, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notifyBuilder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notifyBuilder.build());
    }

    public static void openMaterialSheetDialog(LayoutInflater inflater, final int position, final String recording, final Contacts contacts) {

        View view = inflater.inflate(R.layout.bottom_menu, null);
         DatabaseHelper db=new DatabaseHelper(view.getContext());
         TextView play =  view.findViewById(R.id.play);
        TextView favorite =  view.findViewById(R.id.fav);
        TextView delete =  view.findViewById(R.id.delete);
         TextView turnoff=view.findViewById(R.id.turn_off);
        TextView upload=view.findViewById(R.id.upload);
        final Dialog materialSheet=new Dialog(view.getContext(),R.style.MaterialDialogSheet);
        materialSheet.setContentView(view);
        materialSheet.setCancelable(true);
        materialSheet.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        materialSheet.getWindow().setGravity(Gravity.BOTTOM);
        materialSheet.show();
        if(checkFav(view.getContext(),contacts.getNumber())){
            //set text remove
            favorite.setText("Add to favourite");
        }else{
            //set text add
            favorite.setText("Remove from favourtie");
        }
        if(checkContactToRecord(view.getContext(),contacts.getNumber())){
            turnoff.setText("Turn off recording");
        }else{
            turnoff.setText("Turn on recording");
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(),position, Toast.LENGTH_SHORT).show();
                playmusic(v.getContext(), Environment.getExternalStorageDirectory()+"/CallRecorder/"+recording);
                materialSheet.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file=new File(Environment.getExternalStorageDirectory()+"/CallRecorder/"+recording);
                if(file.delete()){
                    //deleted
                    itemdelete.deleterefreshList(true);
                    Toast.makeText(view.getContext(),"File deleted Successfully",Toast.LENGTH_SHORT).show();
                }else{
                    //not deleted
                    itemdelete.deleterefreshList(true);
                    Toast.makeText(view.getContext(),"Deletion failed",Toast.LENGTH_SHORT).show();
                }
                materialSheet.dismiss();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFavourite(view.getContext(),contacts.getNumber())){
                    Toast.makeText(view.getContext(),"added to favourite",Toast.LENGTH_SHORT).show();
                    itemrefresh.refreshList(true);
                }else{
                    itemrefresh.refreshList(true);
                    Toast.makeText(view.getContext(),"removed from fvourite",Toast.LENGTH_SHORT).show();
                }
                materialSheet.dismiss();
            }
        });
        turnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //turn off recording
                if(checkContactToRecord(view.getContext(),contacts.getNumber())){
                    // recording enabled turn it off
                    if(!togglestate(view.getContext(),contacts.getNumber())){
                        //off
                        Toast.makeText(view.getContext(),"turned off",Toast.LENGTH_SHORT).show();
                        itemrefresh.refreshList(true);
                    }
                }else{
                    if(togglestate(view.getContext(),contacts.getNumber())){
                        Toast.makeText(view.getContext(),"turned on",Toast.LENGTH_SHORT).show();
                        itemrefresh.refreshList(true);
                    }
                    //recording disabled turn it on
                }
                materialSheet.dismiss();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialSheet.dismiss();
                itemrefresh.refreshList(true);
            }
        });
    }
    public static void playmusic(Context ctx,String path){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(path);
        intent.setDataAndType(Uri.fromFile(file), "audio/*");
        ctx.startActivity(intent);
    }

    public static ArrayList<String> getRecordingList(Context ctx,ArrayList<String> recordings,String type){
        ArrayList<String> newRecordings=new ArrayList<>();
        if (type.equals("IN")) {
            //incoming list
            newRecordings.clear();
            for (String filename : recordings) {
//                newRecordings.add(filename);
                String recordedfilearray[] = filename.split("__");      //recorded file_array
                if (recordedfilearray[2].equals("IN")) {
                    newRecordings.add(filename);
                }
            }
        } else if (type.equals("OUT")) {
            newRecordings.clear();
            for (String filename : recordings) {
                String recordedfilearray[] = filename.split("__");      //recorded file_array
                if (recordedfilearray[2].equals("OUT")) {
                    newRecordings.add(filename);
                }
            }
        } else {
            newRecordings.clear();
            for (String filename : recordings) {

                String recordedfilearray[] = filename.split("__");      //recorded file_array
                    newRecordings.add(filename);
            }
        }

        return newRecordings;
    }
    //SQL Lite Database
    public static boolean checkFavourite(Context context,String number){
        DatabaseHelper db=new DatabaseHelper(context);
        Contacts contacts1=db.isContact(number);
        if(contacts1.getFav()==0){
            contacts1.setFav(1);
            db.updateContact(contacts1);
            return true;
        }else if(contacts1.getFav()==1){
            contacts1.setFav(0);
            db.updateContact(contacts1);
            return false;
        }else{
            return false;
        }
    }
    public static boolean checkFav(Context context,String number){
        DatabaseHelper db=new DatabaseHelper(context);
        Contacts contacts1=db.isContact(number);
        if(contacts1.getFav()==0){
            return true;
        }else if(contacts1.getFav()==1){
            return false;
        }else{
            return true;
        }
    }
    private static void addToDatabase(Context ctx,ArrayList<Contacts> recordedContacts) {
        DatabaseHelper db=new DatabaseHelper(ctx);
        for (Contacts cont:recordedContacts){
            Contacts s=db.isContact(cont.getNumber());
            if(s.getNumber()!=null){
                //has contanct
            }else{
                // no contacnt
                cont.setFav(0);
                cont.setState(0);
                cont.setNumber(StringUtils.prepareContacts(ctx,cont.getNumber()));
                db.addContact(cont);
            }
        }
    }
    public static boolean checkContactToRecord(Context ctx,String number){
        DatabaseHelper db=new DatabaseHelper(ctx);
        Contacts newcontacts=db.isContact(number);
        if(newcontacts.getNumber()!=null){
            if(newcontacts.getState()==0){
                //recording on
                return true;
            }else if(newcontacts.getState()==1){
                return false;
                //dont wanna record
            }else{
                return true;
            }
        }
        return true;
    }

    public static boolean togglestate(Context ctx,String number){
        DatabaseHelper db=new DatabaseHelper(ctx);
            Contacts s=db.isContact(number);
            if(s.getNumber()!=null) {
                //has contanct
                if(s.getState()==0) {
                    s.setState(1);
                    db.updateContact(s);
                    return false;
                }else if(s.getState()==1) {
                    s.setState(0);
                    db.updateContact(s);
                    return  true;
                }
            }
        return true;
    }
    public interface refresh{
        public void refreshList(boolean var);
    }
    public interface deleterefresh{
        public void deleterefreshList(boolean var);
    }
}
