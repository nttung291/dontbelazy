package com.example.sonnv.dontbelazy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Nttung PC on 9/21/2017.
 */

public class ContactHandleDB {
    private MyDatabase myDatabase;
    private static ContactHandleDB contactdatabaseHandle;
    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_CREATE =
            "create table if not exists logs (_id integer primary key autoincrement, "+ "date datetime, city text, " + "type text, log text, nick text);";

    public ContactHandleDB(Context context) {
        myDatabase = new MyDatabase(context);
    }

    public static ContactHandleDB getInstance(Context context){
        if (contactdatabaseHandle == null){
            contactdatabaseHandle = new ContactHandleDB(context);
        }
        return contactdatabaseHandle;
    }

    public List<Contact> getListContact(){
        List<Contact> contactList = new ArrayList<>();
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM contact",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            // get data
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String mail = cursor.getString(2);
            String phonenumber = cursor.getString(3);
            String address = cursor.getString(4);
            String subject = cursor.getString(5);
            String note = cursor.getString(6);
            Boolean isMale = cursor.getInt(7) != 0;

            Contact contact = new Contact(id,name,mail,phonenumber,address,subject,note,isMale);
            contactList.add(contact);

            // next cursor
            cursor.moveToNext();
        }
      return contactList;
    }

    public Contact getContactAtId(int id) {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM contact WHERE id = " + id, null);
        cursor.moveToFirst();
        try {
            String name = cursor.getString(1);
            String mail = cursor.getString(2);
            String phonenumber = cursor.getString(3);
            String address = cursor.getString(4);
            String subject = cursor.getString(5);
            String note = cursor.getString(6);
            Boolean isMale = cursor.getInt(7) != 0;
            return new Contact(id,name,mail,phonenumber,address,subject,note,isMale);
        } catch (Exception ex) {
            return null;
        }
    }

    public void setContact(String name, String mail,String phoneNumber,String address,String subject,String note,boolean isMale){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("mail",mail);
        contentValues.put("phonenumber",phoneNumber);
        contentValues.put("address",address);
        contentValues.put("subject",subject);
        contentValues.put("note",note);
        if (isMale){
            contentValues.put("sex",1);
        }else{
            contentValues.put("sex",0);
        }
        sqLiteDatabase.insert("contact",null,contentValues);
    }

    public void deleteContact(int id) {
        sqLiteDatabase.delete("contact", "id = " + id, null);
    }

    public void editContact(Contact contact,String name,String mail, String phoneNumber, String address, String subject,String note, boolean isMale){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("mail",mail);
        contentValues.put("phonenumber",phoneNumber);
        contentValues.put("address",address);
        contentValues.put("subject",subject);
        contentValues.put("note",note);
        if (isMale){
            contentValues.put("sex",1);
        }else{
            contentValues.put("sex",0);
        }
//        sqLiteDatabase.update("tbl_short_story",contentValues,
//                "id = " + storyModel.getId(),null);
        sqLiteDatabase.update("contact",contentValues,
                "id = ?", new String[]{String.valueOf(contact.getId())});

    }
}
