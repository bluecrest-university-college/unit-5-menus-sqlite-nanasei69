package com.example.contactsbook;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import static android.os.Build.ID;

public class DatabaseConnector {
    private static final String DATABASE_NAME="UserContacts";
    public Cursor getAllContacts;
    private static SQLiteDatabase db;
    private DatabaseOpenHelper dbHelper;

    public DatabaseConnector(Context context){
        dbHelper=new DatabaseOpenHelper(context,DATABASE_NAME,null,1);
    }

    public void open() {
        db=dbHelper.getWritableDB();
    }

    public static void close() {
        if(db!=null)
            db.close();
    }


    public Cursor getGetAllContacts(){
        return db.query("contacts", new String[]{"_id","name"}, null, null, null, null ,"name");
    }



    public Cursor getOneContact(Long ID){
        return db.query("contacts", null, "_id="+ID, null, null, null, null);
    }

    public void deleteContact(Long ID) {
        open();
        db.delete("contacts","_id"+ID,null);
        close();
    }


    public void insertContact(String name, String phone, String email) {
        ContentValues newContact=new ContentValues();
        newContact.put("name",name);
        newContact.put("phone",phone);
        newContact.put("email",email);
        open();
        db.insert("contacts",null,newContact);
        close();
    }

    public void updateContact(long rowID, String name, String phone, String email) {
        ContentValues editContact=new ContentValues();
        editContact.put("name",name);
        editContact.put("phone",phone);
        editContact.put("email",email);
        open();
        db.update("contacts",editContact,"_id="+rowID,null);
        close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper{


        public DatabaseOpenHelper(Context context,String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
            // onCreate running if the database does not yet exist
        @Override
        public void onCreate(SQLiteDatabase db){
            String createTable="CREATE TABLE contacts"
                    + "(_id integer primary key autoincrement," +
                    "name TEXT, phone TEXT, email TEXT)";
            db.execSQL(createTable);
        }
                //if you supply a newer version number than the database version currently
        //stored on the device, onUpgrade running but onDowngrade vice versa
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            // no implementation
        }

        public SQLiteDatabase getWritableDB() {
            db=dbHelper.getWritableDB();
            return null;

        }
    }// end of DatabaseOpenHelper Class

}// end of DatabaseConnector Class
