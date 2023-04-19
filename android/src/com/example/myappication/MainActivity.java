package com.example.myappication;

import android.os.Bundle;
import android.util.Log;

//import androidx.annotation.NonNull;
import org.qtproject.qt.android.bindings.QtActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;

public class MainActivity extends QtActivity {



    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d("here: ", "RUNNING!");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS},
//                        CONTACTS_PERMISSION_REQUEST_CODE);
//            }
//        }

        super.onCreate(savedInstanceState);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_REQUEST_CODE);
//        } else {
//            readContacts();
//        }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                createContacts();
//            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    private void readContacts(){
//        Cursor cursor = getContentResolver().query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                null, null, null);

//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                @SuppressLint("Range") String name = ((Cursor) cursor).getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                Log.i("Contact", "Name: " + name + ", Phone Number: " + phoneNumber + " " + "Total Contacts: " + cursor.getCount());
//            }
//            cursor.close();
//        }
//    }

//    private void createContacts() {
//        ContentResolver contentResolver = getContentResolver();
//
//        for (int i = 1; i <= 1000; i++) {
//            String displayName = "Contact_" + i;
//            String phoneNumber = "123456789" + i;
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(ContactsContract.RawContacts.ACCOUNT_TYPE, (String) null);
//            contentValues.put(ContactsContract.RawContacts.ACCOUNT_NAME, (String) null);
//
//            Uri rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
//            long rawContactId = ContentUris.parseId(rawContactUri);
//
//            contentValues.clear();
//            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//            contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName);
//            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
//
//            contentValues.clear();
//            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//            contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//            contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
//            contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
//        }
//    }
}
    public void printSmth(){
        Log.d("print", "SOMSETHING!!!");
    }

    public int rtrnSmth(){
        return 10;
    }
}