package com.example.contactsbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEditContacts extends AppCompatActivity {

    private long rowID;

    //EditTexts
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contacts);

        nameEditText=(EditText)findViewById(R.id.nameEditText);
        phoneEditText=(EditText)findViewById(R.id.phoneEditText);
        emailEditText=(EditText)findViewById(R.id.emailEditText);

        Bundle extras=getIntent().getExtras();

        if(extras!=null){
            rowID = extras.getLong("row_id");
            nameEditText.setText(extras.getString("name"));
            phoneEditText.setText(extras.getString("phone"));
            emailEditText.setText(extras.getString("email"));
        }

        Button saveContactButton=(Button) findViewById(R.id.saveContactButton);
        saveContactButton.setOnClickListener(saveContactButtonClicked);

    }//end OnCreate

    OnClickListener saveContactButtonClicked=new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nameEditText.getText().length()!=0){
                final AsyncTask<Object, Object, Object> saveContactTask=new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {

                        saveContact();

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result){
                        finish();
                    }

                }; //end AsyncTask

                saveContactTask.execute((Object[])null);

            }else{
                AlertDialog.Builder builder=new AlertDialog.Builder(AddEditContacts.this);
                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton,null);
                builder.show();
            }
        }//end Method OnClick
    };// end OnClicklistener


    private void saveContact(){
        DatabaseConnector dbConn=new DatabaseConnector(this);
        if(getIntent().getExtras()==null){
            dbConn.insertContact(
                    nameEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    emailEditText.getText().toString());
        }
        else {
            dbConn.updateContact( rowID,
                    nameEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    emailEditText.getText().toString());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_edit_contacts,menu);
        return true;
    }

}
