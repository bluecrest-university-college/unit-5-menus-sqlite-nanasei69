 package com.example.contactsbook;

import android.app.ListActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ContactsBook extends ListActivity {

    public static final String ROW_ID = "row_id"; //intent extra key
    private ListView contactListView; //the ListActivity's ListView
    private CursorAdapter contactAdapter; //adapter for ListView



    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState); // call super Activity class
        contactListView = getListView(); //get the built-in ListView
        contactListView.setOnItemClickListener(viewContactListener); // wait

        // map each contact's name to a TextView in the ListView Layout

        String[] from = new String[]{"name"};
        int[] to = new int[]{R.id.contactTextView};

        CursorAdapter contactAdapter =
                new SimpleCursorAdapter(ContactsBook.this, R.layout.contact_list_item,null,from,to, 0);
       //simpleCursorAdapter designed to simplify mapping Cursor Columns directly to TextViews of imageTextView
        setListAdapter(contactAdapter); //setcontactView's Adapter

        // end of onCreate

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.contactsbook,menu);
        return true;
    } // end of onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        Intent addNewContact = new Intent(ContactsBook.this,AddEditContacts.class);
        startActivity(addNewContact);

        return super.onOptionsItemSelected(item);
    }

    OnItemClickListener viewContactListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent viewContact = new Intent(ContactsBook.this,ViewContact.class);
        // pass the selected contact
            viewContact.putExtra(ROW_ID,id);
            startActivity(viewContact);
        }


    };


    @Override
    protected void onResume(){
        super.onResume();
        //create new GetContacts Task dn execute it
        new GetContactsTask().execute((Object[]) null);
    } //end of onResume

    @Override
    protected void onStop(){
        super.onStop();
        Cursor cursor = contactAdapter.getCursor();//get Current Cursor
        if(cursor!=null)
            cursor.deactivate();

        contactAdapter.changeCursor(null);//adapted now has no cursor
        super.onStop();
    }

    // method performs databse query outside GUI thread
    private class GetContactsTask extends AsyncTask<Object,Object,Cursor>{

        DatabaseConnector databaseConnector = new DatabaseConnector(ContactsBook.this);

        // perform the database Access
        @Override
        protected Cursor doInBackground(Object... objects) {
            databaseConnector.open();
            return databaseConnector.getAllContacts;
        }
    }
    //use the Cursor returned from the doingBackground method
    protected void onPostExecute(Cursor result){
        contactAdapter.changeCursor(result);
        DatabaseConnector.close();
    }

}


