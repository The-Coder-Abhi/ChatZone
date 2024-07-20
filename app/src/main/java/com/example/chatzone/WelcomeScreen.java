package com.example.chatzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

    }
    public void SignInPage(View view)
    {
        //Intent i = new Intent(WelcomeScreen.this,SignIn.class);
        Intent i = new Intent(WelcomeScreen.this,SignIn.class);
        startActivity(i);
    }
    public void SignUpPage(View view)
    {
        Intent i = new Intent(WelcomeScreen.this,userInfo.class);
        startActivity(i);
    }
    /*
    @Override
    protected void onStart() {
        super.onStart();

        mUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                User = mAuth.getCurrentUser();
                if(User!=null)
                {
                    Intent i = new Intent(WelcomeScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }*/
}