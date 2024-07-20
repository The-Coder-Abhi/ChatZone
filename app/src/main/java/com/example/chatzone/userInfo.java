package com.example.chatzone;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class userInfo extends AppCompatActivity {

    public static final String t1="com.example.chatzone.t1";
    public static final String t2="com.example.chatzone.t2";
    public static final String t3="com.example.chatzone.t3";
    public static final String t4="com.example.chatzone.t4";

    //private DatePickerDialog.OnDateSetListener dateSetListener1;
    public TextInputEditText BirthDate;
    int age;
    ProgressBar PB;

    Button select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        BirthDate = findViewById(R.id.BirthDate01);
        select = findViewById(R.id.SelectDate);
        BirthDate.setText(getTodaysDate());
        BirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c =Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int style = AlertDialog.THEME_HOLO_LIGHT;
                DatePickerDialog dateDialog = new DatePickerDialog(view.getContext(), style, datePickerListener,year,month,day);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c =Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int style = AlertDialog.THEME_HOLO_LIGHT;
                DatePickerDialog dateDialog = new DatePickerDialog(view.getContext(), style, datePickerListener,year,month,day);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
            }
        });
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,day);
            String format = new SimpleDateFormat("dd MM yyyy").format(c.getTime());
            BirthDate.setText(format);
            age = calculateAge(c.getTimeInMillis());
            //select.setText(Integer.toString(age));
        }
    };
    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int a = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH)< dob.get(Calendar.DAY_OF_MONTH)){
            a--;
        }
        return a;
    }
    public void nextpage(View view)
    {
        EditText date = (EditText) findViewById(R.id.BirthDate01);
        String BD = date.getText().toString();
        EditText FirstName = (EditText) findViewById(R.id.firstname);
        String FN = FirstName.getText().toString();
        EditText LastName = (EditText) findViewById(R.id.lastname);
        String LN = LastName.getText().toString();
        EditText Phone = (EditText) findViewById(R.id.PhoneNumber);
        String PN = Phone.getText().toString();
        int length = Phone.getText().length();
        if(TextUtils.isEmpty(BD)||TextUtils.isEmpty(FN)||TextUtils.isEmpty(LN)||TextUtils.isEmpty(PN))
        {
            Toast.makeText(this,"Input Field Cannot Be Empty",Toast.LENGTH_SHORT).show();
        }
        else if(age > 15)
        {
            Toast.makeText(this,"Not Eligible",Toast.LENGTH_SHORT).show();
        }
        else if(length != 10)
        {
            Toast.makeText(this, "Phone No. is InValid", Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(userInfo.this, SignUp.class);
            i.putExtra(t1, BD);
            i.putExtra(t2, FN);
            i.putExtra(t3, LN);
            i.putExtra(t4, PN);
            startActivity(i);
        }

    }
    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = new SimpleDateFormat("dd MM yyyy").format(cal.getTime());
        return date;

    }



}