package com.example.sonnv.dontbelazy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuonghx2709 on 9/26/2017.
 */

public class NoteHandleDB {
    private static final String TAG = NoteHandleDB.class.toString();
    private MyDatabase myDatabase;
    private static NoteHandleDB noteHandleDB;
    private SQLiteDatabase sqLiteDatabase;

    public NoteHandleDB(Context context){
        this.myDatabase = new MyDatabase(context);
    }


    public static NoteHandleDB getInstence(Context context){
        if (noteHandleDB == null){
            noteHandleDB = new NoteHandleDB(context);
        }

        return  noteHandleDB;
    }

    public List<Note> getList(){
        List<Note> list = new ArrayList<>();
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_note", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            String text = cursor.getString(1);
            String time = cursor.getString(2);
            boolean notifi = cursor.getInt(3) != 0;
            String maintext = cursor.getString(4);
            Log.d(TAG, "getList: " + maintext);
            cursor.moveToNext();

            Note note = new Note(id, time, text, maintext, notifi);

            list.add(note);
        }

        return list;
    }

    public void editNote(Note note,String title,String text, String time, boolean notifi){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("text", title);
        contentValues.put("maintext", text);


        if (notifi){
            contentValues.put("notifi", 1);
        }else {
            contentValues.put("notifi", 0);
        }
        contentValues.put("time", time);


        Log.d(TAG, "editNote: " + note.getId());

        sqLiteDatabase.update("tb_note",contentValues,
                "idNote = " + note.getId(),null);
//        sqLiteDatabase.update("tb_note",contentValues,
//                "id = ?", new String[]{String.valueOf(note.getId())});

    }

    public void deleteNote(int idNote) {
        sqLiteDatabase.delete("tb_note", "idNote = " + idNote, null);
    }

    public void setNote(String tittle,String text, String time ,boolean nofifi){

        Log.d("cuonghx2709", "setNote: " + tittle + text + time + nofifi);
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("text",  tittle);
        contentValues.put("time", time);

        if (nofifi){
            contentValues.put("notifi", 1);
        }else {
            contentValues.put("notifi", 0);
        }
        contentValues.put("maintext", text);

        sqLiteDatabase.insert("tb_note",null,contentValues);
    }



}
