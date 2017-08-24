package ramt57.infotrench.com.callrecorder.utils;

import android.content.Context;
import android.telephony.PhoneNumberUtils;

/**
 * Created by sandhya on 24-Aug-17.
 */

public class StringUtils  {
    public static  String prepareContacts(Context ctx,String number){
            String preparednumbers=number.trim();
            preparednumbers=preparednumbers.replace(" ","");
            preparednumbers=preparednumbers.replace("(","");
            preparednumbers=preparednumbers.replace(")","");
            if(preparednumbers.contains("+")){
                preparednumbers=preparednumbers.replace(preparednumbers.substring(0,3),""); //to remove country code
            }
            preparednumbers=preparednumbers.replace("-","");
            return preparednumbers;
    }
}
