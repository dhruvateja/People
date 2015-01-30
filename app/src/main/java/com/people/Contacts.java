package com.people;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;


public class Contacts extends Activity {
    TextView mTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mTxt = (TextView) findViewById(R.id.text);
        //  getCallDetails();
       logCallLog();
       // readContacts();
    }


    public void getContactLogDetails() {
        //     ContentResolver cr = getActivity().getContentResolver();
        //  Cur
        //CallLog.Calls.DATE + " DESC"

    }

    public void logCallLog() {
        long dialed;
        String columns[] = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,

                CallLog.Calls.TYPE};

      //  Cursor curLog = CallLogHelper.getAllCallLogs(getContentResolver());
        Cursor c;
        c = getContentResolver().query(Uri.parse("content://call_log/calls"),
                columns, null, null, "Calls._ID DESC"); //last record first
        // System.out.println("phani call log count "+c.getCount());
        while (c.moveToNext()) {
            dialed = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
            System.out.println("phani date "+dialed);
            System.out.println("phani num "+ c.getString(1
            ));
            //Log.i("CallLog", "type: " + c.getString(4) + "Call to number: " + c.getString(1) + ", registered at: " + new Date(dialed).toString());
        }
    }

    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        ContentResolver cr = getContentResolver();
        String strGroupBy = " 1=1) group by( " + android.provider.CallLog.Calls.NUMBER;

        Cursor managedCursor = cr.query(CallLog.Calls.CONTENT_URI, null, strGroupBy, null, CallLog.Calls.DATE + " DESC");
        System.out.println("phani cursor cnt " + managedCursor.getCount());

        sb.append("Call Details :");
        managedCursor.moveToFirst();
        do {
            int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            String callerName = managedCursor.getString(name);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
//           Log.v("Name ",callerName);
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration + "\n caller Name :-" + callerName);
            sb.append("\n----------------------------------");

        } while (managedCursor.moveToNext());
        /*while ( managedCursor.moveToNext() ) {
            String callerName = managedCursor.getString(name);
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            Log.v("Name ",callerName);
            sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration +"\n caller Name :-"+callerName);
            sb.append("\n----------------------------------");
        }*/
        managedCursor.close();
        mTxt.setText(sb);
    }
    public void favContacts(){
        ContentResolver cr = getContentResolver();
        String columns[] = new String[]{
                  ContactsContract.Contacts.STARRED
        };

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, columns, null, null, null);
        System.out.println("phani fav conts cnt ="+cur.getCount());
    }
    public void readContacts() {
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                cur.getColumnIndex(ContactsContract.Contacts.STARRED);
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                        System.out.println("phone" + phone);
                    }
                    pCur.close();
                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        emailContact = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                        System.out.println("Email " + emailContact + " Email Type : " + emailType);
                    }
                    emailCur.close();
                }
                if (image_uri != null) {
                    System.out.println(Uri.parse(image_uri));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(image_uri));
                        sb.append("\n Image in Bitmap:" + bitmap);
                        System.out.println(bitmap);
                    } catch (FileNotFoundException e) { // TODO Auto-generated catch block e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); } } sb.append("\n........................................"); } textDetail.setText(sb); } }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mTxt.setText(sb.toString());

            }
        }

    }
}