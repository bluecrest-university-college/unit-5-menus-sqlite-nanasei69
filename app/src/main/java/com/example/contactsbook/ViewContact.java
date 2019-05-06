package com.example.contactsbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewContact extends AppCompatActivity {

    private long rowID;//selected contact name
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        // get EditTexts
        nameTextView=(TextView) findViewById(R.id.nameTextView);
        phoneTextView=(TextView) findViewById(R.id.phoneTextView);
        emailTextView=(TextView) findViewById(R.id.emailTextView);

        // get the selected contact's row ID
        Bundle extras=getIntent().getExtras();
        rowID=extras.getLong("row_id"); // small letter
    }

    @Override
    protected void onResume(){
        super.onResume();
        //LoadContactTask subClass of Asynctask
        new LoadContactTask().execute(rowID);
    }

    private class LoadContactTask extends AsyncTask<Long, Object, Cursor>{
        DatabaseConnector dbConn=new DatabaseConnector(ViewContact.this);
        private Long[] params;
        @Override
        protected Cursor doInBackground(Long... longs) {
            dbConn.open();
            return dbConn.getOneContact(params[0]);
        }

        @Override
        protected void onPostExecute(Cursor result){
            super.onPostExecute(result);
            result.moveToFirst();

            //get the column index for each data item
            int nameIndex=result.getColumnIndex("name");
            int phoneIndex=result.getColumnIndex("phone");
            int emailIndex=result.getColumnIndex("email");

            //fill TextViews with the retrieved data
            nameTextView.setText(result.getString(nameIndex));
            phoneTextView.setText(result.getString(phoneIndex));
            emailTextView.setText(result.getString(emailIndex));

            result.close();
            dbConn.close();
        }
    } // end class LoadContactTask


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuInflater Inflater=getMenuInflater();
        Inflater.inflate(R.menu.activity_view_contact,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item){

        switch (Item.getItemId())
        {
            case R.id.edititem:
                Intent addEditContacts=new Intent(this,AddEditContacts.class);
                addEditContacts.putExtra("row_id", rowID);
                addEditContacts.putExtra("name", nameTextView.getText());
                addEditContacts.putExtra("phone", phoneTextView.getText());
                addEditContacts.putExtra("email", emailTextView.getText());
                startActivity(addEditContacts);
                return true;

            case R.id.deleteitem:
                deleteContact(); //delete displayed Contact
                return true;

            default:
                return super.onOptionsItemSelected(Item);
        }



    }

    private void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewContact.this);
        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessage);

        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final DatabaseConnector dbConn=new DatabaseConnector(ViewContact.this);

                // create n Asynctask

                AsyncTask<Long, Object, Object> deleteTask=new AsyncTask<Long, Object, Object>(){

                    private Long[] params;

                    @Override
                    protected Object doInBackground(Long... longs) {
                        dbConn.deleteContact(params[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result){

                        finish(); // return to ContatcsBook Activity
                    }

                }; //end new AsyncTask

                    // execute the AsyncTask to delete contact at row
                    deleteTask.execute(rowID);


            }
        });//end call method setPositiveButton all one line

        builder.setNegativeButton(R.string.button_cancel,null);
        builder.show();
    }// end method deleteContact

}// end class ViewContact
