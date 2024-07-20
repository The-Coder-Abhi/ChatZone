package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {

    private static int Timer=3000;
    FirebaseUser mUser,User;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                        if(mUser!=null)
                        {
                            Intent i = new Intent(Splash_Screen.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Intent i=new Intent(Splash_Screen.this,WelcomeScreen.class);
                            startActivity(i);
                            finish();
                        }

            }
        },Timer);
    }
}