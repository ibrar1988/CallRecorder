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
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.MainActivity;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * Created by sandhya on 23-Aug-17.
 */

public class ContactProvider {

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
        String remainingTime = "";
        if (d < 60) {
            //seconds
            remainingTime = ((((d % 31536000) % 86400) % 3600) % 60) + " seconds ago";
        } else if (d > 60 && d < 3600) {
            //in minutes
            remainingTime = Math.round((((d % 31536000) % 86400) % 3600) / 60) + " minutes ago";
        } else if (d > 3600 && d < 86400) {
            //in hours
            remainingTime = Math.round(((d % 31536000) % 86400) / 3600) + " hours ago";
        } else if (d > 86400 && d < 31536000) {
            //in days
            remainingTime = Math.round((d % 31536000) / 86400) + " days ago";
        } else {
            //in years
            remainingTime = Math.round(d / 31536000) + " years ago";
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
                            people.setTime(relative_time);
                            recordedContacts.add(people);
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
                            people.setTime(relative_time);
                            recordedContacts.add(people);
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
                        people.setTime(relative_time);
                        recordedContacts.add(people);
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

    public static void openMaterialSheetDialog(LayoutInflater inflater, final int position) {

        View view = inflater.inflate(R.layout.dialog_lyout, null);
        ImageView play = (ImageView) view.findViewById(R.id.imageView2);
        ImageView favorite = (ImageView) view.findViewById(R.id.imageView3);
        ImageView delete = (ImageView) view.findViewById(R.id.imageView4);
        final Dialog materialSheet=new Dialog(view.getContext(),R.style.MaterialDialogSheet);
        materialSheet.setContentView(view);
        materialSheet.setCancelable(true);
        materialSheet.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        materialSheet.getWindow().setGravity(Gravity.BOTTOM);
        materialSheet.show();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),position, Toast.LENGTH_SHORT).show();
                materialSheet.dismiss();
            }
        });
    }
}
