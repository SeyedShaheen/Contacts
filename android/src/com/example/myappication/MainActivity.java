package com.example.myappication;

import android.Manifest;
import android.annotation.SuppressLint;
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

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = ((Cursor) cursor).getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contacts.add(name + ":" + phoneNumber);

                //Log.d("Contact", "Name: " + name + ", Phone Number: " + phoneNumber + " " + "Total Contacts: " + cursor.getCount());
            }
            cursor.close();
        }
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        Log.d("---------> Read Duration: ", tDelta+"");
        return contacts;
    }


    public void updateContacts(ArrayList<String> updatedList) {
        for (int i = 0; i< updatedList.size(); i++) {
                    String element = updatedList.get(i);
                    if (!initialContacts.contains(element)) {
//                        Log.d("New element:---> ",element);
//                        Log.d("New elements index :---> ",i+"");
                        update(pointer, element, i);
                        initialContacts.add(i,element);
                    }
                }

                for (int i = 0; i < initialContacts.size(); i++) {
                    String element = initialContacts.get(i);
                    if (!updatedList.contains(element)) {
//                        Log.d("Removed element:---> ",element);
//                        Log.d("Removed elements index :---> ",i+"");
                        removeFromModel(pointer,i);
                        initialContacts.remove(i);
                    }
                }

        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
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
        public void onChange(boolean selfChange){
            super.onChange(selfChange);
            long tStart = System.currentTimeMillis();
            ArrayList<String> newArrList = readContacts();
            updateContacts(newArrList);
            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            Log.d("---------> Update Duration: ", tDelta+"");
        }

        @Override
        public void onChange(boolean selfChange, Uri uri){
            super.onChange(selfChange, uri);
            ArrayList<String> newArrList = readContacts();
            updateContacts(newArrList);
        }

    }
}
