package com.example.myappication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;
import android.database.ContentObserver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//import androidx.annotation.NonNull;
import org.qtproject.qt.android.bindings.QtActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;

public class MainActivity extends QtActivity {


    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS
    };
    private contactsContentObserver contactsContentObserver;
    public long pointer;
    public ArrayList<String> initialContacts;
    ArrayList<String> initialContactIds = new ArrayList<>();
    public long loadedTimestamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Request the required permission if it hasn't been granted yet
            requestPermissions(REQUIRED_PERMISSIONS, 1);
            readContacts();
        }
        super.onCreate(savedInstanceState);
        Handler handler = new Handler(getApplicationContext().getMainLooper());
        contactsContentObserver = new contactsContentObserver(handler, this);
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactsContentObserver);
        initialContacts = readContacts();
        for (int i = 0; i < initialContacts.size(); i++){
            String[] elements = initialContacts.get(i).split(":");
            initialContactIds.add(elements[0]);
        }
//        Log.d("","--------------->>"+readContacts().size());
//        Log.d("","--------------->>"+initialContacts.size());
}

void setPointer(long ptr){
        pointer = ptr;
}

public native void update(long ptr, String updatedContacts, int index);
public native void removeFromModel(long ptr, int index);

    protected void onDestroy() {
        super.onDestroy();

        // Unregister the content observer when the activity is destroyed
        if (contactsContentObserver != null) {
            getContentResolver().unregisterContentObserver(contactsContentObserver);
        }
    }

    public ArrayList<String> readContacts(){
        long tStart = System.currentTimeMillis();

        ArrayList<String> contacts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);

        int i = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                @SuppressLint("Range") String name = ((Cursor) cursor).getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(contactId + ":" + name + ":" + phoneNumber);
            }
            cursor.close();
        }
        loadedTimestamp = System.currentTimeMillis();
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        Log.d("---------> Read Duration: ", tDelta+"");
        return contacts;
    }


    public void updateContacts(ArrayList<String> updatedList) {

        Log.d("", "updated list size: " + updatedList.size());

        ArrayList<String> updatedContactIds = new ArrayList<>();

        for (int i = 0; i < updatedList.size(); i++){
            String[] elements = updatedList.get(i).split(":");
            updatedContactIds.add(elements[0]);
        }

        for (int i = 0; i < updatedList.size(); i++){
            int idx = initialContactIds.indexOf(updatedContactIds.get(i));
            String element = updatedList.get(i);
            Log.d("-->>>", element);
            Log.d("Index to change :---> ",idx+"");
            Log.d("Updated contactId :---> ",element+"");

            if (idx >= 0) {
                removeFromModel(pointer,idx);
                update(pointer, element, 0);
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                initialContacts.remove(idx);
                initialContacts.add(0,element);
            } else if (!initialContacts.contains(element)) {
                initialContacts.add(0,element);
                update(pointer, element, 0);
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            }
        }
//        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                Toast.makeText(this, "Permission denied. Cannot load contacts.", Toast.LENGTH_SHORT).show();
            }
        }
}

    class contactsContentObserver extends ContentObserver{

        private Context context;
        private MainActivity mainActivity;

        public contactsContentObserver(Handler handler, MainActivity mainActivity) {
            super(handler);
            this.mainActivity = mainActivity;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri, int flags) {
            ArrayList<String> newArrList = new ArrayList<>();
            Log.d("", "onChange Called");
            long lastUpdateTimestamp = loadedTimestamp; // the timestamp of the last update
            String selection = ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " > ?";
            String[] selectionArgs = new String[]{ String.valueOf(lastUpdateTimestamp) };
            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{
                            ContactsContract.Data.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.Contacts.DISPLAY_NAME
                    },
                    selection,
                    selectionArgs,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // process the updated contact
                    @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    newArrList.add(contactId + ":" + name + ":" + phoneNumber);
                    Log.d("Updated Contacts", "ContactID: "+ contactId +", Name: " + name + ", Phone Number: " + phoneNumber + " " + "Total Contacts: " + cursor.getCount());
                } while (cursor.moveToNext());
                cursor.close();
            }
            else {
                String[] projection = new String[]{
                        ContactsContract.DeletedContacts.CONTACT_ID,
                        ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP
                };

                String delSelection = ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP + " > ?";
                String[] delSelectionArgs = new String[]{ String.valueOf(lastUpdateTimestamp) };
                Cursor deleteCursor = getContentResolver().query(
                        ContactsContract.DeletedContacts.CONTENT_URI,
                        projection,
                        delSelection,
                        delSelectionArgs,
                        null
                );
                if (deleteCursor != null && deleteCursor.moveToFirst()) {
                    do {
                        // process the deleted contact
                        @SuppressLint("Range") String contactId = deleteCursor.getString(deleteCursor.getColumnIndex(ContactsContract.DeletedContacts.CONTACT_ID));
                        @SuppressLint("Range") long deletedTimestamp = deleteCursor.getLong(deleteCursor.getColumnIndex(ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP));
                        Log.d("Deleted Contacts", "ContactID: "+ contactId + ", Deleted Timestamp: " + deletedTimestamp);
                    } while (deleteCursor.moveToNext());
                    deleteCursor.close();
                }
            }



            loadedTimestamp = System.currentTimeMillis();
            Log.d("", "" + newArrList.size());
            updateContacts(newArrList);
        }
    }
}
