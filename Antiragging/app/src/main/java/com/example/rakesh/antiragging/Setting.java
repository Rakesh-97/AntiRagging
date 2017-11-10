package com.example.rakesh.antiragging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.rakesh.antiragging.MainActivity.messagestring;
import static com.example.rakesh.antiragging.MainActivity.s;


/**
 * Created by Rakesh on 7/26/2017.
 */

public class Setting extends AppCompatActivity {
    Button buttonPickContact;
    Button buttonPickSMS;
    TextView showsms;
    TextView contactNumber;
    EditText emergencysms;
    EditText contactno;
    private static final int PICK_CONTACT =1000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        emergencysms = (EditText) findViewById(R.id.messageid);
        contactno = (EditText) findViewById(R.id.contactid);
        showsms= (TextView) findViewById(R.id.textview2);
        showsms.setText(messagestring);
        contactNumber = (TextView)findViewById(R.id.textview);
        contactNumber.setText(s);

        SQLiteDatabase db = openOrCreateDatabase("data",MODE_PRIVATE,null);
        db.execSQL("create table if not exists data(Contact_num varchar,Message varchar)");
        Cursor resultSet = db.rawQuery("Select * from data",null);

        if(resultSet.moveToNext()){
            s=resultSet.getString(0);
            contactNumber.setText(s);
            messagestring = resultSet.getString(1);
            showsms.setText(messagestring);
        }

        buttonPickContact = (Button)findViewById(R.id.button2);
        buttonPickContact.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }});

        buttonPickSMS = (Button) findViewById(R.id.button3);
        buttonPickSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emergencysms = (EditText) findViewById(R.id.messageid);
                messagestring = emergencysms.getText().toString();
                showsms.setText(messagestring);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SQLiteDatabase db = openOrCreateDatabase("data",MODE_PRIVATE,null);
        db.execSQL("create table if not exists data(Contact_num varchar,Message varchar)");
        db.execSQL("delete from data");
        db.execSQL("insert into data values('"+s+"','"+messagestring+"')");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number =cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //contactNumber = (TextView)findViewById(R.id.textview);
                //contactName.setText(name);
                s=number;
                //String name =cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //contactNumber.setText(name);
                //contactNumber.setText(number);
                //contactEmail.setText(email);
                Toast.makeText(getBaseContext(),"Number is set",Toast.LENGTH_LONG).show();

            }
        }
    }
}